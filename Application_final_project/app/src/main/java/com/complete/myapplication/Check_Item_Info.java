package com.complete.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class Check_Item_Info extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.waitlist_item_info);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.item_info_linearLayout);
        TextView title = (TextView)findViewById(R.id.item_title);
        TextView content = (TextView)findViewById(R.id.item_content);
        TextView createtime = (TextView)findViewById(R.id.item_create_time);
        TextView alerttime=(TextView)findViewById(R.id.item_alert_time);


        content.setMovementMethod((ScrollingMovementMethod.getInstance()));
        Toolbar toolbar1 = findViewById(R.id.item_info_toolbar);
        setSupportActionBar(toolbar1);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Check_Item_Info.this,MainActivity.class);
                startActivity(intent);
            }
        });

        Bundle b = this.getIntent().getExtras();
        if(b!=null){
            String t = b.getString("Title");
            String c = b.getString("Context");
            String ct = b.getString("CreateTime");
            String at = b.getString("AlertTime");
            title.setText(t);
            content.setText(c);
            createtime.setText(ct);
            alerttime.setText(at);
        }


    }

}
