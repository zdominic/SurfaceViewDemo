package com.edan.www.surfaceviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private MySurfaceView mMySurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMySurfaceView = (MySurfaceView) findViewById(R.id.MySurfaceView);
    }


    public void clearDate(View view){
        mMySurfaceView.reDraw();
    }
}
