package com.licheedev.keyboardview_copy

import android.os.Build
import android.widget.EditText
import java.lang.reflect.Method


/**
 * 禁止在获取焦点时显示软键盘
 * @receiver EditText
 */
fun EditText.disableShowSoftInputOnFocus() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.showSoftInputOnFocus = false;
    } else {
        val cls = EditText::class.java
        val method: Method
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", Boolean::class.javaPrimitiveType)
            method.isAccessible = true
            method.invoke(this, false)
        } catch (e: Exception) {
        }
    }

}