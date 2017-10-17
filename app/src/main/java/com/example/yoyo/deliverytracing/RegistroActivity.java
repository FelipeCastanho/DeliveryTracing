package com.example.yoyo.deliverytracing;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Switch;

public class RegistroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_registro);

        Switch isAdmin = (Switch) findViewById(R.id.adminSwitch);
        isAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextInputLayout empresa = (TextInputLayout) findViewById(R.id.textInputLayoutEmpresa);
                if(isChecked){
                    empresa.setVisibility(View.VISIBLE);
                }else{
                    empresa.setVisibility(View.GONE);
                }
            }
        });
    }


}
