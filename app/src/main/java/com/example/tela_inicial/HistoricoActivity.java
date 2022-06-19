package com.example.tela_inicial;

import androidx.fragment.app.FragmentActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.example.tela_inicial.databinding.ActivityHistoricoBinding;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class HistoricoActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityHistoricoBinding binding;

    ArrayList<LatLng> trajetoria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHistoricoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        obterHistorico();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_historico);

        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.addPolyline(new PolylineOptions().addAll(trajetoria));

        double latmin = trajetoria.get(0).latitude;
        double latmax = trajetoria.get(0).latitude;
        double lonmin = trajetoria.get(0).longitude;
        double lonmax = trajetoria.get(0).longitude;
        for (int i = 1; i < trajetoria.size(); i++) {
            double lat, lon;
            lat = trajetoria.get(i).latitude;
            lon = trajetoria.get(i).longitude;
            latmin = latmin < lat ? latmin : lat;
            latmax = latmax > lat ? latmax : lat;
            lonmin = lonmin < lon ? lonmin : lon;
            lonmax = lonmax > lon ? lonmax : lon;
        }
        LatLng southwest = new LatLng(latmin, lonmin);
        LatLng northeast = new LatLng(latmax, lonmax);
        LatLngBounds boundingBox = new LatLngBounds(southwest, northeast);
        mMap.setOnMapLoadedCallback(() -> mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundingBox, 50)));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "fechar", Toast.LENGTH_SHORT).show();

    }

    private void obterHistorico() {
        String dbPath = "/data/data/com.example.tela_inicial/historico.db";
        SQLiteDatabase db;
        try {
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            Cursor cursor = db.rawQuery("select latitude,longitude from coordenadas", null);
            int latitudeCol = cursor.getColumnIndex("latitude");
            int longitudeCol = cursor.getColumnIndex("longitude");
            trajetoria = new ArrayList<LatLng>();
            while (cursor.moveToNext()) {
                trajetoria.add(new LatLng(cursor.getDouble(latitudeCol), cursor.getDouble(longitudeCol)));
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}