package com.example.tela_inicial;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Perfil extends AppCompatActivity {

    private static final String SHARED_PREFERENCES = "shared";
    private static final String SEXO = "sexo";
    private static final String IDADE = "idade";
    private CardView getCardContat_us;
   private Calendar calendar;
    private int ano, mes, dia;
    private String selectedLabel;
    private int selectedPosition;
    private DatePicker datePicker;
    TextView labelSexo;
    TextView labelIdade;
    TextView labelPeso;
    TextView labelAltura;
    SharedPreferences sharedPreferences;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);

        initComponents();


        labelSexo.setOnClickListener(v -> createAlertDialogSexo());
        labelIdade.setOnClickListener(v -> {
            createAlertDialogIdade().show();
        });
        labelPeso.setOnClickListener(view -> createAlertDialogoPeso());


    }

    private void createAlertDialogoPeso() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Peso");
        //builder.setView(R.id.input_peso);
        builder.create();
        builder.show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {

                    Map<String, Integer> data = new HashMap<>();
                    data.put("ano", ano);
                    data.put("mes", mes);
                    data.put("dia", dia);
                    data.forEach((key, value) -> saveSharedPreferences(key, value)
                    );
                }
            };


    private void initComponents() {
        labelSexo = findViewById(R.id.textViewSexo);
        labelIdade = findViewById(R.id.textIdade);
        labelPeso = findViewById(R.id.textViewPeso);
        labelAltura = findViewById(R.id.textViewAltura);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Dialog createAlertDialogIdade() {
        calendar = Calendar.getInstance();
        Date dataAtual  = calendar.getTime();
        SimpleDateFormat anoAtualFormat = new SimpleDateFormat("yyyy");


        ano = sharedPreferences.getInt("ano", calendar.get(Calendar.YEAR));
        mes = sharedPreferences.getInt("mes", calendar.get(Calendar.MONTH));
        dia = sharedPreferences.getInt("dia", calendar.get(Calendar.DAY_OF_MONTH));
        return new DatePickerDialog(this, dateSetListener, ano, mes, dia);
    }

    private void createAlertDialogSexo() {

        final String arr[] = getResources().getStringArray(R.array.Sexo);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int currentSelectedValue = sharedPreferences.getInt(SEXO, -1);
        builder.setTitle("Telefone");
        builder.setSingleChoiceItems(R.array.Sexo, currentSelectedValue, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedLabel = arr[which];
                selectedPosition = which;
            }
        });
        builder.setPositiveButton("Selecionar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveSharedPreferences(SEXO, selectedPosition);
                Toast.makeText(Perfil.this, selectedLabel, Toast.LENGTH_SHORT).show();
            }
        });

        {
            builder.create();
            builder.show();
        }
    }

    private void saveSharedPreferences(String configNome, int selectedPosition) {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(configNome, selectedPosition);
        editor.apply();
    }


}