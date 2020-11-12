package com.brins.baselib.utils

import android.os.Environment
import android.util.Log
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

/**
 * Created by lipeilin
 * on 2020/11/12
 */
/**
 * Create a directory if it doesn't exist, otherwise do nothing.
 *
 * @param file The file.
 * @return `true`: exists or creates successfully<br></br>`false`: otherwise
 */
fun createOrExistsDir(file: File?): Boolean {
    return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
}

/**
 * Write file from string.
 *
 * @param file    The file.
 * @param content The string of content.
 * @param append  True to append, false otherwise.
 * @return `true`: success<br></br>`false`: fail
 */
fun writeFileFromString(
    file: File?,
    content: String?,
    append: Boolean
): Boolean {
    if (file == null || content == null) return false
    if (!createOrExistsFile(file)) {
        Log.e("FileIOUtils", "create file <$file> failed.")
        return false
    }
    var bw: BufferedWriter? = null
    return try {
        bw = BufferedWriter(FileWriter(file, append))
        bw.write(content)
        true
    } catch (e: IOException) {
        e.printStackTrace()
        false
    } finally {
        try {
            bw?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

/**
 * Create a file if it doesn't exist, otherwise do nothing.
 *
 * @param file The file.
 * @return `true`: exists or creates successfully<br></br>`false`: otherwise
 */
fun createOrExistsFile(file: File?): Boolean {
    if (file == null) return false
    if (file.exists()) return file.isFile
    return if (!createOrExistsDir(file.parentFile)) false else try {
        file.createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }
}

/**
 * Return whether sdcard is enabled by environment.
 *
 * @return `true`: enabled<br></br>`false`: disabled
 */
fun isSDCardEnableByEnvironment(): Boolean {
    return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
}