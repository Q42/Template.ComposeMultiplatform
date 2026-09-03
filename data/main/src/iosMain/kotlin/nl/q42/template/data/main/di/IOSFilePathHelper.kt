package nl.q42.template.data.main.di

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import nl.q42.template.core.utils.logging.AppLogger
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSURL
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSURLIsExcludedFromBackupKey
import platform.Foundation.NSUserDomainMask

object IOSFilePathHelper {

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    fun createPath(
        directoryType: ULong,
        fileName: String,
        excludeFromBackup: Boolean,
        failureDirectoryLabel: String,
    ): Path = memScoped {
        val errorPtr = alloc<ObjCObjectVar<NSError?>>()
        val directoryUrl = NSFileManager.defaultManager.URLForDirectory(
            directory = directoryType,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = errorPtr.ptr,
        ) ?: throw IllegalStateException(
            "Failed to get $failureDirectoryLabel directory. Error: ${errorPtr.value?.localizedDescription}"
        )

        val path = directoryUrl.path ?: throw IllegalStateException(
            "Failed to get path for $failureDirectoryLabel directory."
        )

        if (excludeFromBackup) {
            val fileUrl = NSURL.fileURLWithPath("$path/$fileName")
            val excludeFromBackupErrorPtr = alloc<ObjCObjectVar<NSError?>>()
            val didSetExcludeFromBackup = fileUrl.setResourceValue(
                value = true,
                forKey = NSURLIsExcludedFromBackupKey,
                error = excludeFromBackupErrorPtr.ptr,
            )

            if (!didSetExcludeFromBackup) {
                AppLogger.error(
                    "Failed to set $failureDirectoryLabel file to be excluded from backup. " +
                        "Error: ${excludeFromBackupErrorPtr.value?.localizedDescription}"
                )
            }
        }

        "$path/$fileName".toPath()
    }
}