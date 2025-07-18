SUMMARY = "Voice call management engine"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://src/main.cpp;beginline=1;endline=18;md5=a513bca9b17080e89422924a39fd0eb0"

DEPENDS += "qtbase qtmultimedia libqofono"

PV = "0.7.14+git"
SRCREV = "4285715324c902a28b402e3370fa765fa82c0134"

inherit qt6-qmake
inherit systemd
inherit pkgconfig

SRC_URI = "git://github.com/sailfishos/voicecall.git;protocol=https;branch=master \
           file://voicecall-manager.service \
           file://0001-Qt6-migration-patch.patch \
           file://0001-src.pro-use-qofono-qt6-instead-of-qofono-qt5.patch \
"

#enable debugging in voicecall. This is now merged upstream so we don't need patches anymore to enable this for each individual file.
EXTRA_QMAKEVARS_PRE += "CONFIG+=enable-debug QT6_INSTALL_LIBDIR=${QT6_INSTALL_LIBDIR}"

# Separated build dirs doesn't work with this component currently due to the way it deals
# with its self build dependencies.
B = "${S}"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "voicecall-manager.service"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/voicecall-manager.service ${D}${systemd_unitdir}/system/
}

FILES:${PN} += "${OE_QMAKE_PATH_QML} \
                ${libdir}/oneshot.d \
"
