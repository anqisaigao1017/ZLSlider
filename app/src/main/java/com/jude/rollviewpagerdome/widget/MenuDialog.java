package com.jude.rollviewpagerdome.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Button;
import android.widget.Toast;

import com.jude.rollviewpagerdome.R;
import com.jude.rollviewpagerdome.utils.SpManager;


public class MenuDialog extends Dialog implements View.OnClickListener {
    private Button btn_refresh;
    private Button btn_clear_cache;
    private Button btn_exit_app;
    private Button btn_set_host;
    private OnMenuClickListener mListener;

    public MenuDialog(Context context) {
        super(context, R.style.base_dialog);
    }

    public MenuDialog(Context context, int themeResId) {
        super(context, R.style.base_dialog);
    }

    protected MenuDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, R.style.base_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_dialog);
        btn_refresh = (Button) findViewById(R.id.btn_refresh);
        btn_clear_cache = (Button) findViewById(R.id.btn_clear_cache);
        btn_exit_app = (Button) findViewById(R.id.btn_exit);
        btn_set_host = (Button) findViewById(R.id.btn_set_host);
        btn_refresh.setOnClickListener(this);
        btn_clear_cache.setOnClickListener(this);
        btn_exit_app.setOnClickListener(this);
        btn_set_host.setOnClickListener(this);
    }

    @Override
    public void show() {
        super.show();
        btn_refresh.requestFocus();
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
            case R.id.btn_refresh:
                if(mListener!=null)
                    mListener.refreshClicked();
                dismiss();
                break;
            case R.id.btn_clear_cache:
                if(mListener!=null)
                    mListener.clearCacheClicked();
                dismiss();
                break;
            case R.id.btn_exit:
                if(mListener!=null)
                    mListener.exitAppClicked();
                dismiss();
                break;
            case R.id.btn_set_host:
                if(mListener!=null)
                    mListener.setHostClicked();
                dismiss();
                break;
        }
    }

    public void setListener(OnMenuClickListener listener){
        mListener = listener;
    }

    public interface OnMenuClickListener{
        void refreshClicked();
        void clearCacheClicked();
        void exitAppClicked();
        void setHostClicked();
    }
}
