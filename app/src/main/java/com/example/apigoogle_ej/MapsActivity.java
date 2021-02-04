package com.example.apigoogle_ej;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private Marker marker;
    int cont =0 ;
    TextView txttype;
    LatLng myposition;
    int cont_poligono=0;
    PolylineOptions lineas;
    boolean activate=false;
    Button btnPoligono;
    TextView txtmarcador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        txttype = findViewById(R.id.txtType);
        lineas = new PolylineOptions();
        btnPoligono = findViewById(R.id.btnPoligono);
        btnPoligono.setBackgroundColor(Color.GREEN);
        txtmarcador = findViewById(R.id.txtMarcador);
    }

    public void MapType(View view)
    {
        cont++;
        if(cont == 0){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            txttype.setText("Normal");
        }
        if(cont == 1){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            txttype.setText("Satellite");
        }
        if(cont == 2){
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            txttype.setText("Terrain");
        }
        if(cont == 3){
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            txttype.setText("Hybrid");
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
        if(cont==3)
            cont=-1;
    }
    public void Poligono(View view)
    {
        if(activate==false) {
            activate = true;
            btnPoligono.setBackgroundColor(Color.RED);
            lineas = new PolylineOptions();
        }
        else{
            activate = false;
            cont_poligono=0;
            btnPoligono.setBackgroundColor(Color.GREEN);
            lineas = null;
        }
    }

    public void Move(View view)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myposition));

        CameraPosition camPos = new CameraPosition.Builder()
                .target(myposition)
                .zoom(10)
                .bearing(45)      //noreste arriba
                .tilt(70)         //punto de vista de la cámara 70 grados
                .build();
        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(camUpd3);
    }
    public void addMarcador(View view)
    {
        mMap.addMarker(new MarkerOptions().position(myposition).title(txtmarcador.getText().toString()));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        myposition = new LatLng(-1.574854, -78.290045);
        marker = mMap.addMarker(new MarkerOptions()
        .position(myposition)
        .title("My position").draggable(true));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(myposition));
        //mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Toast.makeText(this,"My position : " + marker.getPosition().latitude + "-" + marker.getPosition().longitude,Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Toast.makeText(this,"Lat" + marker.getPosition().latitude +
                " lng " + marker.getPosition().longitude,Toast.LENGTH_LONG).show();
        myposition = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
    }

    @Override
    public void onMapClick(LatLng latLng) {

        Projection proj = mMap.getProjection();
        Point coord = proj.toScreenLocation(latLng);

        Toast.makeText(this,
        "Click\n" +"Lat: " + latLng.latitude + "\n" +"Lng: "
                + latLng.longitude + "\n" +"X: "
                + coord.x + " - Y: " + coord.y,Toast.LENGTH_SHORT).show();
        if(activate == true){
            lineas.add(new LatLng(latLng.latitude,latLng.longitude));
            cont_poligono++;
        }
        if(cont_poligono == 5){
            cont_poligono=0;
            activate=false;
            btnPoligono.setBackgroundColor(Color.GREEN);
            lineas.width(8);
            lineas.color(Color.RED);
            mMap.addPolyline(lineas);
        }

    }
}