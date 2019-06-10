package com.complete.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class Check_Item_Info extends AppCompatActivity {
    Bundle b ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.waitlist_item_info);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.item_info_linearLayout);
        TextView content = (TextView)findViewById(R.id.item_content);
        TextView createtime = (TextView)findViewById(R.id.item_create_time);
        TextView alerttime=(TextView)findViewById(R.id.item_alert_time);

        b= this.getIntent().getExtras();

        content.setMovementMethod((ScrollingMovementMethod.getInstance()));
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Button button=(Button)findViewById(R.id.edit);



        if(b!=null){
            String t = b.getString("Title");
            String c = b.getString("Context");
            String ct = b.getString("CreateTime");
            String at = b.getString("AlertTime");
            setTitle(t);

            content.setText(c);
            createtime.setText(ct);
            alerttime.setText(at);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Check_Item_Info.this,EditItem.class);
                Bundle eb= new Bundle();
                eb.putString("Title",b.getString("Title"));
                eb.putString("Context",b.getString("Context"));
                eb.putString("AlertTime",b.getString("AlertTime"));
                eb.putString("CreateTime",b.getString("CreateTime"));
                i.putExtras(eb);
                startActivity(i);

            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

}
