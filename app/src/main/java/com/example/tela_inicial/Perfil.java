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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Perfil extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String SHARED_PREFERENCES = "shared";
    private static final String SEXO = "sexo";
    private static final String IDADE = "idade";
    private CardView getCardContat_us;
    private int ano, mes, dia;
    private String selectedLabel;
    private int selectedPosition;
    private DatePicker datePicker;
    TextView labelSexo;
    TextView labelIdade;
    TextView labelPeso;
    TextView labelAltura;
    View btnSalvar;
    EditText inputPeso;
    SharedPreferences sharedPreferences;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);

        initComponents();


        labelSexo.setOnClickListener(v -> createAlertDialogSexo());
        labelIdade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertDialogIdade();
            }
        });
        labelPeso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertDialogPeso();
            }
        });
        labelAltura.setOnClickListener(new View.OnClickListener() {
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

        builder.setView(alturaInputLayout);
        builder
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        EditText inpuAltura = alturaInputLayout.findViewById(R.id.edit_text_altura);
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
        data.forEach(this::saveSharedPreferences
        );
    }


    private void initComponents() {
        labelSexo = findViewById(R.id.textViewSexo);
        labelIdade = findViewById(R.id.textIdade);
        labelPeso = findViewById(R.id.textViewPeso);
        labelAltura = findViewById(R.id.textViewAltura);
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