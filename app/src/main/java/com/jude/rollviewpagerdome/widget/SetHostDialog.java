package com.jude.rollviewpagerdome.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.rollviewpagerdome.R;
import com.jude.rollviewpagerdome.module.main.view.HomeActivity;
import com.jude.rollviewpagerdome.utils.SpManager;
import com.jude.rollviewpagerdome.utils.SystemUtil;


public class SetHostDialog extends Dialog implements View.OnClickListener {
    private TextView mHostTextView;
    private EditText mHostEditView;
    private Button mPositiveTextView;
    private Button mNegativeView;

    public SetHostDialog(Context context) {
        super(context, R.style.base_dialog);
    }

    public SetHostDialog(Context context, int themeResId) {
        super(context, R.style.base_dialog);
    }

    protected SetHostDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, R.style.base_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_host_dialog);
        mHostTextView = (TextView) findViewById(R.id.tv_host);
        mHostEditView = (EditText) findViewById(R.id.edt_host);
        mPositiveTextView = (Button) findViewById(R.id.tv_positive);
        mNegativeView = (Button) findViewById(R.id.tv_negative);
        mHostTextView.setText("host="+SpManager.getInstance(getContext()).getHostAddress());
        mHostEditView.requestFocus();
        ((TextView) findViewById(R.id.tv_android_id)).setText("androidID=" + SystemUtil.getAndroidId(getContext()));

        mNegativeView.setOnClickListener(this);
        mPositiveTextView.setOnClickListener(this);
        findViewById(R.id.tv_reset).setOnClickListener(this);
    }

    @Override
    public void show() {
        super.show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_positive:
                String hostAddress = mHostEditView.getText().toString().trim();
                if (!TextUtils.isEmpty(hostAddress)) {
                    SpManager.getInstance(getContext()).setHostAddress("http://" + hostAddress + "/zhili_slider/");
                    Toast.makeText(getContext(), "当前Host为:" + SpManager.getInstance(getContext()).getHostAddress(), Toast.LENGTH_LONG).show();
                    mHostTextView.setText("host="+SpManager.getInstance(getContext()).getHostAddress());
                    mHostEditView.setText("");
                }
                dismiss();
                break;
            case R.id.tv_negative:
                dismiss();
                break;
            case R.id.tv_reset:
                SpManager.getInstance(getContext()).setHostAddress(getContext().getString(R.string.base_url));
                Toast.makeText(getContext(), "当前Host为:" + SpManager.getInstance(getContext()).getHostAddress(), Toast.LENGTH_LONG).show();
                mHostTextView.setText("host="+SpManager.getInstance(getContext()).getHostAddress());
                mHostEditView.setText("");
                break;
        }
    }
}
