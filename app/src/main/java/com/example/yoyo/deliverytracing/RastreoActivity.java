package com.example.yoyo.deliverytracing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class RastreoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rastreo);
    }
}
