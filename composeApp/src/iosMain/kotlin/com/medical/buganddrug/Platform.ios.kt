package com.medical.buganddrug

import platform.Photos.PHPhotoLibrary
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()
actual fun pickImages(
    onImagesPicked: (List<ByteArray>) -> Unit
) {
    val config = PHPickerConfiguration(PHPhotoLibrary.sharedPhotoLibrary())
    config.selectionLimit = 5
    config.filter = PHPickerFilter.imagesFilter()

    val picker = PHPickerViewController(config)

    val delegate = ImagePickerDelegate()
    picker.delegate = delegate

    pickerDelegate = delegate
    callbackRef = onImagesPicked

    val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController
    rootVC?.presentViewController(picker, animated = true, completion = null)
}