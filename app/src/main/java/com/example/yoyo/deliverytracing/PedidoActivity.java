package com.example.yoyo.deliverytracing;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

public class PedidoActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRefPedidos;
    Map<String, Object> mapPedidos;
    String empresa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pedido);
    }

    @Override
    protected void onStart() {
        super.onStart();
        empresa = getIntent().getExtras().getString("idEmpresa");
        database = FirebaseDatabase.getInstance();
        myRefPedidos = database.getReference("pedidos");
        ValueEventListener pedidosListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                mapPedidos = (Map<String, Object>) dataSnapshot.getValue();
            }
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRefPedidos.addValueEventListener(pedidosListener);
    }

    public void buttonTapped(View view) {
        int numId = view.getId();
        String id = view.getResources().getResourceEntryName(numId);

        if(id.equals("botonRegistrar")) {
            EditText cliente = (EditText) findViewById(R.id.cliente);
            EditText direccion = (EditText) findViewById(R.id.direccion);
            EditText telefono = (EditText) findViewById(R.id.telefono);

            int codigo = ultimoCodigo() + 1;


            try{
                if(cliente.getText().toString().equals("") || direccion.getText().toString().equals("") || telefono.getText().toString().equals("")){
                   Toast t = Toast.makeText(getApplicationContext(), "Debe rellenar todos los campos", Toast.LENGTH_SHORT);
                    t.show();
                }else{

                    myRefPedidos.child("pd" + codigo).child("cliente").setValue(cliente.getText().toString());
                    myRefPedidos.child("pd" + codigo).child("direccion").setValue(direccion.getText().toString());
                    myRefPedidos.child("pd" + codigo).child("telefono").setValue(Integer.parseInt(telefono.getText().toString()));
                    myRefPedidos.child("pd" + codigo).child("estado").setValue("En proceso");
                    myRefPedidos.child("pd" + codigo).child(empresa+"").setValue(true);
                    AlertDialog.Builder mensaje = new AlertDialog.Builder(this);
                    mensaje.setTitle("Código").setMessage("El pedido ha sido registrado. El código es: pd"+codigo)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    finish();
                                }
                            })
                            .show();
                }
            }catch (Exception ex){
                Toast t = Toast.makeText(getApplicationContext(), "Error al registrar el pedido", Toast.LENGTH_SHORT);
                t.show();

            }
        }
    }

    public int ultimoCodigo(){
        int numeroPedido = 0;
        try{
            Iterator<Map.Entry<String, Object>> it = mapPedidos.entrySet().iterator();
            Map.Entry<String, Object> entry;

            while(it.hasNext()){
                int aux;
                entry = it.next();
                String ultimoPedido = entry.getKey();
                String[] numero = ultimoPedido.split("pd");
                aux = Integer.parseInt(numero[1]);
                if(aux > numeroPedido){
                    numeroPedido = aux;
                }
            }
        }catch (Exception ex){
            numeroPedido = 1;
        }
        return numeroPedido;
    }
}
