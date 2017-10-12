package com.example.yoyo.deliverytracing;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");
    }

    public void buttonTapped(View view) {

        int numId = view.getId();
        String id = view.getResources().getResourceEntryName(numId);
        if(id.equals("rastrear")){
            Intent newActivity = new Intent(MainActivity.this,RastreoActivity.class);
                    //.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//Con esta linea se mueren las actividades (No la uso aqui porque no es necesario, solo sera necesario despues de iniciar sesion)
            startActivity(newActivity );
        }
        else if(id.equals("login")){
        }

    }
}
