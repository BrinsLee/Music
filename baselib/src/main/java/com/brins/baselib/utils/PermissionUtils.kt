package com.brins.baselib.utils

import android.app.Fragment
import android.content.Context
import android.net.Uri
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.option.Option
import java.io.File

/**
 * Created by lipeilin
 * on 2021/2/18
 */
fun with(fragment: Fragment): Option? {
    return AndPermission.with(fragment)
}

fun with(context: Context): Option? {
    return AndPermission.with(context)
}

fun hasAlwaysDeniedPermission(
    fragment: Fragment?,
    deniedPermissions: List<String?>?
): Boolean {
    return AndPermission.hasAlwaysDeniedPermission(fragment, deniedPermissions)
}

fun hasAlwaysDeniedPermission(
    context: Context?,
    deniedPermissions: List<String?>?
): Boolean {
    return AndPermission.hasAlwaysDeniedPermission(context, deniedPermissions)
}


fun hasAlwaysDeniedPermission(
    fragment: Fragment?,
    vararg deniedPermissions: String?
): Boolean {
    return AndPermission.hasAlwaysDeniedPermission(fragment, *deniedPermissions)
}

fun hasAlwaysDeniedPermission(
    context: Context?,
    vararg deniedPermissions: String?
): Boolean {
    return AndPermission.hasAlwaysDeniedPermission(context, *deniedPermissions)
}


fun hasPermissions(
    fragment: Fragment?,
    vararg permissions: String?
): Boolean {
    return AndPermission.hasPermissions(fragment, *permissions)
}

fun hasPermissions(
    context: Context?,
    vararg permissions: String?
): Boolean {
    return AndPermission.hasPermissions(context, *permissions)
}

fun hasPermissions(
    fragment: Fragment?,
    vararg permissions: Array<String?>?
): Boolean {
    return AndPermission.hasPermissions(fragment, *permissions)
}

fun hasPermissions(
    context: Context?,
    vararg permissions: Array<String?>?
): Boolean {
    return AndPermission.hasPermissions(context, *permissions)
}


fun getFileUri(fragment: Fragment?, file: File?): Uri? {
    return AndPermission.getFileUri(fragment, file)
}

fun getFileUri(context: Context?, file: File?): Uri? {
    return AndPermission.getFileUri(context, file)
}

fun requestPermission(
    context: Context,
    onGranted: (data: List<String>) -> Unit,
    onDenied: (data: List<String>) -> Unit,
    vararg permission: String
) {

    with(context)!!
        .runtime()
        .permission(permission)
        .onGranted {
            onGranted(it)
        }
        .onDenied {
            onDenied(it)
        }
        .start()
}
