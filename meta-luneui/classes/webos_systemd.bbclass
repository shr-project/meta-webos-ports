# Copyright (c) 2023-2024 LG Electronics, Inc.
#
# webos_systemd
#
# This class is to be inherited by the recipe for every component which has systemd units like *.{service,path,target,sh}
#

inherit systemd

FILESEXTRAPATHS:prepend = "${LAYERDIR_meta-luneui}/files:"
SRC_URI:append = " \
    file://replace.cmake \
    ${@' '.join(['file://' + f for f in '${WEBOS_SYSTEMD_SERVICE}'.split()])} \
    ${@' '.join(['file://' + f for f in '${WEBOS_SYSTEMD_SCRIPT}'.split()])} \
"

LUNEOS_SYSTEMD_SERVICE ?= ""
WEBOS_SYSTEMD_SERVICE ?= ""
WEBOS_SYSTEMD_SCRIPT ?= ""

def removesuffix(text, suffix):
    if text.endswith(suffix):
        return text[:-len(suffix)]
    else:
        return text

SYSTEMD_SERVICE:${PN} ?= "${@' '.join([removesuffix(f, '.in') for f in '${LUNEOS_SYSTEMD_SERVICE} ${WEBOS_SYSTEMD_SERVICE}'.split()])}"

#SYSTEMD_AUTO_ENABLE = "disable"

# User and group will be set as root if webos-dac is not in distro features.
WEBOS_SYSTEMD_USER ?= "root"
WEBOS_SYSTEMD_GROUP ?= "root"

install_units() {
    install -d ${WORKDIR}/staging-units

    if [ -n "${LUNEOS_SYSTEMD_SERVICE}" ]; then
        for f in ${LUNEOS_SYSTEMD_SERVICE}; do
            cp ${S}/files/systemd/${LUNEOS_SYSTEMD_SERVICE} ${WORKDIR}/
            cp ${S}/files/systemd/${LUNEOS_SYSTEMD_SERVICE} ${WORKDIR}/staging-units/
        done
    fi

    for f in ${WEBOS_SYSTEMD_SERVICE} ${WEBOS_SYSTEMD_SCRIPT}; do
        cp ${UNPACKDIR}/$f ${WORKDIR}/staging-units/
    done

    if [ $(ls ${WORKDIR}/staging-units | wc -l) -gt 0 ]; then
        ls ${UNPACKDIR}/replace.cmake >/dev/null 2>/dev/null && cp ${UNPACKDIR}/replace.cmake ${WORKDIR}/staging-units/CMakeLists.txt
        (cd ${WORKDIR} && \
         cmake staging-units \
                 -DIN_FILES="${LUNEOS_SYSTEMD_SERVICE} ${WEBOS_SYSTEMD_SERVICE} ${WEBOS_SYSTEMD_SCRIPT}" \
                 -DCMAKE_INSTALL_UNITDIR="${D}${systemd_system_unitdir}" \
                 -DWEBOS_SYSTEMD_USER="${@bb.utils.contains('DISTRO_FEATURES', 'webos-dac', '${WEBOS_SYSTEMD_USER}', 'root', d)}" \
                 -DWEBOS_SYSTEMD_GROUP="${@bb.utils.contains('DISTRO_FEATURES', 'webos-dac', '${WEBOS_SYSTEMD_GROUP}', 'root', d)}" \
                 ${WEBOS_SYSTEMD_REPLACE_OTHERS} && \
         make install && \
         rm -rf Makefile)
    fi

    rm -rf ${WORKDIR}/staging-units
}

do_install[postfuncs] += "install_units"
do_install[depends] += "cmake-native:do_populate_sysroot cmake-modules-webos-native:do_populate_sysroot"

FILES:${PN} += "${systemd_system_unitdir}/*"
