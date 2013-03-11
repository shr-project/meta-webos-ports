FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 3}"

RDEPENDS_${PN} += "qt-webos-plugin"

inherit webos-ports-submissions

SRCREV = "03343962a3893dbef7184139722fbdf99f68106b"
