# Copyright (c) 2013-2025 LG Electronics, Inc.

DESCRIPTION = "System Application Manager"
AUTHOR = "Guruprasad KN <guruprasad.kn@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 luna-service2 libpbnjson boost icu pmloglib libwebosi18n"
RDEPENDS:${PN} = "ecryptfs-utils"
RDEPENDS:${PN} += "${VIRTUAL-RUNTIME_webos-customization}"

VIRTUAL-RUNTIME_webos-customization ?= ""

WEBOS_VERSION = "2.0.0-77_7afc802ab0499a7f84e64f3f142b26682d996878"
PR = "r31"

inherit webos_component
inherit webos_cmake
inherit webos_enhanced_submissions
inherit webos_daemon
inherit webos_system_bus
#inherit webos_distro_variant_dep
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
	file://0001-com.webos.sam.role.json.in-Fix-various-outbound-perm.patch \
	file://0002-Allow-getAppBasePath-also-from-trusted-apps.patch \
	file://0003-RunningApp-disable-killer-timeout-for-app-relaunch.patch \
	file://0004-Setup-QML-style-for-LuneOS.patch \
	file://0005-Handle-noWindow-apps.patch \
	file://0006-AppDescription.h-Add-org.webosports-as-privileged-as.patch \
	file://0007-Setup-QT_IM_MODULE-for-client-apps.patch \
	file://0008-NativeContainer-configure-native-apps.patch \
	file://0009-Setup-QT_WAYLAND_SHELL_INTEGRATION-for-webOS.patch \
	file://0001-CMakeLists.txt-replace-std-gnu-0x-with-std-c-17-for-.patch \
"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "sam.service"
WEBOS_SYSTEMD_SCRIPT = "sam.sh"

PACKAGECONFIG:append = " ${@bb.utils.filter('DISTRO_FEATURES', 'smack', d)}"
PACKAGECONFIG[smack] = "-Dapply_webos_smack:BOOL=True"

PACKAGES =+ "${PN}-tests"
ALLOW_EMPTY:${PN}-tests = "1"
FILES:${PN}-tests = "${libexecdir}/tests/*"
