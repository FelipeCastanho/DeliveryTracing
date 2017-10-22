package com.example.yoyo.deliverytracing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = null;
    Map<String, Object> map = null;
    String nombreUsuario = "";
    String idEmpresa = "";
    String idUsuario = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        TextView registrame = (TextView) findViewById(R.id.registrarmeText);
        registrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegistro = new Intent(LoginActivity.this, RegistroActivity.class);
                LoginActivity.this.startActivity(intentRegistro);
            }
        });
    }

    public void buttonTapped(View view) {
        EditText campo1 = (EditText)findViewById(R.id.usuario);
        String usuario = campo1.getText().toString();
        EditText campo2 = (EditText)findViewById(R.id.contrasena);
        String pass = campo2.getText().toString();
        String tipo = buscarEmpleado(usuario, pass);
        if(tipo.equals("administrador")){
            SharedPreferences sharedPreference = getSharedPreferences("DeliveryTracing",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreference.edit();
            editor.putString("tipo", tipo);
            editor.putString("nombreUsuario", nombreUsuario);
            editor.putString("idEmpresa", idEmpresa);
            editor.putString("idUsuario", idUsuario);
            editor.commit();
            Intent newActivity = new Intent(LoginActivity.this, AdministradorActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            newActivity.putExtra("tipo", tipo);
            newActivity.putExtra("nombreUsuario", nombreUsuario);
            newActivity.putExtra("idEmpresa", idEmpresa);
            newActivity.putExtra("idUsuario", idUsuario);
            startActivity(newActivity );
        }else if(tipo.equals("empleado")){
            SharedPreferences sharedPreference = getSharedPreferences("DeliveryTracing",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreference.edit();
            editor.putString("tipo", tipo);
            editor.putString("nombreUsuario", nombreUsuario);
            editor.putString("idEmpresa", idEmpresa);
            editor.putString("idUsuario", idUsuario);
            editor.commit();
            Intent newActivity = new Intent(LoginActivity.this, EmpleadoActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            newActivity.putExtra("tipo", tipo);
            newActivity.putExtra("nombreUsuario", nombreUsuario);
            newActivity.putExtra("idEmpresa", idEmpresa);
            newActivity.putExtra("idUsuario", idUsuario);
            startActivity(newActivity );
        }
    }

    protected void onStart() {
        super.onStart();
        myRef = database.getReference("empleados");
        ValueEventListener pedidoListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                map = (Map<String, Object>) dataSnapshot.getValue();
            }
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRef.addValueEventListener(pedidoListener);
    }

    private String buscarEmpleado(String usuario, String pass) {
        boolean mensaje = true;
        try{
            Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
            Map.Entry<String, Object> entry = null;
            String n = null;
            String user = "", password = "", tipo = "";
            while (it.hasNext()) {
                entry= it.next();
                Map<String, Object> mapUsuario = (Map<String, Object>) entry.getValue();
                Iterator<Map.Entry<String, Object>> itUsuario = mapUsuario.entrySet().iterator();
                while (itUsuario.hasNext()) {
                    Map.Entry<String, Object> entryPedido = itUsuario.next();
                    if(entryPedido.getKey().equals("usuario")){
                        user = (String)entryPedido.getValue();
                        idUsuario = entry.getKey();
                    }
                    else if(entryPedido.getKey().equals("password")){
                        password = (String)entryPedido.getValue();
                    }
                    else if(entryPedido.getKey().equals("tipo")){
                        tipo = (String)entryPedido.getValue();
                    }else if(entryPedido.getKey().equals("nombreEmpleado")){
                        nombreUsuario = (String) entryPedido.getValue();
                    }if(entryPedido.getKey().contains("empresa")){
                        idEmpresa = (String) entryPedido.getKey();
                    }
                }
                if(usuario.equals(user)){
                    if(pass.equals(password)){
                        return tipo;
                    }else{
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(),
                                        "Contraseña incorrecta", Toast.LENGTH_SHORT);
                        toast1.show();
                        mensaje = false;
                        break;
                    }
                }
            }
            if(mensaje){
                Toast toast1 =
                        Toast.makeText(getApplicationContext(),
                                "El usuario no se encuentra registrado", Toast.LENGTH_SHORT);
                toast1.show();
            }

        }
        catch (Exception ex){
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "Error al iniciar sesión "+ex.getMessage(), Toast.LENGTH_SHORT);
            toast1.show();
        }
        return "";
    }
}
