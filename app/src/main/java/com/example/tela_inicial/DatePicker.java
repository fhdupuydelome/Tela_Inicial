package com.example.tela_inicial;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Date;

public class DatePicker extends DialogFragment {
   private int ano, mes, dia;

   public DatePicker(){}
   public DatePicker(int ano, int mes, int dia){
        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog,
                (DatePickerDialog.OnDateSetListener)getActivity(),
                ano,
                mes,
                dia);
    }
}
