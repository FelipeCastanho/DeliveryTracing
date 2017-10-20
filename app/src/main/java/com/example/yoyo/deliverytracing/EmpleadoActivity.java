package com.example.yoyo.deliverytracing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

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
            startActivity(newActivity );
        } else if(id.equals("imgMisPedidos")){
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "Mis pedidos", Toast.LENGTH_SHORT);
            toast1.show();
        } else if(id.equals("imgLogout")){
            Intent newActivity = new Intent(EmpleadoActivity.this,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newActivity );
        }
    }
}
