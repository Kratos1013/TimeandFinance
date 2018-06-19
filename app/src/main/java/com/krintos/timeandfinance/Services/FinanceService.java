package com.krintos.timeandfinance.Services;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.AlertDialogLayout;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 6/16/18.
 */

public class FinanceService extends Service{
    public SmsReceiver smsReceiver = new SmsReceiver();
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Intent restart = new Intent(getApplicationContext(), this.getClass());
        restart.setPackage(getPackageName());
        startService(restart);
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private class SmsReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String phonenumber = "", message = "";

            Bundle extras = intent.getExtras();

            if (extras != null) {
                Object[] pdus = (Object[]) extras.get("pdus");
                if (pdus != null) {

                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = getIncomingMessage(pdu, extras);
                        phonenumber = smsMessage.getDisplayOriginatingAddress();
                        message += smsMessage.getDisplayMessageBody();
                    }

                // Display the message
                    if (phonenumber.equals("900")){
                        callpopup(phonenumber, message);

                      Toast.makeText(context, "Number "+phonenumber+"\n Message "+message, Toast.LENGTH_LONG).show();

                    }
                }
            }
        }
        private SmsMessage getIncomingMessage(Object object, Bundle bundle) {
            SmsMessage smsMessage;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String format = bundle.getString("format");
                smsMessage = SmsMessage.createFromPdu((byte[]) object, format);
            } else {
                smsMessage = SmsMessage.createFromPdu((byte[]) object);
            }

            return smsMessage;
        }
        }

    private void callpopup(String  phonenumber, String Message) {
        Intent dialogIntent = new Intent(this, FinancePopup.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        dialogIntent.putExtra("PHONE_NUMBER", phonenumber);
        dialogIntent.putExtra("MESSAGE_FROM_BANK", Message);
        startActivity(dialogIntent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restart = new Intent(getApplicationContext(), this.getClass());
        restart.setPackage(getPackageName());
        startService(restart);
        super.onTaskRemoved(rootIntent);
    }
}
