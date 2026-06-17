package com.medical.buganddrug

import platform.UIKit.*
import platform.PhotosUI.*
import kotlinx.cinterop.*
import platform.Foundation.*
import platform.darwin.*

var pickerDelegate: PHPickerViewControllerDelegateProtocol? = null
var callbackRef: ((List<ByteArray>) -> Unit)? = null

class ImagePickerDelegate : NSObject(), PHPickerViewControllerDelegateProtocol {

    override fun picker(
        picker: PHPickerViewController,
        didFinishPicking: List<*>
    ) {
        picker.dismissViewControllerAnimated(true, completion = null)

        val results = didFinishPicking as List<PHPickerResult>

        val group = dispatch_group_create() ?: return // safety
        val output = mutableListOf<ByteArray>()

        results.forEach { result ->
            val itemProvider = result.itemProvider

            if (itemProvider.hasItemConformingToTypeIdentifier("public.image")) {
                dispatch_group_enter(group)

                itemProvider.loadDataRepresentationForTypeIdentifier("public.image") { data, error ->
                    if (data != null) {
                        val byteArray = data.toByteArray() // or use NSData.toByteArray() extension if needed
                        output.add(byteArray)
                    } else {
                        // Optionally log error
                    }
                    dispatch_group_leave(group)
                }
            }
        }

        dispatch_group_notify(group, dispatch_get_main_queue()) {
            callbackRef?.invoke(output)
            // Optional: release the group if you want (though usually not necessary)
        }
    }
}
// Extension function to bridge NSData to Kotlin's ByteArray
@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    val size = this.length.toInt()
    if (size == 0) return ByteArray(0)

    val byteArray = ByteArray(size)
    byteArray.usePinned { pinned ->
        this.getBytes(pinned.addressOf(0), this.length)
    }
    return byteArray
}