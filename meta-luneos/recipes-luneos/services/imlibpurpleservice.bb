SUMMARY = "Instant Messaging service"
SECTION = "webos/services"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=263f341c01743dbd6b06ae75369dbeed \
    file://COPYRIGHT;md5=2ce083d13f0f21e5207b4115c8926450 \
"

DEPENDS = "glib-2.0 db8 pidgin luna-service2 tidy-html5"

PV = "3.0.5+git"
SRCREV = "134cf24abf82d019cc645599c39f360c3875a6b7"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

RRECOMMENDS:${PN} += " \
    pidgin-sipe \
    purple-matrix \
    funyahoo-plusplus \
    icyque \
    libpurple-plugin-autoaccept \
    libpurple-plugin-buddynote \
    libpurple-plugin-idle \
    libpurple-plugin-joinpart \
    libpurple-plugin-log_reader \
    libpurple-plugin-newline \
    libpurple-plugin-offlinemsg \
    libpurple-plugin-psychic \
    libpurple-plugin-ssl \
    libpurple-plugin-ssl-gnutls \
    libpurple-plugin-statenotify \
    libpurple-protocol-bonjour \
    libpurple-protocol-gg \
    libpurple-protocol-irc \
    libpurple-protocol-novell \
    libpurple-protocol-simple \
    libpurple-protocol-xmpp \
    libpurple-protocol-zephyr \
"

do_install:append() {
    cp -R --no-dereference --preserve=mode,links -v ${S}/files/etc ${D}
}

CXXFLAGS += "-fpermissive"
