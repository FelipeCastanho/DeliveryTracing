package com.example.yoyo.deliverytracing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class EmpleadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_empleado);

        TextView titulo = (TextView) findViewById(R.id.textSaludo);

        String nombreCompleto = getIntent().getExtras().getString("nombreUsuario");
        String[] nombres = nombreCompleto.split(" ");

        String palabra = "¡Hola ";
        char[] saludo = palabra.concat(nombres[0]+"!").toCharArray();
        titulo.setText(saludo,0,saludo.length);
    }

    public void buttonTapped(View view) {
        int numId = view.getId();
        String id = view.getResources().getResourceEntryName(numId);
        if(id.equals("imgPedidosDisponibles")){
            Intent newActivity = new Intent(EmpleadoActivity.this,PedidosDisponiblesActivity.class);
            String idEmpresa = getIntent().getExtras().getString("idEmpresa");
            newActivity.putExtra("idEmpresa", idEmpresa);
            String idUsuario = getIntent().getExtras().getString("idUsuario");
            newActivity.putExtra("idUsuario", idUsuario);
            startActivity(newActivity );
        } else if(id.equals("imgMisPedidos")){
            Intent newActivity = new Intent(EmpleadoActivity.this,MisPedidosActivity.class);
            String idEmpresa = getIntent().getExtras().getString("idEmpresa");
            String idUsuario = getIntent().getExtras().getString("idUsuario");
            newActivity.putExtra("idUsuario", idUsuario);
            startActivity(newActivity );
        } else if(id.equals("imgLogout")){
            SharedPreferences sharedPreference = getSharedPreferences("DeliveryTracing", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreference.edit();
            editor.putString("tipo", "No hay dato");
            editor.putString("nombreUsuario", "No hay dato");
            editor.putString("idEmpresa", "No hay dato");
            editor.putString("idUsuario", "No hay dato");
            editor.commit();
            Intent newActivity = new Intent(EmpleadoActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newActivity );
        }
    }
}
