#!/usr/bin/env python3
#
# Taken from older revision of https-everywhere where it was first replaced
# with make-json.py in 5.1.5:
# https://github.com/EFForg/https-everywhere/commit/79536a2f39380eaeeb96b7895f9616435e54136d
# and then make-json.py was dropped as well in 2017.10.24:
# https://github.com/EFForg/https-everywhere/commit/809238db223026d1a123dd5a9e0a643a2eeeaaa5
#
# Builds an sqlite DB and a JSON DB containing all the rulesets, indexed by target.
# The sqlite DB is used by trivial-validate.py. The JSON DB is used by the
# Firefox addon.
#

import glob
import locale
import json
import os
import re
import sqlite3
import subprocess
import sys

import collections
from lxml import etree

# Explicitly set locale so sorting order for filenames is consistent.
# This is important for deterministic builds.
# https://trac.torproject.org/projects/tor/ticket/11630#comment:20
# It's also helpful to ensure consistency for the lowercase check below.
locale.setlocale(locale.LC_ALL, 'C')

json_path = os.path.join(os.path.dirname(__file__), 'rulesets.json')
# Removing the file before we create it avoids some non-determinism.
db_path = os.path.join(os.path.dirname(__file__), 'rulesets.sqlite')
if os.path.isfile(db_path):
    os.remove(db_path)
conn = sqlite3.connect(db_path)
c = conn.cursor()
c.execute('''DROP TABLE IF EXISTS rulesets''')
c.execute('''CREATE TABLE rulesets
             (id INTEGER PRIMARY KEY,
              contents TEXT)''')
c.execute('''DROP TABLE IF EXISTS targets''')
c.execute('''CREATE TABLE targets
             (host TEXT,
              ruleset_id INTEGER)''')

json_output = {
    "rulesetStrings": [],
    "targets": collections.defaultdict(list)
}

parser = etree.XMLParser(remove_blank_text=True)

# Precompile xpath expressions that get run repeatedly.
xpath_host = etree.XPath("/ruleset/target/@host")
xpath_ruleset = etree.XPath("/ruleset")

# Sort filenames so output is deterministic.
filenames = sorted(glob.glob('src/chrome/content/rules/*'))

counted_lowercase_names = collections.Counter([name.lower() for name in filenames])
most_common_entry = counted_lowercase_names.most_common(1)[0]
if most_common_entry[1] > 1:
    dupe_filename = re.compile(re.escape(most_common_entry[0]), re.IGNORECASE)
    print(("%s failed case-insensitivity testing." % list(filter(dupe_filename.match, filenames))))
    print("Rules exist with identical case-insensitive names, which breaks some filesystems.")
    sys.exit(1)

for fi in filenames:
    if fi.endswith('/00README') or fi.endswith('/make-trivial-rule') or fi.endswith('/default.rulesets'):
        continue

    if " " in fi:
        print(("%s failed validity: Rule filenames cannot contain spaces" % (fi)))
        sys.exit(1)
    if not fi.endswith('.xml'):
        print(("%s failed validity: Rule filenames must end in .xml" % (fi)))
        sys.exit(1)

    try:
        tree = etree.parse(fi, parser)
    except Exception as oops:
        print(("%s failed XML validity: %s\n" % (fi, oops)))
        sys.exit(1)

    # Remove comments to save space.
    etree.strip_tags(tree, etree.Comment)

    targets = xpath_host(tree)
    if not targets:
        print(('File %s has no targets' % fi))
        sys.exit(1)

    # Strip out the target tags. These aren't necessary in the DB because
    # targets are looked up in the target table, which has a foreign key
    # pointing into the ruleset table.
    etree.strip_tags(tree, 'target')
    etree.strip_tags(tree, 'test')

    # Store the filename in the `f' attribute so "view source XML" for rules in
    # FF version can find it.
    xpath_ruleset(tree)[0].attrib["f"] = os.path.basename(fi)

    c.execute('''INSERT INTO rulesets (contents) VALUES(?)''', (etree.tostring(tree),))
    ruleset_id = c.lastrowid
    for target in targets:
        c.execute('''INSERT INTO targets (host, ruleset_id) VALUES(?, ?)''', (target, ruleset_id))
        # id is the current length of the rules list - i.e. the offset at which
        # this rule will be added in the list.
        json_output["targets"][target].append(str(len(json_output["rulesetStrings"])))
    json_output["rulesetStrings"].append(str(etree.tostring(tree)))

conn.commit()
conn.execute("VACUUM")
conn.close()

with open(json_path, 'w') as f:
    f.write(json.dumps(json_output))
