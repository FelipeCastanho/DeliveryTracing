package com.example.yoyo.deliverytracing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

public class EmpleadoActivity extends AppCompatActivity {
    private Marker marcador;
    LocationManager locManager;
    String idUsuario = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_empleado);

        TextView titulo = (TextView) findViewById(R.id.textSaludo);

        String nombreCompleto = getIntent().getExtras().getString("nombreUsuario");
        String[] nombres = nombreCompleto.split(" ");
        idUsuario = getIntent().getExtras().getString("idUsuario");
        String palabra = "¡Hola ";
        char[] saludo = palabra.concat(nombres[0]+"!").toCharArray();
        titulo.setText(saludo,0,saludo.length);
        miUbicacion();
    }

    public void buttonTapped(View view) {
        int numId = view.getId();
        String id = view.getResources().getResourceEntryName(numId);
        if(id.equals("imgPedidosDisponibles")){
            Intent newActivity = new Intent(EmpleadoActivity.this,PedidosDisponiblesActivity.class);
            String idEmpresa = getIntent().getExtras().getString("idEmpresa");
            newActivity.putExtra("idEmpresa", idEmpresa);
            String idUsuario = getIntent().getExtras().getString("idUsuario");
            newActivity.putExtra("idUsuario", idUsuario);
            startActivity(newActivity );
        } else if(id.equals("imgMisPedidos")){
            Intent newActivity = new Intent(EmpleadoActivity.this,MisPedidosActivity.class);
            String idEmpresa = getIntent().getExtras().getString("idEmpresa");
            String idUsuario = getIntent().getExtras().getString("idUsuario");
            newActivity.putExtra("idUsuario", idUsuario);
            startActivity(newActivity );
        } else if(id.equals("imgLogout")){
            SharedPreferences sharedPreference = getSharedPreferences("DeliveryTracing", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreference.edit();
            editor.putString("tipo", "No hay dato");
            editor.putString("nombreUsuario", "No hay dato");
            editor.putString("idEmpresa", "No hay dato");
            editor.putString("idUsuario", "No hay dato");
            editor.commit();
            locManager.removeUpdates(locationListener);
            Intent newActivity = new Intent(EmpleadoActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newActivity );
        }
    }
    public void alerta(Location location) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("empleados/"+idUsuario);
        myRef.child("latitud").setValue(location.getLatitude());
        myRef.child("longitud").setValue(location.getLongitude());
    }

    private void miUbicacion() {
        locManager = (LocationManager) EmpleadoActivity.this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission( EmpleadoActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( EmpleadoActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        boolean gpsProvider = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkProvider = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(gpsProvider){
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locationListener);
        }else if(networkProvider){
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
        }

    }
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            alerta(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
