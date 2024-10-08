SUMMARY = "Distribution specific configuration for Pulseaudio"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = " \
    file://webos-system.pa \
    file://pulseaudio.conf \
"

do_install() {
    install -d ${D}${sysconfdir}/pulse
    install -m 0644 ${UNPACKDIR}/webos-system.pa ${D}${sysconfdir}/pulse/

    install -d ${D}${sysconfdir}/default
    install -m 0644 ${UNPACKDIR}/pulseaudio.conf  ${D}${sysconfdir}/default/pulseaudio.conf
}

FILES:${PN} = "${sysconfdir}/pulse ${sysconfdir}/default"
