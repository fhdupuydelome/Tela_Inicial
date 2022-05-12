package com.example.tela_inicial;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class config extends AppCompatActivity {

    private static final String SHARED_PREFERENCES = "shared";
    private static final String ORIENTACAO = "orientacao";
    private static final String VELOCIDADE = "velocidade";
    private static final String OPCAO_MAPA = "opcaoMapa";
    private static final String TIPO_EXERCICIO = "tipo_exercicio";
    private CardView getCardAjustes;
    private String selectedLabel;
    private int selectedPosition;
    SharedPreferences sharedPreferences;
    CardView cardGlobo;
    CardView cardOrientacao;
    CardView CardVelocidade;
    CardView cardTipoExercicio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);

        CardVelocidade = findViewById(R.id.cardVelocidade);
        cardTipoExercicio = findViewById(R.id.cardTipoExercicio);
        cardGlobo = findViewById(R.id.cardGlobo);
        cardOrientacao = findViewById(R.id.cardOrientação);

        cardGlobo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialogTipoMapa();
            }

        });

        CardVelocidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialogVelocidade();
            }
        });

        cardOrientacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialogOrientacao();
            }
        });

        cardTipoExercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialogTipoExercicio();
            }
        });

    }

    private void createAlertDialogTipoExercicio() {

        final String arr[] = getResources().getStringArray(R.array.Tipo_de_Exercício);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int currentSelectedValue = sharedPreferences.getInt(TIPO_EXERCICIO, -1);
        builder.setTitle("Coordenadas");
        builder.setSingleChoiceItems(R.array.Tipo_de_Exercício, currentSelectedValue, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               selectedLabel = arr[which];
               selectedPosition = which;
            }
        });
        builder.setPositiveButton("Selecionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveSharedPreferences(TIPO_EXERCICIO ,selectedPosition);
                Toast.makeText(config.this, selectedLabel, Toast.LENGTH_SHORT).show();
            }
        });

        {
            builder.create();
            builder.show();
        }
    }

    private void createAlertDialogOrientacao() {

        final String arr[] = getResources().getStringArray(R.array.Orientação);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int currentSelectedValue = sharedPreferences.getInt(ORIENTACAO, -1);
        builder.setTitle("Orientação");
        builder.setSingleChoiceItems(R.array.Orientação, currentSelectedValue, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedLabel = arr[which];
                selectedPosition = which;

            }
        });
        builder.setPositiveButton("Selecionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveSharedPreferences(ORIENTACAO, selectedPosition);
                Toast.makeText(config.this, selectedLabel, Toast.LENGTH_SHORT).show();
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

    private void createAlertDialogVelocidade() {

        final String arr[] = getResources().getStringArray(R.array.Velocidade);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int currentSelectedValue = sharedPreferences.getInt(VELOCIDADE, -1);
        builder.setTitle("Velocidade");
        builder.setSingleChoiceItems(R.array.Velocidade, currentSelectedValue, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedLabel = arr[which];
                selectedPosition = which;

            }
        });
        builder.setPositiveButton("Selecionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveSharedPreferences(VELOCIDADE, selectedPosition);
                Toast.makeText(config.this, selectedLabel, Toast.LENGTH_SHORT).show();
            }
        });

        {
            builder.create();
            builder.show();
        }
    }

    private void createAlertDialogTipoMapa() {

        final String arr[] = getResources().getStringArray(R.array.mapas);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int currentSelectedValue = sharedPreferences.getInt(OPCAO_MAPA, -1);
        builder.setTitle("Opções de Mapas");
        builder.setSingleChoiceItems(R.array.mapas, currentSelectedValue, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedLabel = arr[which];
                selectedPosition = which;

            }
        });
        builder.setPositiveButton("Selecionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveSharedPreferences(OPCAO_MAPA, selectedPosition);
                Toast.makeText(config.this, selectedLabel, Toast.LENGTH_SHORT).show();
            }
        });

        {
            builder.create();
            builder.show();

        }


    }


}
