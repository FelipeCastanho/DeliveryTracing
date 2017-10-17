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
    DatabaseReference myRef;
    Map<String, Object> map;
    boolean estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_registro);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("empleados");
        ValueEventListener empleadoListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                map = (Map<String, Object>) dataSnapshot.getValue();
            }
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRef.addValueEventListener(empleadoListener);

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

            if(!estado){
                Toast t =
                        Toast.makeText(getApplicationContext(),
                                confirmarContrasena.getText() + " " + ultimoEmpleado(), Toast.LENGTH_SHORT);
                t.show();
            }else{
                Toast t =
                        Toast.makeText(getApplicationContext(),
                                confirmarContrasena.getText() + " " +nombreEmpresa.getText(), Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }

    public int ultimoEmpleado(){
        int numeroEmpleado;
        try{

            Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
            Map.Entry<String, Object> entry = it.next();

            String ultimoEmpleado = entry.getKey();
            String[] numero = ultimoEmpleado.split("empleado");

            numeroEmpleado = Integer.parseInt(numero[1]);

        }catch (Exception ex){
            numeroEmpleado = 1;
        }

        return numeroEmpleado;
    }

}
