package com.chenjiajuan.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends Activity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        intent=new Intent();
    }

    public void onNormal(View view){
        intent.setClass(this,MemoryTestActivity.class);
        startActivity(intent);

    }

    public void  onProcess(View view){
        intent.setClass(this,LoginServiceActivity.class);
        startActivity(intent);
    }
}
