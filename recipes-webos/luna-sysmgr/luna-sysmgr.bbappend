FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 9}"

SRC_URI_append = " \
    file://enable-webosports-first-use.patch \
    file://dont-spawn-bootanimation-process.patch \
    file://use-webosports-wifi-settings-app.patch"

inherit webos-ports-submissions

SRCREV = "b93317b95b30643ca8d7e350f48febaa89591825"
