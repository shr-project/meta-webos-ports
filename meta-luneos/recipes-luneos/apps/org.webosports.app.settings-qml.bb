SUMMARY = "Settings app written from scratch in QML for LuneOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "qtbase qtdeclarative qtdeclarative-native"

inherit webos_ports_repo
inherit webos_filesystem_paths

inherit qt6-cmake
inherit webos_cmake
inherit webos_app
inherit pkgconfig

PV = "0.4.0-1+git"
SRCREV = "9a5b5527ec4c6a153c4c07c099e07cdb3dcb01c8"

WEBOS_GIT_PARAM_BRANCH = "qml-based"
WEBOS_REPO_NAME = "org.webosports.app.settings"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# We don't provide android-property-service on non-Android devices, so remove it in these cases to avoid LS2 warnings/errors

REMOVE_ANDROID_PROPERTY_SERVICE_CMD:halium = ""
REMOVE_ANDROID_PROPERTY_SERVICE_CMD = "sed -i 's/\"android-property-service.operation\", //g' ${D}/${webos_applicationsdir}/org.webosports.app.settings.deviceinfo/appinfo.json"

do_install:append() {
    ${REMOVE_ANDROID_PROPERTY_SERVICE_CMD}
}

FILES:${PN} += "${webos_applicationsdir}/org.webosports.app.settings-common \
                ${webos_applicationsdir}/org.webosports.app.settings.bluetooth \
                ${webos_applicationsdir}/org.webosports.app.settings.networksettings \
                ${webos_applicationsdir}/org.webosports.app.settings.vpn \
                ${webos_applicationsdir}/org.webosports.app.settings.wifi \
                ${webos_applicationsdir}/org.webosports.app.settings.backup \
                ${webos_applicationsdir}/org.webosports.app.settings.certificate \
                ${webos_applicationsdir}/org.webosports.app.settings.dateandtime \
                ${webos_applicationsdir}/org.webosports.app.settings.deviceinfo \
                ${webos_applicationsdir}/org.webosports.app.settings.devmodeswitcher \
                ${webos_applicationsdir}/org.webosports.app.settings.exhibitionpreferences \
                ${webos_applicationsdir}/org.webosports.app.settings.help \
                ${webos_applicationsdir}/org.webosports.app.settings.languagepicker \
                ${webos_applicationsdir}/org.webosports.app.settings.location \
                ${webos_applicationsdir}/org.webosports.app.settings.screenlock \
                ${webos_applicationsdir}/org.webosports.app.settings.searchpreferences \
                ${webos_applicationsdir}/org.webosports.app.settings.soundsandalerts \
                ${webos_applicationsdir}/org.webosports.app.settings.textassist \
                ${webos_applicationsdir}/org.webosports.app.settings.updates \
        ${datadir}/ls2 \
        "

RDEPENDS:${PN} = " \
    qtdeclarative-qmlplugins \
"
