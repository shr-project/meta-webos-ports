# Copyright (c) 2013-2025 LG Electronics, Inc.

SUMMARY = "webOS uMediaserver daemon and utilities"
AUTHOR = "Sujeet Nayak <Sujeet.nayak@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 libpbnjson libconfig swig-native libxml2 luna-service2 pmloglib boost luna-prefs"
DEPENDS += "gstreamer1.0 gstreamer1.0-plugins-base"
DEPENDS += "${@'' if '${WEBOS_DISTRO_PRERELEASE}' == '' else 'pmtrace'}"
RDEPENDS:${PN} = "umediaserver-configs"

INC_PR = "r29"

inherit webos_component
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_library
inherit webos_daemon
inherit webos_system_bus
#inherit webos_machine_dep
inherit python3-dir
inherit python3native
inherit webos_public_repo
inherit webos_prerelease_dep

WEBOS_VERSION = "1.0.0-gav.50_b56a3469edb8aa87056482c0648d0819f3935f9c"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-uMediaserver-add-missing-include-to-fix-build-with-b.patch \
    file://0001-python-fix-build-with-python-3.13.patch \
"

# umediaserver-python contains the Python bindings
PACKAGES =+ "${PN}-python"

FILES:${PN}-python = "${libdir}/${PYTHON_DIR}/site-packages/uMediaServer/* ${datadir}/${BPN}/python/"

# Use append, because umediaserver-clang uses:
# PACKAGECONFIG += "${@bb.utils.contains('USE_WEBRUNTIME_LIBCXX', '1', 'webruntime-libcxx', 'system-libcxx', d)}"

do_install:append() {
    install -m 755 -o media -g media -v -d ${D}${webos_execstatedir}/ums
}
