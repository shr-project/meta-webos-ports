# Copyright (c) 2023 Herman van Hazendonk <github.com@herrie.org>

DESCRIPTION = "Binder based ofono plugin (Needed for Android 8.0+)."
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=37fe900f9ece53e2621d89780f2031be"
SECTION = "webos/support"

DEPENDS = "ofono libgbinder libgbinder-radio glib-2.0 libglibutil libofonobinderpluginext"

inherit pkgconfig

SRC_URI = "git://github.com/mer-hybris/ofono-binder-plugin.git;branch=master;protocol=https"

PV = "1.1.12"
SRCREV = "0bd4932f0c30187cd90ac91fe40c9e7131ed6110"

CFLAGS += "--sysroot=${RECIPE_SYSROOT} "
LDFLAGS += "--sysroot=${RECIPE_SYSROOT} "

EXTRA_OEMAKE = "KEEP_SYMBOLS=1 CROSS_COMPILE=${TARGET_PREFIX} CC='${CC}'"
PARALLEL_MAKE = ""

do_install() {
    make install DESTDIR=${D}
}

FILES:${PN} += "/usr/lib/ofono/plugins/binderplugin.so"
