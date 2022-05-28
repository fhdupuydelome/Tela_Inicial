package com.example.tela_inicial;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Sobre extends AppCompatActivity {

    private CardView getCardCreditos;
    private String data;

    CardView CardRonald;
    CardView cardLeonam;
    CardView CardFernando;
    CardView cardVersao;
    CardView cardGabriel;
    CardView cardLuiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        CardFernando = findViewById(R.id.cardFelipe);
        cardVersao = findViewById(R.id.cardVersão);
        CardRonald = findViewById(R.id.cardRonald);
        cardLeonam = findViewById(R.id.cardLeonam);
        cardGabriel = findViewById(R.id.cardNovo);

        CardFernando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AlertDialog.Builder alerta = new AlertDialog.Builder(Sobre.this);
               alerta.setTitle("Sobre Felipe");
               alerta
                       .setMessage("Felipe é um garoto Comun, estuda na Unifacs e nao tira notas boas e sua frase preferida é (android Studio roda em Qualquer PC)")
                       .setCancelable(false)
                       .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                           }
                       })
                       .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_SHORT).show();
                   }
               });
               AlertDialog alertDialog = alerta.create();
               alertDialog.show();
            }

        });

        cardVersao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(Sobre.this);
                alerta.setTitle("Versão");
                alerta
                        .setMessage("Versão 1.11.0")
                        .setCancelable(false)
                        .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();
            }

        });

        cardLeonam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(Sobre.this);
                alerta.setTitle("Sobre Leonam");
                alerta
                        .setMessage("Um cara normal, Estuda na unifacs e é o Dev desse APP")
                        .setCancelable(false)
                        .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();

            }
        });

        CardRonald.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {AlertDialog.Builder alerta = new AlertDialog.Builder(Sobre.this);
                alerta.setTitle("Sobre Alessandro");
                alerta
                        .setMessage("Um cara normal, Estuda na unifacs e que ajuda o Dev desse APP")
                        .setCancelable(false)
                        .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();

            }
        });

        cardGabriel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {AlertDialog.Builder alerta = new AlertDialog.Builder(Sobre.this);
                alerta.setTitle("Sobre Novo");
                alerta
                        .setMessage("Um cara normal, Estuda na unifacs e que ajuda o Dev desse APP")
                        .setCancelable(false)
                        .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alertDialog = alerta.create();
                alertDialog.show();

            }
        });
    }
}
