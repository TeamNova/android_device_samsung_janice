# Include common makefile
$(call inherit-product, device/samsung/u8500-common/common.mk)

ifneq ($(TARGET_SCREEN_HEIGHT),800)
# Call cm.mk because somehow it's not being called!
$(call inherit-product, device/samsung/janice/cm.mk)
endif

LOCAL_PATH := device/samsung/janice

# Overlay
DEVICE_PACKAGE_OVERLAYS += $(LOCAL_PATH)/overlay
PRODUCT_AAPT_CONFIG := normal hdpi xhdpi
PRODUCT_AAPT_PREF_CONFIG := xhdpi

PRODUCT_PROPERTY_OVERRIDES += \
    dalvik.vm.dexopt-cache-only=0 \
    dalvik.vm.dexopt-data-only=1

# Graphics
PRODUCT_PROPERTY_OVERRIDES += \
    ro.zygote.disable_gl_preload=true \
    ro.bq.gpu_to_cpu_unsupported=1

# STE
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/ste_modem.sh:system/etc/ste_modem.sh

# Enable AAC 5.1 output
PRODUCT_PROPERTY_OVERRIDES += \
    media.aac_51_output_enabled=true

# Packages
PRODUCT_PACKAGES += \
    GalaxySAdvanceSettings \
    Stk \
    org.cyanogenmod.hardware \
    org.cyanogenmod.hardware.xml

# Gps
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/gps.conf:system/etc/gps.conf
    
# Compass workaround
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/compass:system/etc/init.d/compass

$(call inherit-product, device/common/gps/gps_eu_supl.mk)

# Init files
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/rootdir/fstab.samsungjanice:root/fstab.samsungjanice \
    $(LOCAL_PATH)/rootdir/init.samsungjanice.rc:root/init.samsungjanice.rc \
    $(LOCAL_PATH)/rootdir/init.samsungjanice.usb.rc:root/init.samsungjanice.usb.rc \
    $(LOCAL_PATH)/rootdir/init.recovery.samsungjanice.rc:root/init.recovery.samsungjanice.rc \
    $(LOCAL_PATH)/rootdir/ueventd.samsungjanice.rc:root/ueventd.samsungjanice.rc

# Permissions
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.sensor.gyroscope.xml:system/etc/permissions/android.hardware.sensor.gyroscope.xml \
    frameworks/native/data/etc/android.hardware.sensor.light.xml:system/etc/permissions/android.hardware.sensor.light.xml

# Audio
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/adm.sqlite-u8500:system/etc/adm.sqlite-u8500 \
    $(LOCAL_PATH)/configs/audio_policy.conf:system/etc/audio_policy.conf
    
# Update Me OTA xml
PRODUCT_COPY_FILES += \
vendor/cm/prebuilt/updateme/i9070/update_me.xml:system/update_me.xml

# Use non-open-source parts if present
$(call inherit-product-if-exists, vendor/samsung/u8500-common/janice/janice-vendor-blobs.mk)
