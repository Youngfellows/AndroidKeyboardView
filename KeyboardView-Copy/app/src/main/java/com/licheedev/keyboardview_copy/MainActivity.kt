package com.licheedev.keyboardview_copy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // 禁止显示系统输入法
        editText.disableShowSoftInputOnFocus()
        editText2.disableShowSoftInputOnFocus()

        editText.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    keyboardView.bindEditText(editText)
                }
            }
        })

        editText2.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    keyboardView.bindEditText(editText2)
                }
            }
        })

        editText.requestFocus()

    }
}