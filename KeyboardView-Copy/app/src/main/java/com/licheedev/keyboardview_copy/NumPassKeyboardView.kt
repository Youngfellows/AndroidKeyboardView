package com.licheedev.keyboardview_copy


import android.content.Context
import android.graphics.Color
import android.inputmethodservice.Keyboard
import android.text.Selection
import android.util.AttributeSet
import android.widget.EditText
import com.licheedev.keyboardview.BaseOnKeyboardActionListener
import com.licheedev.keyboardview.KeyboardView
import com.licheedev.keyboardview.OnKeyboardActionListener
import com.licheedev.myutils.LogPlus
import java.lang.ref.WeakReference
import java.util.*

class NumPassKeyboardView : KeyboardView {

    companion object {
        const val KEYCODE_DELETE_ALL = -233
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    val value: OnKeyboardActionListener = object : BaseOnKeyboardActionListener() {


        override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
            //LogPlus.e("onKey(primaryCode: $primaryCode, keyCodes: ${Arrays.toString(keyCodes)})")

            val editText = mEditText ?: return
            val editable = editText.editableText ?: return

            val start: Int = editText.selectionStart
            val end: Int = editText.selectionEnd

            //LogPlus.e("start=$start,end=$end")

            if (primaryCode == KEYCODE_DELETE_ALL) { // 清除按钮
                editable.clear()
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
                if (start == end) {
                    if (start > 0) {
                        editable.delete(start - 1, start)
                    }
                } else {
                    editable.delete(start, end)
                }
            } else {
                editable.replace(start, end, primaryCode.toChar().toString())
            }
        }

        override fun onText(text: CharSequence) {
            //LogPlus.e("onText(text: $text)")

            val editText = mEditText ?: return
            val editable = editText.editableText ?: return

            val start: Int = editText.selectionStart
            val end: Int = editText.selectionEnd

            editable.replace(start, end, text)
            Selection.setSelection(editable, start + text.length)
        }
    }


    init {
        val keyboard = Keyboard(context, R.xml.keyboard_num_password)
        setKeyboard(keyboard)
        isPreviewEnabled = false
        if (isInEditMode) {
            setKeyBackground(R.drawable.selector_keyboard_key)
            setKeyTextColor(Color.BLACK)
            setKeyTextSizeRes(R.dimen.normal_60dp)
            setLabelTextSizeRes(R.dimen.normal_48dp)
        }
        onKeyboardActionListener = value
    }

    private var mEditTextRef: WeakReference<EditText>? = null

    private val mEditText: EditText? get() = mEditTextRef?.get()

    fun bindEditText(editText: EditText) {
        mEditTextRef = WeakReference(editText)
    }

}