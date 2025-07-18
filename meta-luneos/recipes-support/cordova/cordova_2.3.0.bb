SUMMARY = "PhoneGap is a web platform that exposes native mobile device apis and data to JavaScript."
HOMEPAGE = "http://phonegap.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=aa80bcec645b0f6ec37d5954ee6dc056"

WEBOS_REPO_NAME = "phonegap"
PV = "2.3.0-1+git"
SRCREV = "368e2da0c0db11f219560ec1c10ca0417df0c1c4"

inherit webos_filesystem_paths
inherit webos_ports_fork_repo
inherit allarch

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

do_install() {
    install -d ${D}${webos_frameworksdir}/cordova/
    install -m 0644 ${S}/lib/webos/lib/cordova.webos.js ${D}${webos_frameworksdir}/cordova/cordova.js
}

PACKAGES = "${PN}"
FILES:${PN} = "${webos_frameworksdir}/cordova"
