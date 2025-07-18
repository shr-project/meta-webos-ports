# Copyright (c) 2021-2023 LG Electronics, Inc.

DESCRIPTION = "webOS audiofocusmanager"
AUTHOR = "Sushovan G <sushovan.g@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 libpbnjson luna-service2 pmloglib"

WEBOS_VERSION = "1.0.0-8_a1d3b3b0e029aa69d6718dce6c68e03591ee3acf"
PR = "r4"

inherit pkgconfig
inherit webos_cmake
inherit webos_system_bus
inherit gettext
inherit webos_lttng
inherit webos_public_repo
inherit webos_enhanced_submissions

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "audiofocusmanager.service"
