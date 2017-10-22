package com.example.yoyo.deliverytracing;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
        SharedPreferences sharedPreference = getSharedPreferences("DeliveryTracing",Context.MODE_PRIVATE);
        String tipo = sharedPreference.getString("tipo","No hay dato");
        String nombreUsuario = sharedPreference.getString("nombreUsuario","No hay dato");
        String idEmpresa = sharedPreference.getString("idEmpresa","No hay dato");
        String idUsuario = sharedPreference.getString("idUsuario","No hay dato");
        if(!tipo.equals("No hay dato")){
            if(tipo.equals("administrador")){
                Intent newActivity = new Intent(MainActivity.this, AdministradorActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                newActivity.putExtra("tipo", tipo);
                newActivity.putExtra("nombreUsuario", nombreUsuario);
                newActivity.putExtra("idEmpresa", idEmpresa);
                newActivity.putExtra("idUsuario", idUsuario);
                startActivity(newActivity );
            }else if(tipo.equals("empleado")){
                Intent newActivity = new Intent(MainActivity.this, EmpleadoActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                newActivity.putExtra("tipo", tipo);
                newActivity.putExtra("nombreUsuario", nombreUsuario);
                newActivity.putExtra("idEmpresa", idEmpresa);
                newActivity.putExtra("idUsuario", idUsuario);
                startActivity(newActivity );
            }
        }

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
            if(!estado.equals("activo") && !estado.equals("")){
                Toast toast1 =
                        Toast.makeText(getApplicationContext(),
                                "Su pedido todavía se encuentra en proceso", Toast.LENGTH_SHORT);
                toast1.show();
            }
           else if (!referencia.equals("")){
                Intent newActivity = new Intent(MainActivity.this,RastreoActivity.class);
                newActivity.putExtra("referencia", referencia);
                newActivity.putExtra("codigo", codigo);
                startActivity(newActivity );
            }
            estado = "";
        }
        else if(id.equals("login")){
            Intent newActivity = new Intent(MainActivity.this,LoginActivity.class);
            //.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newActivity );
        }

    }

    protected void onStart() {
        super.onStart();
        myRef = database.getReference("pedidos");
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
            Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); //Se crea el iterador del map recibido en el listener (Contine todos los pedidos)
            Map.Entry<String, Object> entry= null; // Se crea la variable auxiliar entry que almacenara la llave y el valor existentes en el map (idPedido - Map con datos de pedido)
            String n=null;
            while (it.hasNext()) {
                entry= it.next();
                n = entry.getKey(); // Aqui se tiene la llave del pedido actualmente recorrido en el ciclo
                if(n.equals(codigo)){
                    Map<String, Object> mapPedido = (Map<String, Object>) entry.getValue(); // Se asigna el valor del pedido, en este caso un map con todos los datos
                    Iterator<Map.Entry<String, Object>> itPedido = mapPedido.entrySet().iterator(); // Se crea un iterador para recorrer los datos dentro del pedido
                    Map.Entry<String, Object> entryPedido = null;
                    while(itPedido.hasNext()){
                        entryPedido = itPedido.next(); // Se crea la variable auxiliar entry que almacena la llave y el valor de un valor especifico ed un pedido
                        if(entryPedido.getKey().contains("empleado")){
                            respuesta = entryPedido.getKey();
                        }
                        if(entryPedido.getKey().equals("estado")){
                            estado = (String)entryPedido.getValue();
                        }
                    }
                }
            }
        }catch (Exception ex){
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "No se ha encontrado el pedido", Toast.LENGTH_SHORT);
            toast1.show();
        }
        if(respuesta.equals("") && estado.equals("")){
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "No se ha encontrado el pedido", Toast.LENGTH_SHORT);
            toast1.show();
        }
        return respuesta;
    }

}