package com.tongjo.girlswish.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.tongjo.girlswish.R;

public class CustomeProgressDialog extends Dialog {  
    public CustomeProgressDialog(Context context, String strMessage) {  
        this(context, R.style.customer_progress_dialog, strMessage);  
    }  
  
    public CustomeProgressDialog(Context context, int theme, String strMessage) {  
        super(context, theme);  
        this.setContentView(R.layout.dialog_progress);  
        this.getWindow().getAttributes().gravity = Gravity.CENTER;  
        TextView tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);  
        if (tvMsg != null) {  
            tvMsg.setText(strMessage);  
        }  
    }  
  
    @Override  
    public void onWindowFocusChanged(boolean hasFocus) {  
        if (!hasFocus) {  
            dismiss();  
        }  
    }  
} 
