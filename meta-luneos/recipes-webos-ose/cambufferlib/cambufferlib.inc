# Copyright (c) 2022-2025 LG Electronics, Inc.

SUMMARY = "Camera Shared Memory library for webOS"
AUTHOR  = "Sungho Lee <shl.lee@lge.com>"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"
inherit webos_component
inherit webos_cmake
inherit webos_system_bus
inherit webos_enhanced_submissions
inherit webos_public_repo
inherit webos_pkgconfig

CLANG_DEPENDENCY_SUFFIX ?= ""
DEPENDS = "pmloglib camera-utils${CLANG_DEPENDENCY_SUFFIX}"

CXXFLAGS += " \
    -I${STAGING_INCDIR}/camera${CLANG_DEPENDENCY_SUFFIX} \
"

WEBOS_VERSION = "1.0.0-17_7f4fed57d44bed5a16ed72ca16e81dd9213100dc"
INC_PR = "r4"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"

PACKAGECONFIG[testapp] = "-DTEST_APP=ON, -DTEST_APP=OFF, opencv, "
