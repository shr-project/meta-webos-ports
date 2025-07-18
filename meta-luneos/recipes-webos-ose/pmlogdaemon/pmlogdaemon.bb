# Copyright (c) 2012-2025 LG Electronics, Inc.

SUMMARY = "webOS logging daemon"
AUTHOR = "Sukil Hong <sukil.hong@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "pmloglib zlib glib-2.0 libpbnjson pmloglib-private luna-service2"
# show_disk_usage.sh script uses mktemp, find, xargs, and du, all of which are
# provided by busybox.
RDEPENDS:${PN} = "busybox"

WEBOS_VERSION = "3.1.0-19_bbaf3b110429a8eaa6ba747019253bf4957a455f"
PR = "r14"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_daemon
inherit webos_system_bus
inherit webos_pmlog_config

PACKAGECONFIG ??= ""
PACKAGECONFIG[whitelist] = "-DENABLE_WHITELIST:BOOL=TRUE, -DENABLE_WHITELIST:BOOL=FALSE"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Make-PmLogDaemon-compatible-with-systemd.patch \
    ${@bb.utils.contains('PACKAGECONFIG', 'whitelist', 'file://whitelist.txt', '', d)} \
"

do_install:append() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'whitelist', 'true', 'false', d)} ; then
        install -m 644 ${UNPACKDIR}/whitelist.txt ${D}${sysconfdir}/PmLogDaemon
    fi
}
FILES:${PN} += "${datadir}/PmLogDaemon"
