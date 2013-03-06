FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 2}"

inherit webos-ports-submissions

SRCREV = "03343962a3893dbef7184139722fbdf99f68106b"
