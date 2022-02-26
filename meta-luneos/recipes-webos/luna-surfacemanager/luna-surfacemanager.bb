# Copyright (c) 2013-2021 LG Electronics, Inc.

SUMMARY = "The core of the Luna Surface Manager (compositor)"
AUTHOR  = "Anupam Kaul <anupam.kaul@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2c7c706c6a586a6abec428c64006d86b \
"

DEPENDS = "qtdeclarative wayland-native qtwayland qtwayland-native qt-features-webos pmloglib webos-wayland-extensions glib-2.0"

#WEBOS_VERSION = "2.0.0-360_5c3a00a694cc9c4ef33e910a7a20a241445b1083"
#PR = "r52"

inherit webos_qmake5
inherit pkgconfig
#inherit webos_enhanced_submissions
inherit webos_lttng
inherit webos_ports_ose_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
           file://0001-Fix-build-for-Qt-5.15.2.patch \
           file://surface-manager.service \
           "
S = "${WORKDIR}/git"

SRCREV = "c3b70c0f4684960ff33a09e493f95924ddc7835a"

OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

# Enable LTTng tracing capability when enabled in webos_lttng class
EXTRA_QMAKEVARS_PRE += "${@ 'CONFIG+=lttng' if '${WEBOS_LTTNG_ENABLED}' == '1' else '' }"

EXTRA_QMAKEVARS_PRE += "${PACKAGECONFIG_CONFARGS} WEBOS_INSTALL_TESTSDIR=${webos_testsdir}"

# We don't support configuring via cmake
EXTRA_QMAKEVARS_POST += "CONFIG-=create_cmake"

FILES:${PN}-dev += " \
    ${OE_QMAKE_PATH_QT_ARCHDATA}/mkspecs/* \
    ${OE_QMAKE_PATH_LIBS}/*.prl \
"

do_install:append() {
    sed -i 's@prefix=${STAGING_DIR_HOST}@prefix=@g;s@-L${STAGING_DIR_HOST} @ @g;' ${D}${libdir}/pkgconfig/*.pc
    sed -i "s@-L${STAGING_LIBDIR}@-L\${libdir}@g" ${D}${libdir}/pkgconfig/*.pc
    if ${@bb.utils.contains('PACKAGECONFIG', 'compositor', 'true', 'false', d)}; then
        install -d ${D}${datadir}/webos-keymap
        ${STAGING_DIR_NATIVE}${OE_QMAKE_PATH_QT_BINS}/generate_qmap ${D}${datadir}/webos-keymap/webos-keymap.qmap
    fi
    
    # This dummy import conflicts with the ${OE_QMAKE_PATH_QML}/WebOSCompositor import we use for luna-next-cardshell
    rm -rf ${D}${OE_QMAKE_PATH_QML}/WebOSCompositorBase/imports/WebOSCompositor
    
    install -d ${D}${systemd_system_unitdir}
    install -v -m 0644 ${WORKDIR}/surface-manager.service ${D}${systemd_system_unitdir}/surface-manager.service
}

VIRTUAL-RUNTIME_gpu-libs ?= ""
RDEPENDS:${PN} += "${VIRTUAL-RUNTIME_gpu-libs}"

inherit webos_system_bus
inherit webos_qmllint
inherit systemd

# qt-features-webos have its own logic to install system bus files reason for
# that is because only qmake knows where substitued files will be placed.
WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"

# Perform extra QML validation
# WEBOS_QMLLINT_EXTRA_VALIDATION = "1"

PACKAGECONFIG ??= "multi-input"
PACKAGECONFIG[compositor] = "CONFIG+=compositor_base,,qt-features-webos-native"
PACKAGECONFIG[multi-input] = ",CONFIG+=no_multi_input,"
PACKAGECONFIG[cursor-theme] = "CONFIG+=cursor_theme,,"

PACKAGECONFIG = "compositor cursor-theme"

PACKAGES =+ "${PN}-conf ${PN}-base ${PN}-base-tests"

SYSTEMD_SERVICE:${PN} = "surface-manager.service"

FILES:${PN}-conf += " \
    ${sysconfdir}/surface-manager.d/ \
    ${systemd_system_unitdir} \
    ${webos_sysbus_apipermissionsdir} \
    ${webos_sysbus_groupsdir} \
    ${webos_sysbus_servicedir} \
    ${webos_sysbus_manifestsdir}/luna-surfacemanager.manifest.json \
    ${webos_sysbus_permissionsdir}/com.webos.surfacemanager.perm.json \
    ${webos_sysbus_rolesdir}/com.webos.surfacemanager.role.json \
"

FILES:${PN}-base += " \
    ${OE_QMAKE_PATH_QML}/WebOSCompositorBase/ \
    ${OE_QMAKE_PATH_QML}/WebOSCompositor/ \
    ${OE_QMAKE_PATH_BINS}/ \
    ${datadir}/icons/ \
    ${datadir}/webos-keymap/webos-keymap.qmap \
"

FILES:${PN}-base-tests += " \
    ${webos_applicationsdir}/ \
    ${webos_sysbus_manifestsdir}/ \
    ${webos_sysbus_permissionsdir}/ \
    ${webos_sysbus_rolesdir}/ \
    ${webos_testsdir}/${BPN}/ \
"

RDEPENDS:${PN}-base += "xkeyboard-config qml-webos-framework qml-webos-bridge qml-webos-components"