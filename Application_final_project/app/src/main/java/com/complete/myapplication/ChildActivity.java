package com.complete.myapplication;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ChildActivity extends AppCompatActivity {
    SQLiteDatabase mDB;
    int y,m,_d,h,mi;
    int times=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ChildActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        final Switch s=findViewById(R.id.switch1);
        //s.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) this);

        Button setdatebutton = (Button)findViewById(R.id.set_date_button);
        Button settimebutton =(Button)findViewById(R.id.set_time_button);
        final TextView det = (TextView) findViewById(R.id.timeoutdate);
        final TextView tet = (TextView)findViewById(R.id.timeouttime);


        final Calendar calendar = Calendar.getInstance();
        final int year =calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int min = calendar.get(Calendar.MINUTE);
        setdatebutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String d = String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(dayOfMonth);
                        det.setText(d);
                        y=year;
                        m=month+1;
                        _d=dayOfMonth;

                    }
                },year,month,day).show();
            }
        });

        settimebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String t = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                        tet.setText(t);
                        h=hourOfDay;
                        mi=minute;

                    }
                },hour,min,true).show();
            }
        });
        DBHelper dbHelper = new DBHelper(this);
        mDB=dbHelper.getWritableDatabase();

        FloatingActionButton floatingActionButton = findViewById(R.id.child_fab);
        final EditText editText1 = (EditText)findViewById(R.id.editText1);
        final EditText editText2 = (EditText)findViewById(R.id.editText2);




        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText1.getText().toString().equals("")){
                    Toast.makeText(ChildActivity.this,"You need to enter title at least.",Toast.LENGTH_LONG).show();
                }else {
                    String sql_insert_data = "INSERT INTO waitlist (_TITLE,_CONTENT,_CREATETIME,_TIMEOUTTIME) " +
                            "VALUES ("+"\""+editText1.getText().toString()+"\",\""+editText2.getText().toString()+"\"," +
                            "datetime(\'now\',\'localtime\'),\""+det.getText().toString()+" "+tet.getText().toString()+"\")";
                    mDB.execSQL(sql_insert_data);

                    //is set up the notification
                    if(s.isChecked()){
                        Calendar calendar1=Calendar.getInstance();
                        calendar1.set(y,m-1,_d,h,mi,0);
                        String tt=String.valueOf(y)+"/"+String.valueOf(m)+"/"+String.valueOf(_d)+" "+String.valueOf(h)+":"+String.valueOf(mi);
                        Intent notifyintent=new Intent(getApplicationContext(),Notification_reciever.class);
                        notifyintent.putExtra("times",times);
                        notifyintent.putExtra("Title",editText1.getText().toString());
                        PendingIntent pendingIntent =PendingIntent.getBroadcast(getApplicationContext(),times,notifyintent,PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar1.getTimeInMillis(),pendingIntent);
                        Toast.makeText(ChildActivity.this,tt,Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(ChildActivity.this,"Did not set alarm.",Toast.LENGTH_LONG).show();
                    }

                    Intent intent = new Intent();
                    intent.setClass(ChildActivity.this,MainActivity.class);
                    startActivity(intent);
                    times+=1;
                }


            }
        });
    }

}
