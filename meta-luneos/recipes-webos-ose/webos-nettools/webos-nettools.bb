# Copyright (c) 2021-2024 LG Electronics, Inc.

DESCRIPTION = "Luna-service2 service providing network utility tools like ping and arping"
AUTHOR = "Muralidhar N <muralidhar.n@lge.com>"
SECTION = "webos/services"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=2763f3ed850f8412903ea776e0526bea \
    file://oss-pkg-info.yaml;md5=19d9ec0fe1295511ff6de5bf74c43d46 \
"

DEPENDS = "luna-service2 libpbnjson glib-2.0"
RDEPENDS:${PN} = "iputils"

WEBOS_REPO_NAME = "com.webos.service.nettools"

WEBOS_VERSION = "1.1.0-6_5ebd0866e9709d88db9c433746ccfcbc7561d48f"
PR = "r1"

inherit pkgconfig
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_system_bus

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

# http://gecko.lge.com:8000/Errors/Details/1142232
# webos-nettools/1.1.0-43/git/src/main.c:51:13: error: too many arguments to function 'initialize_nettools_ls2_calls'; expected 0, have 1
CFLAGS += "-std=gnu17"
