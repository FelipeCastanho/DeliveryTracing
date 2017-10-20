package com.example.yoyo.deliverytracing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class AdministradorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_administrador);
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

        if(id.equals("imgPedido")) {
            Intent newActivity = new Intent(AdministradorActivity.this, PedidoActivity.class);
            startActivity(newActivity);
        }
    }
}
