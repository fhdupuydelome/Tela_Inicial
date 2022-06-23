package com.example.tela_inicial;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.tela_inicial.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnSuccessListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    CardView btnIniciar, btnSalvar;
    TextView speedDisplay, timeDisplay;

    Chronometer cron;
    private ArrayList<Float> velocidadesParciais = new ArrayList<>();

    private Boolean registrarAtividade = false;
    private boolean firstFix = true;
    private Map<String, Integer> appConfig = new HashMap<>();
    SharedPreferences sharedPreferences;

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
                    if (registrarAtividade) {

                        registrarAtividade(locationResult.getLastLocation());
                    }
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

    private void registrarAtividade(Location location) {

        if (firstFix) {
            firstFix = false;
            currentPosition = lastPosition = location;
            distanciaAcumulada = 0;

        } else {
            lastPosition = currentPosition;
            currentPosition = location;
            distanciaAcumulada += currentPosition.distanceTo(lastPosition);
        }
        if (location.hasSpeed()) {

            float velocidadMs = location.getSpeed();
            velocidadesParciais.add(velocidadMs);
            BigDecimal bd = new BigDecimal(velocidadMs).setScale(2, RoundingMode.HALF_EVEN);

            switch (appConfig.get("velocidade")) {
                case 0:
                    speedDisplay.setText(String.valueOf(bd) + " m/s");
                    break;
                case 1:
                    speedDisplay.setText(String.valueOf(bd.divide(
                            BigDecimal.valueOf(3.6), 2, RoundingMode.HALF_UP)) + " Km/H");
                    break;
            }
        }


        inserePosicaoNaTabela(location);
    }

    private void inserePosicaoNaTabela(@NonNull Location location) {
        System.out.println("Registrando atividade");
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

    private void atualizaPosicaoNoMapa(@NonNull Location location) {
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16.0f));
            if (userMarker == null) {
                userMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng));
            } else {
                userMarker.setPosition(currentLatLng);

                switch (appConfig.get("tipo_orientacao")) {
                    case 0:
                        mMap.getUiSettings().setRotateGesturesEnabled(true);
                        mMap.getUiSettings().setScrollGesturesEnabled(true);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                        break;
                    case 1:
                        mMap.getUiSettings().setRotateGesturesEnabled(false);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                        break;
                    case 2:
                        mMap.getUiSettings().setRotateGesturesEnabled(false);
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                .target(currentLatLng)
                                .bearing(location.getBearing())
                                .zoom(16.0f)
                                .build()));
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);


        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_activity);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);


        cron = (Chronometer) findViewById(R.id.cronometro);

        btnIniciar = findViewById(R.id.btnStart);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarAtividade = !registrarAtividade;
                if (registrarAtividade) {
                    iniciarCronometro();
                }
            }
        });
        btnSalvar = findViewById(R.id.btnSave);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                armazenarInfoAtividade();
            }
        });

        speedDisplay = findViewById(R.id.textView4);


        loadAppConfig();
        iniciaBancodeDados();
        iniciarColetaLocalizacao();


    }

    private void armazenarInfoAtividade() {
        float velocidadeMedia = obterVelocidadeMedia();
       double caloriaTotal = calcularGastoCalorico(velocidadeMedia);


    }

    private double calcularGastoCalorico(float velocidade) {
        float peso = sharedPreferences.getFloat("peso", 0.0f);

        double fatorCaloria = 0;
        int tipoExercicio = sharedPreferences.getInt("tipo_exercicio", 0);

        switch (tipoExercicio){
            case 0:
                fatorCaloria = 0.0140;
                break;
            case 1:
                fatorCaloria = 0.175;
                break;
            case 2:
                fatorCaloria = 0.0199;
                break;
        }
        double caloria = (peso * velocidade) * fatorCaloria;
        double caloriaTota = 0;
        String[] tempoTotal = obterTempoTotal();
        String min, sec;
        min = tempoTotal[1];
        sec = tempoTotal[0];

        if(!"00".equals(min)){
            caloriaTota = caloria * Double.parseDouble(min);
        } else if(!"00".equals(sec)){
            caloriaTota += caloria * (Double.parseDouble(sec) / 60);
        }
        return  caloriaTota;
    }

    private String[] obterTempoTotal() {
        String[] minSec = cron.getText().toString().split(":");
        String min = minSec[0];
        String sec = minSec[1];
        return  minSec;
    }

    private float obterVelocidadeMedia() {
        float somatorioVelocidades = 0, total = 0, media = 0;
        for (int i = 0; i < velocidadesParciais.size(); i++){
            somatorioVelocidades += velocidadesParciais.get(i);
            total++;
        }
        if (total <=  0){
            return 0;
        } 
        return media = somatorioVelocidades/total;
    }

    private void iniciarCronometro() {
        cron.setBase(SystemClock.elapsedRealtime());
        cron.start();
    }

    private void loadAppConfig() {
        appConfig.put("tipo_orientacao", sharedPreferences.getInt("orientacao", 0));
        appConfig.put("velocidade", sharedPreferences.getInt("velocidade", 0));
        appConfig.put("tipo_exercicio", sharedPreferences.getInt("tipo_exercicio", 0));
        appConfig.put("opcao_mapa", sharedPreferences.getInt("opcaoMapa", 0));
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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                int tipoMapa = sharedPreferences.getInt("opcaoMapa", 0);

                switch (tipoMapa){
                    case 0:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 1:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                }
            }
        });
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



