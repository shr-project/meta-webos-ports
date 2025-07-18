SUMMARY = "webOS Ports Developer mode management service"
SECTION = "webos/services"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

inherit webos_ports_repo
inherit allarch
inherit webos_filesystem_paths
inherit webos_system_bus

PV = "0.1.0+git"
SRCREV = "65588c2d9c74a89d794fb7b3ea2d8da5df1b9921"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"

do_install() {
    # Install service and remove unecessary things
    install -d ${D}${webos_servicesdir}/${PN}
    cp -rv ${S}/* ${D}${webos_servicesdir}/${PN}
    rm -rf ${D}${webos_servicesdir}/${PN}/files
    
    # Install the ACG configuration
    install -d ${D}${webos_sysbus_servicedir}
    install -d ${D}${webos_sysbus_permissionsdir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -d ${D}${webos_sysbus_apipermissionsdir}
    install -d ${D}${webos_sysbus_groupsdir}
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.service ${D}${webos_sysbus_servicedir}/${BPN}.service
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.perm.json ${D}${webos_sysbus_permissionsdir}/${BPN}.perm.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.role.json ${D}${webos_sysbus_rolesdir}/${BPN}.role.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.api.json ${D}${webos_sysbus_apipermissionsdir}/${BPN}.api.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.groups.json ${D}${webos_sysbus_groupsdir}/${BPN}.groups.json
}

FILES:${PN} += "${webos_servicesdir}/${PN}"

inherit useradd

# Let the devmode recipe, add the developer users & group
USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = " \
    -u 504 -r -g developer --system -c developer -d /home/developer -s /bin/sh developer; \
    -u 2003 -g developer -d /var -s /usr/sbin/nologin debug; \
    -u 2004 -g developer -d /var -s /usr/sbin/nologin dev-func; \
"

GROUPADD_PARAM:${PN} = " \
    -g 504 -f developer; \
"

GROUPMEMS_PARAM:${PN} = " \
    -a developer -g developer; \
    -a dev-func -g developer; \ 
"
