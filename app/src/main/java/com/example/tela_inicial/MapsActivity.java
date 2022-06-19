package com.example.tela_inicial;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.tela_inicial.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int REQUEST_LAST_LOCATION = 1;
    private static final int REQUEST_LOCATION_UPDATES = 2;

    private Geocoder geocoder;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location currentPosition, lastPosition;
    private boolean firstFix = true;

    private double distanciaAcumulada;

    private Marker userMarker;
    private Polyline rastro;

    private LatLng currentLocationLatLong;

    SQLiteDatabase db;
    ArrayList<LatLng> trajetoria = new ArrayList<>();

    private void centralizaMapa() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            atualizaPosicaoNoMapa(location);
                        }
                    }
            );

        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LAST_LOCATION
            );
        }
    }

    private void iniciarColetaLocalizacao() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            //Configurar o location request
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(5 * 1000);
            mLocationRequest.setFastestInterval(1 * 1000);
            //registra a callback
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    inserePosicaoNaTabela(locationResult.getLastLocation());
                    atualizaPosicaoNoMapa(locationResult.getLastLocation());

                }
            };
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_UPDATES
            );
        }
    }

    private void inserePosicaoNaTabela(Location location) {
        db.beginTransaction();
        try {
            db.execSQL("insert into coordenadas (latitude,longitude) values ("
                    + location.getLatitude() + "," + location.getLongitude() + ");");
            db.setTransactionSuccessful(); //persista as mudanças
        } catch (SQLiteException e) {
            System.out.println(e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    private ArrayList<LatLng> atualizaTrajetoria() {

        Cursor cursor = db.rawQuery("select latitude,longitude from coordenadas", null);
        int latitudeCol = cursor.getColumnIndex("latitude");
        int longitudeCol = cursor.getColumnIndex("longitude");
        while (cursor.moveToNext()) {
            trajetoria.add(new LatLng(cursor.getDouble(latitudeCol), cursor.getDouble(longitudeCol)));
        }
        return trajetoria;
    }

    private void atualizaPosicaoNoMapa(@NonNull Location location) {
        LatLng currentLatLng, lastLatLng;
        if (firstFix) {
            firstFix = false;
            currentPosition = lastPosition = location;
            currentLatLng = new LatLng(currentPosition.getLatitude(), currentPosition.getLongitude());
            distanciaAcumulada = 0;

        } else {
            lastPosition = currentPosition;
            currentPosition = location;
            currentLatLng = new LatLng(currentPosition.getLatitude(), currentPosition.getLongitude());
            distanciaAcumulada += currentPosition.distanceTo(lastPosition);
        }

        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16.0f));
            if (userMarker == null) {
                userMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng));
            } else {
                userMarker.setPosition(currentLatLng);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(currentLatLng));
            }
        } else {
            System.out.println("Teste");
            System.out.println("Teste: " + location.getLatitude() + ", " + location.getLongitude() );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iniciarColetaLocalizacao();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_historico);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        iniciaBancodeDados();


    }

    private void iniciaBancodeDados() {
        String dbPath = "/data/data/com.example.tela_inicial/historico.db";
        try {
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            db.execSQL("create table if not exists coordenadas (latitude numeric,longitude numeric)");
            db.execSQL("delete from coordenadas");
        } catch (SQLiteException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
        db.close();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LAST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                centralizaMapa();
            } else {
                Toast.makeText(this, "Sem permissão para monitorar sua localização", Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
        }
        if (requestCode == REQUEST_LOCATION_UPDATES) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarColetaLocalizacao();
            } else {
                Toast.makeText(this, "Sem permissão para monitorar sua localização", Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
        }
    }


}



