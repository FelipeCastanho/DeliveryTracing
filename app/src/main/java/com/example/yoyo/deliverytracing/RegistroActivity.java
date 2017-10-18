package com.example.yoyo.deliverytracing;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Iterator;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRefEmpleados;
    DatabaseReference myRefEmpresas;
    Map<String, Object> mapEmpleado;
    Map<String, Object> mapEmpresa;
    boolean estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_registro);

        database = FirebaseDatabase.getInstance();
        myRefEmpleados = database.getReference("empleados");
        ValueEventListener empleadoListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                mapEmpleado = (Map<String, Object>) dataSnapshot.getValue();
            }
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRefEmpleados.addValueEventListener(empleadoListener);

        myRefEmpresas = database.getReference("empresas");
        ValueEventListener empresaListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                mapEmpresa = (Map<String, Object>) dataSnapshot.getValue();
            }
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRefEmpresas.addValueEventListener(empresaListener);

        Switch isAdmin = (Switch) findViewById(R.id.switchAdmin);
        isAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextInputLayout empresa = (TextInputLayout) findViewById(R.id.textInputLayoutEmpresa);
                if(isChecked){
                    empresa.setVisibility(View.VISIBLE);
                    estado = true;
                }else{
                    empresa.setVisibility(View.GONE);
                    estado = false;
                }
            }
        });
    }

    public void buttonTapped(View view) {
        int numId = view.getId();
        String id = view.getResources().getResourceEntryName(numId);

        if(id.equals("botonRegistro")){
            EditText cedula = (EditText) findViewById(R.id.cedula);
            EditText nombre = (EditText) findViewById(R.id.nombre);
            EditText usuario = (EditText) findViewById(R.id.usuario);
            EditText contrasena = (EditText) findViewById(R.id.contrasena);
            EditText confirmarContrasena = (EditText) findViewById(R.id.confirmar);
            EditText nombreEmpresa = (EditText) findViewById(R.id.empresa);

            int numeroEmpleado = ultimoEmpleado() + 1;
            int numeroEmpresa = ultimaEmpresa() + 1;

            try{
                if(contrasena.getText().toString().equals(confirmarContrasena.getText().toString())){
                    myRefEmpleados.child("empleado"+numeroEmpleado).child("nombreEmpleado").setValue(nombre.getText().toString());
                    myRefEmpleados.child("empleado"+numeroEmpleado).child("cedula").setValue(Integer.parseInt(cedula.getText().toString()));
                    myRefEmpleados.child("empleado"+numeroEmpleado).child("usuario").setValue(usuario.getText().toString());
                    myRefEmpleados.child("empleado"+numeroEmpleado).child("password").setValue(contrasena.getText().toString());
                    myRefEmpleados.child("empleado"+numeroEmpleado).child("latitud").setValue(0);
                    myRefEmpleados.child("empleado"+numeroEmpleado).child("longitud").setValue(0);
                    if(!estado){
                        myRefEmpleados.child("empleado"+numeroEmpleado).child("tipo").setValue("empleado");
                    }else{
                        myRefEmpleados.child("empleado"+numeroEmpleado).child("tipo").setValue("administrador");
                        myRefEmpleados.child("empleado"+numeroEmpleado).child("empresa"+numeroEmpresa).setValue(true);
                        myRefEmpresas.child("empresa"+numeroEmpresa).child("nombreEmpresa").setValue(nombreEmpresa.getText().toString());
                    }
                    Toast t = Toast.makeText(getApplicationContext(), "El registro se ha efectuado exitosamente", Toast.LENGTH_SHORT);
                    t.show();
                    finish();
                }else{
                    Toast t = Toast.makeText(getApplicationContext(), "Las contraseñas deben coincidir", Toast.LENGTH_SHORT);
                    t.show();
                }

            }catch (Exception ex){
                Toast t = Toast.makeText(getApplicationContext(), "Error en el registro", Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }

    public int ultimoEmpleado(){
        int numeroEmpleado;
        try{

            Iterator<Map.Entry<String, Object>> it = mapEmpleado.entrySet().iterator();
            Map.Entry<String, Object> entry = it.next();

            String ultimoEmpleado = entry.getKey();
            String[] numero = ultimoEmpleado.split("empleado");

            numeroEmpleado = Integer.parseInt(numero[1]);

        }catch (Exception ex){
            numeroEmpleado = 1;
        }

        return numeroEmpleado;
    }

    public int ultimaEmpresa(){
        int numeroEmpresa;
        try{

            Iterator<Map.Entry<String, Object>> it = mapEmpresa.entrySet().iterator();
            Map.Entry<String, Object> entry = it.next();

            String ultimoEmpleado = entry.getKey();
            String[] numero = ultimoEmpleado.split("empresa");

            numeroEmpresa = Integer.parseInt(numero[1]);

        }catch (Exception ex){
            numeroEmpresa = 1;
        }

        return numeroEmpresa;
    }

}
