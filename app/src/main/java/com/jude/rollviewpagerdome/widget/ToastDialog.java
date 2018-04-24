package com.jude.rollviewpagerdome.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.rollviewpagerdome.R;
import com.jude.rollviewpagerdome.utils.SpManager;
import com.jude.rollviewpagerdome.utils.SystemUtil;


public class ToastDialog extends Dialog {

    private TextView mToastView;

    private String mToast;

    public ToastDialog(Context context) {
        super(context, R.style.base_dialog);
    }

    public ToastDialog(Context context, int themeResId) {
        super(context, R.style.base_dialog);
    }

    protected ToastDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, R.style.base_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toast_dialog);
        mToastView = (TextView) findViewById(R.id.tv_toast);
    }

    @Override
    public void show() {
        super.show();
        mToastView.setText(mToast);
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    protected void onStart() {
        super.onStart();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.CENTER;
        getWindow().setAttributes(layoutParams);
    }

    public void setToast(String toast) {
        mToast = toast;
    }

}
