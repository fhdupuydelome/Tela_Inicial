package com.example.tela_inicial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private CardView getCardtela_inicial;

    CardView cardAtividade;
    CardView cardAjustes;
    CardView cardPerfil;
    CardView cardSobre;
    CardView cardHistorico;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardAtividade = findViewById(R.id.cardAtividade);
        cardAjustes = findViewById(R.id.cardAjustes);
        cardSobre = findViewById(R.id.cardInfo);
        cardPerfil = findViewById(R.id.cardPerfil);
        cardHistorico = findViewById(R.id.cardHistorico);

        cardAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Intent = new Intent(getApplicationContext(), config.class);
                startActivity(Intent);
            }


        });

        cardSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Intent = new Intent(getApplicationContext(), Sobre.class);
                startActivity(Intent);
            }

        });

        cardAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(Intent);
            }

        });

        cardPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Intent = new Intent(getApplicationContext(), Perfil.class);
                startActivity(Intent);
            }

        });

        cardHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Intent = new Intent(getApplicationContext(), HistoricoActivity.class);
                startActivity(Intent);
            }

        });

    }


}

