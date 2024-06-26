DESCRIPTION = "LuneOS image"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

IMAGE_FEATURES += "${LUNEOS_IMAGE_DEFAULT_FEATURES}"

inherit luneos_image

WKS_FILE ?= "luneos-image.wks"

# During build the pulse-access group is not available to wam, therefore add it here in the end of the image

inherit extrausers
EXTRA_USERS_PARAMS = " \
    usermod -a -G pulse-access wam; \
"
