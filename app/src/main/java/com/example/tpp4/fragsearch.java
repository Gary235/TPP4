package com.example.tpp4;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class fragsearch extends Fragment implements View.OnClickListener  {

    Button btn;
    EditText edt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    View vista;
        vista = inflater.inflate(R.layout.ingresosearch,container,false);

    btn = vista.findViewById(R.id.botonbuscar);
    edt= vista.findViewById(R.id.edittext);
    btn.setOnClickListener(this);


   return vista;
    }

    @Override
    public void onClick(View v) {

        String textoingresado;
        textoingresado = edt.getText().toString();

        MainActivity main=(MainActivity) getActivity();
        main.recibirTexto(textoingresado);

        main.pasarAOtroFragment();

    }
}
