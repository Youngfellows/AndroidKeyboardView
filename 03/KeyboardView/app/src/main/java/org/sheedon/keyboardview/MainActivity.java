package org.sheedon.keyboardview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import org.sheedon.keyboardlibrary.utils.DensityUtil;
import org.sheedon.keyboardlibrary.utils.ScreenUtil;
import org.sheedon.keyboardlibrary.widget.EditView;
import org.sheedon.keyboardlibrary.widget.SKeyboardView;

public class MainActivity extends AppCompatActivity {

    SKeyboardView keyboardView;
    EditView editView;
    LinearLayout llKeyboard;
    LinearLayout llGuan;

    private int height = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keyboardView = findViewById(R.id.keyboard_view);
        editView = findViewById(R.id.edit_view);
        llKeyboard = findViewById(R.id.ll_keyboard);
        llGuan = findViewById(R.id.ll_guan);
        setSubView();
        initEvent();
    }

    private void initEvent() {
        editView.setOnKeyboardListener(new EditView.OnKeyboardListener() {
            @Override
            public void onHide(boolean isCompleted) {
                if (height > 0) {
                    llGuan.scrollBy(0, -(height + DensityUtil.dp2px(MainActivity.this, 16)));
                }

                if (isCompleted) {
                    Log.i("", "你点击了完成按钮");
                }
            }

            @Override
            public void onShow() {
                llGuan.post(new Runnable() {
                    @Override
                    public void run() {
                        //pos[0]: X，pos[1]: Y
                        int[] pos = new int[2];
                        //获取编辑框在整个屏幕中的坐标
                        editView.getLocationOnScreen(pos);
                        //编辑框的Bottom坐标和键盘Top坐标的差
                        height = (pos[1] + editView.getHeight())
                                - (ScreenUtil.getScreenHeight(MainActivity.this) - keyboardView.getHeight());
                        if (height > 0) {
                            //编辑框和键盘之间预留出16dp的距离
                            llGuan.scrollBy(0, height + DensityUtil.dp2px(MainActivity.this, 16));
                        }
                    }
                });
            }

            @Override
            public void onPress(int primaryCode) {

            }
        });
    }

    private void setSubView() {
        editView.setEditView(llKeyboard, keyboardView, true);
    }
}