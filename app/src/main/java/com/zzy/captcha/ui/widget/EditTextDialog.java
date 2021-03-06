package com.zzy.captcha.ui.widget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zzy.captcha.R;
import com.zzy.captcha.service.NotificationClickReceiver;
import com.zzy.captcha.utils.Utils;
import com.zzy.captcha.utils.SharedPreferencesUtils;
import com.zzy.captcha.utils.XposedPreferencesUtils;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by zzyandzzy on 2017/1/10.
 */

public class EditTextDialog extends MaterialDialog {
    private Context context;
    private View view;
    private EditText message;
    private XposedPreferencesUtils xposedPreferencesUtils;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private NotificationManager notificationManager;

    public EditTextDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EditTextDialog(Context context,String title,String defineMessage,String ok,String close,String lis){
        super(context);
        this.context = context;
        init();
        this.setTitle(title);
        initShowDialog(defineMessage,ok,close,lis);
    }

    private void initShowDialog(String defineMessage, String ok,String close, final String lis) {
        message.setText(defineMessage);
        this.setPositiveButton(ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (lis){
                    case "smstest":
                        if (!isMessageNull(message.getText().toString())){
                            addNotification(context,context.getString(R.string.smsTest),message.getText().toString());
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                                xposedPreferencesUtils.putString("smstest",message.getText().toString());
                            else
                                sharedPreferencesUtils.putString("smstest",message.getText().toString());
                            dismiss();
                        }
                        break;
                    case "smsregex":
                        if (!isMessageNull(message.getText().toString())){
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                                xposedPreferencesUtils.putString("smsRegex",message.getText().toString());
                            else
                                sharedPreferencesUtils.putString("smsRegex",message.getText().toString());
                            dismiss();
                        }
                        break;
                    case "keyword":
                        if (!isMessageNull(message.getText().toString())){
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                                xposedPreferencesUtils.putString("keyword",message.getText().toString());
                            else
                                sharedPreferencesUtils.putString("keyword",message.getText().toString());
                            dismiss();
                        }
                        break;
                    case "tigger":
                        if (!isMessageNull(message.getText().toString())){
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                                xposedPreferencesUtils.putString("tigger",message.getText().toString());
                            else
                                sharedPreferencesUtils.putString("tigger",message.getText().toString());
                            dismiss();
                        }
                        break;
                    case "copytext":
                        if (!isMessageNull(message.getText().toString())){
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                                xposedPreferencesUtils.putString("copytext",message.getText().toString());
                            else
                                sharedPreferencesUtils.putString("copytext",message.getText().toString());
                            dismiss();
                        }
                        break;
                    case "systemuitext":
                        if (!isMessageNull(message.getText().toString())){
                            xposedPreferencesUtils.putString("systemuitext",message.getText().toString());
                            dismiss();
                        }
                        break;
                    default:
                        dismiss();
                }
            }
        });
        if (close != null){
            this.setNegativeButton(close, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (lis){
                        case "smsregex":
                            message.setText(Utils.SmsRegex);
                            break;
                        case "keyword":
                            message.setText(Utils.Keyword);
                            break;
                        case "tigger":
                            message.setText(Utils.TiggerRegex);
                            break;
                        case "copytext":
                            message.setText(Utils.CpoyText);
                            break;
                        default:
                            dismiss();
                    }
                }
            });
        }
        this.show();
    }

    private boolean isMessageNull(String message){
        if (message.length() != 0 && message != null){
            return false;
        }
        else{
            Toast.makeText(context, R.string.errorEditString,Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    private void init() {
        xposedPreferencesUtils = new XposedPreferencesUtils(context);
        sharedPreferencesUtils = new SharedPreferencesUtils(context);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_edit,null);
        this.setContentView(view);
        message = (EditText) view.findViewById(R.id.dialog_edit_message);
    }

    private void addNotification(Context context, String title, String message) {
        //获取通知管理器服务
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = createDisplayMessageIntent(context, message, Utils.getNotificationId());
        //新建一个notification
        Notification.Builder builder = new Notification.Builder(context)
                .setTicker(message)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);
        builder.setFullScreenIntent(pendingIntent, true);
        //开始通知
        notificationManager.notify(Utils.getNotificationId(), builder.getNotification());
    }

    private PendingIntent createDisplayMessageIntent(Context context,String message,int notificationId) {
        Intent intent = new Intent(context, NotificationClickReceiver.class);
        PendingIntent pendingIntent= PendingIntent.getBroadcast(context, 0, intent, notificationId);
        return pendingIntent;
    }
}
