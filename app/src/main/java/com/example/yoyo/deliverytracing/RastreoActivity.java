package com.example.yoyo.deliverytracing;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class RastreoActivity extends AppCompatActivity {
    String codigo;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rastreo);
        codigo = getIntent().getExtras().getString("codigo");
        myRef = database.getReference("pedidos/"+codigo);
    }



    @Override
    protected void onStart() {
        super.onStart();
        ValueEventListener pedidoListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String key;
                //Object value;
                //FirebaseDatabase database = FirebaseDatabase.getInstance();
                //DatabaseReference myRef = database.getReference("message");
                //Pedidos post = dataSnapshot.getValue(Pedidos.class);
                //myRef.setValue(post.toString());

                DatabaseReference myRef = database.getReference("message");

                String valor = "";
                try{
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    Iterator <Map.Entry<String, Object>> it = map.entrySet().iterator();
                    Map.Entry<String, Object> entry= null;

                    String n=null;
                    while (it.hasNext()) {
                        entry= it.next();
                        n = entry.getKey(); //clave
                        String dato = "";
                        boolean opcion = false;
                        try{
                            dato = (String) entry.getValue();
                        }
                        catch (Exception ex){
                            opcion =  (boolean)entry.getValue();
                        }
                        valor += n;
                        if(!dato.equals("")){
                            valor += " "+dato + " - ";
                        }
                        else{
                            valor += " "+opcion + " - ";
                        }

                    }

                    myRef.setValue(valor);
                }catch (Exception ex){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addValueEventListener(pedidoListener);
    }


}
