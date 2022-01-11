package com.jwhh.reminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int notificationId = 1;
    private EditText editText;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.set_button).setOnClickListener(this);
        findViewById(R.id.cancel_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.editText = findViewById(R.id.edit_text);
        this.timePicker = findViewById(R.id.time_picker);

        //Set notification Id and Text
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        intent.putExtra("notificationId", this.notificationId);
        intent.putExtra("todo", this.editText.getText().toString());

        PendingIntent alarmIntent = PendingIntent.getBroadcast(MainActivity.this, notificationId,
                intent, Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        int id = view.getId();
        if (id == R.id.set_button) {
            int hour = timePicker.getCurrentHour();
            int min = timePicker.getCurrentMinute();

            //Creating a time
            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, hour);
            startTime.set(Calendar.MINUTE, min);
            startTime.set(Calendar.SECOND, 0);
            long alarmStartTime = startTime.getTimeInMillis();

            //Now setting an alarm

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);
            }

            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
            notificationId++;
        } else if (id == R.id.cancel_button) {
            alarmManager.cancel(alarmIntent);
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    }
}