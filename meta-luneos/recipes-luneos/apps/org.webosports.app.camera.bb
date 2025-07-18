SUMMARY = "Camera application written from scratch for webOS ports"
SECTION = "webos/apps"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=84dcc94da3adb52b53ae4fa38fe49e5d \
"

PV = "0.0.2-1+git"
SRCREV = "fbcbee642deaeb725c35372995e1eeb9e884444a"

DEPENDS = "qtbase qtdeclarative qtdeclarative-native qtmultimedia"

WEBOS_GIT_PARAM_BRANCH = "herrie/qt6"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    file://0001-CMakeLists.txt-move-camera_shaders-to-camera-qml-tar.patch \
"

inherit pkgconfig
inherit webos_ports_repo
inherit webos_application
inherit webos_filesystem_paths
inherit webos_tweaks

inherit qt6-cmake
inherit webos_cmake

INSANE_SKIP:${PN} = "libdir"
INSANE_SKIP:${PN}-dbg = "libdir"

FILES:${PN} += "${webos_applicationsdir}/org.webosports.app.camera"

RDEPENDS:${PN} = " \
    qtdeclarative-qmlplugins \
"

