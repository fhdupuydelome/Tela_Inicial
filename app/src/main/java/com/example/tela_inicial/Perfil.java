package com.example.tela_inicial;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Perfil extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String SHARED_PREFERENCES = "shared";
    private static final String GENERO = "genero";
    private static final String IDADE = "idade";
    private String selectedLabel;
    private int selectedPosition;
    TextView textButtonGenero;
    TextView textButtonNascimento;
    TextView textButtonPeso;
    TextView textButtonAltura;
    TextView labelGenero, labelNascimento, labelPeso, labelAltura, labelIdade, labelImc;
    SharedPreferences sharedPreferences;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);

        initComponents();


        textButtonGenero.setOnClickListener(v -> createAlertDialogGenero());
        textButtonNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertDialogIdade();
            }
        });
        textButtonPeso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertDialogPeso();
            }
        });
        textButtonAltura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertDialogAltura();
            }
        });


    }

    private void createAlertDialogAltura() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Altura");

        final View alturaInputLayout = getLayoutInflater().inflate(R.layout.altura_input_layout, null);
        NumberPicker inputAltura = (NumberPicker) alturaInputLayout.findViewById(R.id.edit_text_altura);

        inputAltura.setMaxValue(3);
        inputAltura.setMinValue(1);
        inputAltura.setValue(1);

        builder.setView(alturaInputLayout);
        builder
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        int alturaValue = inputAltura.getValue();
                        saveSharedPreferences("altura", alturaValue);
                    }
                });
        AlertDialog pesoDialog = builder.create();
        pesoDialog.show();
    }

    private void createAlertDialogPeso() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Peso");

        final View pesoInputLayout = getLayoutInflater().inflate(R.layout.peso_input_layout, null);

        builder.setView(pesoInputLayout);
        builder
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        EditText inputPeso = pesoInputLayout.findViewById(R.id.edit_text_peso);

                        int pesoValue = Integer.parseInt(inputPeso.getText().toString());
                        saveSharedPreferences("peso", pesoValue);
                    }
                });
        AlertDialog pesoDialog = builder.create();
        pesoDialog.show();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {

        Map<String, Integer> data = new HashMap<>();
        data.put("ano", ano);
        data.put("mes", mes);
        data.put("dia", dia);
        data.forEach((configNome, selectedPosition1) -> saveSharedPreferences(configNome, selectedPosition1)
        );
    }


    private void initComponents() {
        textButtonGenero = (TextView) findViewById(R.id.textViewGenero);
        textButtonNascimento = (TextView) findViewById(R.id.textIdade);
        textButtonPeso = (TextView) findViewById(R.id.textViewPeso);
        textButtonAltura = (TextView) findViewById(R.id.textViewAltura);
        labelGenero = (TextView) findViewById(R.id.generoOut);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createAlertDialogIdade() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        com.example.tela_inicial.DatePicker datePickerDialogFragment;
        datePickerDialogFragment = new com.example.tela_inicial.DatePicker(
                sharedPreferences.getInt("ano", calendar.get(Calendar.YEAR)),
                sharedPreferences.getInt("mes", calendar.get(Calendar.MONTH)),
                sharedPreferences.getInt("dia", calendar.get(Calendar.DAY_OF_MONTH))
        );
        datePickerDialogFragment.show(getSupportFragmentManager(), "Data de Nascimento");
    }

    private void createAlertDialogGenero() {

        final String arr[] = getResources().getStringArray(R.array.Genero);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int currentSelectedValue = sharedPreferences.getInt(GENERO, -1);
        builder.setTitle("Genero");
        builder.setSingleChoiceItems(R.array.Genero, currentSelectedValue, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedLabel = arr[which];
                selectedPosition = which;
            }
        });
        builder.setPositiveButton("Selecionar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveSharedPreferences(GENERO, selectedPosition);
                labelGenero.setText(selectedLabel);
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