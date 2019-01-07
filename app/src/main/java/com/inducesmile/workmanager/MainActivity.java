package com.inducesmile.workmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.inducesmile.workmanager.helper.Constants;
import com.inducesmile.workmanager.helper.NotificationHandler;

import java.util.Calendar;
import java.util.UUID;

import androidx.work.Data;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText alertInputValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alertInputValue = (EditText)findViewById(R.id.alert_time);

        Button alertButton = (Button)findViewById(R.id.set_alert_button);
        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputValue = alertInputValue.getText().toString();
                if(inputValue.equals("")){
                    Toast.makeText(MainActivity.this, "Input value must not be empty", Toast.LENGTH_SHORT).show();
                }

                //Generate notification string tag
                String tag = generateKey();

                //Get time before alarm
                int minutesBeforeAlert = Integer.valueOf(inputValue);
                long alertTime = getAlertTime(minutesBeforeAlert) - System.currentTimeMillis();
                long current =  System.currentTimeMillis();

                Log.d(TAG, "Alert time - " + alertTime + "Current time " + current);

                int random = (int )(Math.random() * 50 + 1);

                //Data
                Data data = createWorkInputData(Constants.TITLE, Constants.TEXT, random);

                NotificationHandler.scheduleReminder(alertTime, data, tag);
            }
        });
    }


    private Data createWorkInputData(String title, String text, int id){
        return new Data.Builder()
                .putString(Constants.EXTRA_TITLE, title)
                .putString(Constants.EXTRA_TEXT, text)
                .putInt(Constants.EXTRA_ID, id)
                .build();
    }


    private long getAlertTime(int userInput){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, userInput);
        return cal.getTimeInMillis();
    }


    private String generateKey(){
        return UUID.randomUUID().toString();
    }

}
