# Copyright (c) 2017-2021 LG Electronics, Inc.

SUMMARY = "iLib Qml loader"
AUTHOR = "Goun Lee <goun.lee@lge.com>"
SECTION = "libs/qtplugin"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
    file://oss-pkg-info.yaml;md5=391133fb0a5ff786d7d30c07467b5d21 \
"

DEPENDS = "qtdeclarative"

PV = "11.0.0-6+git${SRCPV}"
SRCREV = "bb19fd3dc52845540c9cf6b944cca773ff48c280"

inherit webos_qmake5
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILE_READER_PROJECT = "filereader-webos.pro"
QMAKE_PROFILES = "${S}/${FILE_READER_PROJECT}"

OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

FILES:${PN} += "${OE_QMAKE_PATH_QML}/*"