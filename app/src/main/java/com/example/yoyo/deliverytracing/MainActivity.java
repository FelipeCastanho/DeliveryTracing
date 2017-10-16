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
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = null;
    Map<String, Object> map = null;
    String estado = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");
    }

    public void buttonTapped(View view) {
        boolean change = true;
        String referencia = "";
        int numId = view.getId();
        String id = view.getResources().getResourceEntryName(numId);
        if(id.equals("rastrear")){
            EditText et1 = (EditText)findViewById(R.id.codigo);
            String codigo = et1.getText().toString();
            referencia = buscarPedido(codigo);
            if(!estado.equals("activo") && !referencia.equals("")){
                Toast toast1 =
                        Toast.makeText(getApplicationContext(),
                                "Su pedido todavía se encuentra en proceso", Toast.LENGTH_SHORT);
                toast1.show();
            }
           else if(!referencia.equals("")){
                Intent newActivity = new Intent(MainActivity.this,RastreoActivity.class);
                newActivity.putExtra("referencia", referencia);
                startActivity(newActivity );
            }
        }
        else if(id.equals("login")){
            Intent newActivity = new Intent(MainActivity.this,LoginActivity.class);
            //.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newActivity );
        }

    }

    protected void onStart() {
        super.onStart();
        myRef = database.getReference("pedidos/");
        ValueEventListener pedidoListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                map = (Map<String, Object>) dataSnapshot.getValue();
            }
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRef.addValueEventListener(pedidoListener);
    }

    private String buscarPedido(String codigo) {
        String respuesta = "";
        try{
            int contador = 0;
            Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
            Map.Entry<String, Object> entry= null;
            String n=null;
            Map<String, Object> mapPedidos = null;
            Map<String, Object> mapPedido = null;
            while (it.hasNext()) {
                contador++;
                entry= it.next();
                n = entry.getKey();
                mapPedidos = (Map<String, Object>) entry.getValue();
                Iterator<Map.Entry<String, Object>> itPedidos = map.entrySet().iterator();
                Map.Entry<String, Object> entryPedidos = itPedidos.next();
                if(entryPedidos.getKey().equals(codigo)){
                    mapPedido = (Map<String, Object>) entryPedidos.getValue();
                    Iterator<Map.Entry<String, Object>> itPedido = mapPedido.entrySet().iterator();
                    Map.Entry<String, Object> entryPedido = itPedido.next();
                    respuesta = entryPedido.getKey();
                    entryPedido = itPedido.next();
                   estado = (String)entryPedido.getValue();

                }
            }
        }catch (Exception ex){
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "No se ha encontrado el pedido", Toast.LENGTH_SHORT);
            toast1.show();
        }
        if(respuesta.equals("")){
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "No se ha encontrado el pedido", Toast.LENGTH_SHORT);
            toast1.show();
        }
        return respuesta;
    }

}
/* ValueEventListener pedidoListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                int contador = 0;
                String valor = "";
                try{
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
                    Map.Entry<String, Object> entry= null;
                    String n=null;
                    while (it.hasNext()) {
                        entry= it.next();
                        n = entry.getKey(); //clave
                        contador++;
                        if(contador == 1) url = "empleados/"+n;
                    }
                }catch (Exception ex){
                    change = false;
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "No se ha encontrado el pedido", Toast.LENGTH_SHORT);
                    toast1.show();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addValueEventListener(pedidoListener);*/