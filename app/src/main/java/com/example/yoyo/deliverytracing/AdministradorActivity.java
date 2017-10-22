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
            newActivity.putExtra("tipo", getIntent().getExtras().getString("tipo"));
            newActivity.putExtra("nombreUsuario", getIntent().getExtras().getString("nombreUsuario"));
            newActivity.putExtra("idEmpresa", getIntent().getExtras().getString("idEmpresa"));
            startActivity(newActivity);
        }else if(id.equals("imgGestion")){
            Intent newActivity = new Intent(AdministradorActivity.this, GestionActivity.class);
            newActivity.putExtra("idEmpresa", getIntent().getExtras().getString("idEmpresa"));
            startActivity(newActivity);
        }else if(id.equals("imgLogout")){
            SharedPreferences sharedPreference = getSharedPreferences("DeliveryTracing", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreference.edit();
            editor.putString("tipo", "No hay dato");
            editor.putString("nombreUsuario", "No hay dato");
            editor.putString("idEmpresa", "No hay dato");
            editor.putString("idUsuario", "No hay dato");
            editor.commit();
            Intent newActivity = new Intent(AdministradorActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newActivity );
        }
    }
}
