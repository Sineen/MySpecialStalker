package com.example.sin_een.myspecialstalker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    
    public static final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;

    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_OUTGOING_CALL = Manifest.permission.PROCESS_OUTGOING_CALLS;
    public static final String PERMISSION_SENDING_SMS = Manifest.permission.SEND_SMS;
    private static final int MY_PERMISSIONS_REQUEST = 1 ;
    private static final String SHARED_PHONE = "Phone_Number" ;
    private static final String SHARED_MSG = "message";
    private static final String NOT_READY =  "Your info is Not ready!";
    private static final String READY =  "Your info is ready!";

    public static String localPhone;
    public static String localMessage;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    
    EditText phoneNumber;
    EditText message;
    TextView title;
    TextView phoneMissing;
    TextView messageMissing;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isPermissionGranted(PERMISSION_OUTGOING_CALL) || isPermissionGranted(PERMISSION_READ_PHONE_STATE) || isPermissionGranted(PERMISSION_SENDING_SMS) ) {
            setContentView(R.layout.activity_main);
            launch();
        }
        else{
          requestPermissions();

        }

    }

    public void requestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{PERMISSION_OUTGOING_CALL, PERMISSION_READ_PHONE_STATE, PERMISSION_SENDING_SMS},
                MY_PERMISSIONS_REQUEST);

    }
    private void launch() {
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        message = (EditText) findViewById(R.id.text);

        title = (TextView) findViewById(R.id.title);
        phoneMissing = (TextView) findViewById(R.id.please_insert_phone);
        messageMissing = (TextView) findViewById(R.id.please_insert_text);

        messageMissing.setVisibility(View.INVISIBLE);
        phoneMissing.setVisibility(View.INVISIBLE);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor= sharedPreferences.edit();

        localPhone = sharedPreferences.getString(SHARED_PHONE, "");
        localMessage = sharedPreferences.getString(SHARED_MSG, "");

        if(localMessage.equals("")) {
            message.setText(String.valueOf(R.string.i_m_going_to_call_this_number));
            localMessage = String.valueOf(R.string.i_m_going_to_call_this_number);
        } else {
            message.setText(localMessage);
        }
        phoneNumber.setText(localPhone);

        if( isReady()) {
            title.setText(READY);
        }else {
            title.setText(NOT_READY);

        }

        phoneNumber.addTextChangedListener(phoneWatcher());
        message.addTextChangedListener(messageWatcher());
    }

    static boolean isReady() {
        return !localPhone.equals("") && !localMessage.equals("");
    }

    private TextWatcher messageWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (message.getText().toString().equals("")) {
                    title.setText(NOT_READY);
                    messageMissing.setVisibility(View.VISIBLE);
                } else {
                    messageMissing.setVisibility(View.INVISIBLE);
                    if (isReady()) {
                        title.setText(READY);
                    }  else {
                        title.setText(NOT_READY);
                    }
                }
                localMessage = charSequence.toString();
                editor.putString(SHARED_MSG, localMessage);
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        };
    }

    private TextWatcher phoneWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    phoneMissing.setVisibility(View.INVISIBLE);
                    if( isReady()) {
                        title.setText(READY);
                    }else {
                        title.setText(NOT_READY);

                    }
                } else {
                    title.setText(NOT_READY);
                    phoneMissing.setVisibility(View.VISIBLE);
                }

                localPhone =charSequence.toString();
                editor.putString(SHARED_PHONE, localPhone);
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        };
    }

    private boolean isPermissionGranted(String permession) {
        return ContextCompat.checkSelfPermission(MainActivity.this, permession) == PERMISSION_GRANTED;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                            int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (verifyingPermissions(grantResults)) {
                setContentView(R.layout.activity_main);
                launch();
            }
            else
                requestPermissions();
        }
    }

    public boolean verifyingPermissions(int[] grantResults)
    {
        return grantResults.length == 3 && grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED
                && grantResults[2] == PERMISSION_GRANTED;
    }










}
