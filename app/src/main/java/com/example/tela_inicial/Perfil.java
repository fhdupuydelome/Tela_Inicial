package com.example.tela_inicial;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Perfil extends AppCompatActivity {

    private static final String SHARED_PREFERENCES = "shared";
    private static final String SEXO = "sexo";
    private CardView getCardContat_us;
    private String data;
    private String selectedLabel;
    private int selectedPosition;
    TextView labelSexo;
    TextView labelIdade;
    TextView labelPeso;
    TextView labelAltura;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);


        labelSexo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialogSexo();
            }
        });


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


