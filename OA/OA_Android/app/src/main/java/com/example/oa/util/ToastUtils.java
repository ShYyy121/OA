package com.example.oa.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oa.R;


public class ToastUtils {
    public static void showToast(Context context, String message) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_toast_layout, null);
        TextView textView = view.findViewById(R.id.toast_message);
        textView.setText(message);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }
}