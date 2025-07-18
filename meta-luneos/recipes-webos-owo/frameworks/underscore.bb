# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "The underscore.js utility-belt library for JavaScript made into an Open webOS loadable framework"
SECTION = "webos/frameworks"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "0.6.0-8+git"
SRCREV = "b52073f0255e8982e890e3843d43fdf136798a53"

inherit webos_public_repo
inherit webos_filesystem_paths
inherit allarch

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"

do_install() {
    install -d ${D}${webos_frameworksdir}/underscore/version/1.0/
    cp -vrf ${S}/* ${D}${webos_frameworksdir}/underscore/version/1.0/
}

FILES:${PN} += "${webos_frameworksdir}"
