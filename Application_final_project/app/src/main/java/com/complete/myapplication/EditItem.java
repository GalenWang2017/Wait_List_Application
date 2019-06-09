package com.complete.myapplication;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class EditItem extends AppCompatActivity {
    int y,m,_d,h,mi;
    Bundle b;
    SQLiteDatabase mDB;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);
        ActionBar actionBar=this.getSupportActionBar();
        b=this.getIntent().getExtras();
        DBHelper dbHelper = new DBHelper(this);
        mDB=dbHelper.getWritableDatabase();


        final EditText etitle=(EditText)findViewById(R.id.edit_title);
        final EditText econtext=(EditText)findViewById(R.id.edit_context);
        Button edate=(Button)findViewById(R.id.edit_date_button);
        Button etime=(Button)findViewById(R.id.edit_time_button);
        final TextView edatetext=(TextView)findViewById(R.id.edit_timeoutdate);
        final TextView etimetext=(TextView)findViewById(R.id.edit_timeouttime);
        final Calendar calendar = Calendar.getInstance();
        final int year =calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int min = calendar.get(Calendar.MINUTE);

        etitle.setText(b.getString("Title"));
        econtext.setText(b.getString("Context"));
        String[] datesplit=b.getString("AlertTime").split(" ");
        edatetext.setText(datesplit[0]);
        etimetext.setText(datesplit[1]);

        edate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String d = String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(dayOfMonth);
                        edatetext.setText(d);
                        y=year;
                        m=month+1;
                        _d=dayOfMonth;

                    }
                },year,month,day).show();
            }
        });

        etime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String t = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                        etimetext.setText(t);
                        h=hourOfDay;
                        mi=minute;

                    }
                },hour,min,true).show();
            }
        });

        FloatingActionButton e_fab=(FloatingActionButton)findViewById(R.id.edit_fab);
        e_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql="UPDATE waitlist SET _TITLE=\""+etitle.getText().toString()+"\",_CONTENT=\""+econtext.getText().toString()+
                        "\",_TIMEOUTTIME=\""+edatetext.getText().toString()+" "+etimetext.getText().toString()+
                        "\" WHERE _CREATETIME=\""+b.getString("CreateTime")+"\"";
                mDB.execSQL(sql);

                Intent i=new Intent(EditItem.this,MainActivity.class);
                String t=edatetext.getText().toString()+" "+etimetext.getText().toString();

                Switch s=findViewById(R.id.switch2);
                if(s.isChecked()){
                    Calendar calendar1=Calendar.getInstance();
                    calendar1.set(y,m-1,_d,h,mi,0);
                    //String tt=String.valueOf(y)+"/"+String.valueOf(m)+"/"+String.valueOf(_d)+" "+String.valueOf(h)+":"+String.valueOf(mi);
                    Intent notifyintent=new Intent(getApplicationContext(),Notification_reciever.class);
                    notifyintent.putExtra("times",0);
                    notifyintent.putExtra("Title",etitle.getText().toString());
                    PendingIntent pendingIntent =PendingIntent.getBroadcast(getApplicationContext(),0,notifyintent,PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP,calendar1.getTimeInMillis(),pendingIntent);
                    Toast.makeText(EditItem.this,"Set Alerm",Toast.LENGTH_LONG).show();
                }
                startActivity(i);

            }
        });

    }

}
