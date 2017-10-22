package com.example.yoyo.deliverytracing;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

public class MisPedidosActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = null;
    String idEmpleado;
    MisPedidosActivity objeto = this;
    String[] codigos = null;
    int posicion = 0;
    Map<String, Object> map = null;
    private ListView list;
    private String[] lista = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mis_pedidos);
        idEmpleado = getIntent().getExtras().getString("idUsuario");
        list = (ListView)findViewById(R.id.listview2);
        myRef = database.getReference("pedidos");
        ValueEventListener pedidoListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                map = (Map<String, Object>) dataSnapshot.getValue();
                lista = actualizarLista(map, idEmpleado);
                ArrayAdapter<String> adaptador = new ArrayAdapter<String>(objeto, android.R.layout.simple_list_item_1, lista);
                list.setAdapter(adaptador);
            }
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRef.addValueEventListener(pedidoListener);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(objeto);
                dialogo1.setTitle("Confirmación de entrega");
                dialogo1.setMessage("¿Desea actualizar el pedido "+ codigos[position] +" como actualizado y finalizar el seguimiento?");
                dialogo1.setCancelable(false);
                posicion = position;
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogo1, int id) {
                        //myRef.child(codigos[posicion]).child("estado").setValue("Finalizado");
                        map.remove(codigos[posicion]);
                        myRef.setValue(map);
                    }
                });
                dialogo1.show();

            }
        });
    }

    private String[] actualizarLista(Map<String, Object> map, String idEmpleado) {
        String lista = "";
        String estado = "";
        String fila = "";
        String id = "";
        try{
            Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); //Se crea el iterador del map recibido en el listener (Contine todos los pedidos)
            Map.Entry<String, Object> entry= null; // Se crea la variable auxiliar entry que almacenara la llave y el valor existentes en el map (idPedido - Map con datos de pedido)
            String n = null;
            while (it.hasNext()) {
                entry= it.next();
                n = entry.getKey(); // Aqui se tiene la llave del pedido actualmente recorrido en el ciclo
                fila = n+"\t \t \t \t \t";
                Map<String, Object> mapPedido = (Map<String, Object>) entry.getValue(); // Se asigna el valor del pedido, en este caso un map con todos los datos
                Iterator<Map.Entry<String, Object>> itPedido = mapPedido.entrySet().iterator(); // Se crea un iterador para recorrer los datos dentro del pedido
                Map.Entry<String, Object> entryPedido = null;
                while(itPedido.hasNext()){
                    entryPedido = itPedido.next(); // Se crea la variable auxiliar entry que almacena la llave y el valor de un valor especifico ed un pedido
                    if(entryPedido.getKey().contains("direccion")){
                        fila += (String)entryPedido.getValue() + "\t \t \t \t \t";
                    }
                    if(entryPedido.getKey().equals("telefono")){
                        //fila += (String)entryPedido.getValue();
                    }
                    if(entryPedido.getKey().equals("estado")){
                        estado = (String) entryPedido.getValue();
                    }
                    if(entryPedido.getKey().contains("empleado")){
                        id = (String) entryPedido.getKey();
                    }
                }
                if(idEmpleado.equals(id) && estado.equals("activo")){
                    lista += fila +";";
                }
                fila = "";
            }
        }catch (Exception ex){
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            ex.getMessage(), Toast.LENGTH_SHORT);
            toast1.show();
        }
        String[] listaRespuesta = lista.split(";");
        codigos = new String[listaRespuesta.length];
        for(int i = 0; i < listaRespuesta.length; i++){
            codigos[i]= listaRespuesta[i].split("\t \t \t \t \t")[0];
        }
        return listaRespuesta;
    }
}
