package com.example.yoyo.deliverytracing;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.Tag;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class RastreoActivity extends AppCompatActivity  implements OnMapReadyCallback,ValueEventListener {
    String codigo;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefEmpleado = null;
    private DatabaseReference mLatitud;
    private DatabaseReference mLongitud;
    private GoogleMap mMap;
    private Marker marcador;
    Location location = new Location(LocationManager.GPS_PROVIDER);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rastreo);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        String referencia = getIntent().getExtras().getString("referencia");
        myRefEmpleado = database.getReference("empleados/"+referencia);
        mLatitud = myRefEmpleado.child("latitud");
        mLongitud = myRefEmpleado.child("longitud");
        mLatitud.addValueEventListener(this);
        mLongitud.addValueEventListener(this);
    }

    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

    }

    public void agregarMarcador(double latitud, double longitud) {
        LatLng coordenadas = new LatLng(latitud, longitud);
        if (marcador != null) {
            marcador.remove();
        }
        //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
        marcador = mMap.addMarker(new MarkerOptions().position(coordenadas).title("Mi ubicación"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 16));
        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        "A", Toast.LENGTH_SHORT);
        toast1.show();
    }

    private void actualizarUbicacion(Location location) {
        if (location != null) {
            double latitud = location.getLatitude();
            double longitud = location.getLongitude();
            agregarMarcador(latitud, longitud);
        }
    }

    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.getValue(Double.class) != null){
            String key = dataSnapshot.getKey();
            if(key.equals("latitud")){
                Double latitud = dataSnapshot.getValue(Double.class);
                location.setLatitude(latitud);
                Toast toast1 =
                        Toast.makeText(getApplicationContext(),
                                latitud+"", Toast.LENGTH_SHORT);
                toast1.show();
            }else if(key.equals("longitud")){
                Double longitud = dataSnapshot.getValue(Double.class);
                location.setLongitude(longitud);
                Toast toast1 =
                        Toast.makeText(getApplicationContext(),
                                longitud+"", Toast.LENGTH_SHORT);
                toast1.show();
            }
            actualizarUbicacion(location);
        }
    }

    public void onCancelled(DatabaseError databaseError) {

    }

}