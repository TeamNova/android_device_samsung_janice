# Specify phone tech before including full_phone
$(call inherit-product, vendor/mahdi/config/gsm.mk)

# Release name
PRODUCT_RELEASE_NAME := GT-I9070

# Inherit common product files.
$(call inherit-product, vendor/mahdi/configs/common.mk)
$(call inherit-product, vendor/mahdi/configs/common_full_phone.mk)

<<<<<<< HEAD:mahdi.mk
# Copy device specific prebuilt files.
PRODUCT_COPY_FILES += \
    vendor/mahdi/prebuilt/bootanimations/BOOTANIMATION-800x480.zip:system/media/bootanimation.zip

=======
# Inherit some common CM stuff.
$(call inherit-product, vendor/cm/config/common_full_phone.mk)
>>>>>>> bb2f6d5f4059de30d836e19efe71d3bad21b20e2:cm.mk

# Inherit device configuration
$(call inherit-product, device/samsung/janice/full_janice.mk)

# CM Packages
PRODUCT_PACKAGES += \
    Stk \
    org.cyanogenmod.hardware \
    org.cyanogenmod.hardware.xml

# Device identifier. This must come after all inclusions
PRODUCT_DEVICE := janice
PRODUCT_NAME := mahdi_janice
PRODUCT_BRAND := samsung
PRODUCT_MODEL := GT-I9070
PRODUCT_MANUFACTURER := samsung

# Set build fingerprint / ID / Product Name ect.
PRODUCT_BUILD_PROP_OVERRIDES += PRODUCT_NAME=GT-I9070 TARGET_DEVICE=janice
