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

public class PedidosDisponiblesActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = null;
    String idEmpresa;
    PedidosDisponiblesActivity objeto = this;
    String[] codigos = null;
    int posicion = 0;

    private ListView list;
    private String[] lista = {"A","B", "C", "D", "E"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pedidos_disponibles);
        idEmpresa = getIntent().getExtras().getString("idEmpresa");
        list = (ListView)findViewById(R.id.listview);
    }

    protected void onStart() {
        super.onStart();
        myRef = database.getReference("pedidos");
        ValueEventListener pedidoListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                lista = actualizarLista(map, idEmpresa);
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
                dialogo1.setTitle("Confirmación de pedido");
                dialogo1.setMessage("¿Desea agregar el pedido "+ codigos[position] +" a su lista de pedidos y activar el seguimiento?");
                dialogo1.setCancelable(false);
                posicion = position;
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogo1, int id) {
                        myRef.child(codigos[posicion]).child("estado").setValue("activo");
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                dialogo1.show();

            }
        });
    }

    private String[] actualizarLista(Map<String, Object> map, String idEmpresa) {
        String lista = "";
        String estado = "";
        String fila = "";
        String id = "";
        try{
            Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); //Se crea el iterador del map recibido en el listener (Contine todos los pedidos)
            Map.Entry<String, Object> entry= null; // Se crea la variable auxiliar entry que almacenara la llave y el valor existentes en el map (idPedido - Map con datos de pedido)
            String n=null;
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
                    if(entryPedido.getKey().contains("empresa")){
                        id = (String) entryPedido.getKey();
                    }
                }
                if(idEmpresa.equals(id) && estado.equals("En proceso")){
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
