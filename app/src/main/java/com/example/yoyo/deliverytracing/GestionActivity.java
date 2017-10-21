package com.example.yoyo.deliverytracing;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

public class GestionActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRefEmpleados;
    private Map<String, Object> mapEmpleados;
    private String empresa;
    private int cedula;
    private boolean encontrado;
    private ListView listaEmpleados;
    private String[] nombres;
    private GestionActivity actividad = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gestion);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        empresa = getIntent().getExtras().getString("idEmpresa");
        cedula = 0;
        encontrado = false;
        listaEmpleados = (ListView)findViewById(R.id.listEmpleados);
        database = FirebaseDatabase.getInstance();
        myRefEmpleados = database.getReference("empleados");
        ValueEventListener empleadosListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                mapEmpleados = (Map<String, Object>) dataSnapshot.getValue();
                nombres = actualizarLista(empresa);
                ArrayAdapter<String> adaptador = new ArrayAdapter<>(GestionActivity.this, android.R.layout.simple_list_item_1, nombres);
                listaEmpleados.setAdapter(adaptador);
            }
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRefEmpleados.addValueEventListener(empleadosListener);
    }

    public void buttonTapped(View view) {
        int numId = view.getId();
        String id = view.getResources().getResourceEntryName(numId);

        if(id.equals("fab")){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Agregar empleado").setMessage("Introduzca la cedula del empleado que desea agregar:");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

            builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try{
                        cedula = Integer.parseInt(input.getText().toString());
                        agregarEmpleado(cedula);
                    }catch(Exception ex){
                        Toast mensaje = Toast.makeText(getApplicationContext(), "Ingrese un numero de cédula", Toast.LENGTH_SHORT);
                        mensaje.show();
                    }
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
    }

    public void agregarEmpleado(int cedula){
        Iterator<Map.Entry<String, Object>> it = mapEmpleados.entrySet().iterator();
        Map.Entry<String, Object> entry;
        while(it.hasNext()){
            entry = it.next();
            String empleado = entry.getKey();
            Map<String, Object> mapEmpleado = (Map<String, Object>) entry.getValue();
            Iterator<Map.Entry<String, Object>> itEmpleado = mapEmpleado.entrySet().iterator();
            while(itEmpleado.hasNext()){
                Map.Entry<String, Object> entryPedido = itEmpleado.next();
                if(entryPedido.getKey().contains("empresa")){
                    break;
                }
                if(entryPedido.getKey().equals("cedula")){
                    int numero = Integer.parseInt(entryPedido.getValue().toString());
                    if(numero == cedula){
                        myRefEmpleados.child(empleado+"").child(empresa+"").setValue(true);
                        encontrado = true;
                        Toast mensaje = Toast.makeText(getApplicationContext(), "El empleado fue agregado exitosamente", Toast.LENGTH_SHORT);
                        mensaje.show();
                    }
                }
            }
        }
        if(!encontrado){
            Toast mensaje = Toast.makeText(getApplicationContext(), "El empleado no existe o pertenece a otra empresa", Toast.LENGTH_SHORT);
            mensaje.show();
        }
        encontrado = false;
    }

    public String[] actualizarLista(String id){
        String palabras = "";
        String idEmpresa = "";
        String nombreEmpleado = "";
        Iterator<Map.Entry<String, Object>> it = mapEmpleados.entrySet().iterator();
        Map.Entry<String, Object> entry;
        while(it.hasNext()) {
            entry = it.next();
            Map<String, Object> mapEmpleado = (Map<String, Object>) entry.getValue();
            Iterator<Map.Entry<String, Object>> itEmpleado = mapEmpleado.entrySet().iterator();
            while (itEmpleado.hasNext()) {
                Map.Entry<String, Object> entryPedido = itEmpleado.next();
                if(entryPedido.getKey().contains("empresa")){
                    idEmpresa = entryPedido.getKey().toString();
                }else if(entryPedido.getKey().equals("nombreEmpleado")){
                    nombreEmpleado = entryPedido.getValue().toString();
                }
            }
            if(idEmpresa.equals(id)){
                palabras += nombreEmpleado + ";";
            }
        }

        return palabras.split(";");

    }
}
