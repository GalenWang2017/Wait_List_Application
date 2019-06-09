package com.complete.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class Question extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
        ActionBar actionBar=this.getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        final RadioButton c1=findViewById(R.id.c1);
        final RadioButton c2=findViewById(R.id.c2);
        final RadioButton c3=findViewById(R.id.c3);
        FloatingActionButton cfab=(FloatingActionButton)findViewById(R.id.confirm);
        cfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c1.isChecked()||c2.isChecked()||c3.isChecked()){
                Toast.makeText(Question.this,"Correct!",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Question.this,MainActivity.class);
                startActivity(intent);
                }else {
                    Toast.makeText(Question.this,"You need to choose 1 at least.",Toast.LENGTH_LONG).show();
                }
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
