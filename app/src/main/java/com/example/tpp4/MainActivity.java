package com.example.tpp4;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;


public class MainActivity extends Activity {


    FragmentManager adminFragment;
    FragmentTransaction transaccionFragment;
    String _Texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        adminFragment = getFragmentManager();



        Fragment fragingresosearch;
        fragingresosearch = new fragsearch();


        transaccionFragment=adminFragment.beginTransaction();
        transaccionFragment.replace(R.id.frameLayout, fragingresosearch);
        transaccionFragment.commit();


    }


    public void recibirTexto(String textorecibido)
    {
        _Texto = textorecibido;
    }
    public String conseguirTexto()
    {
        return _Texto;
    }


    public void pasarAOtroFragment()
    {
        Fragment fraglista;
        fraglista = new fragLista();


        transaccionFragment=adminFragment.beginTransaction();
        transaccionFragment.replace(R.id.frameLayout, fraglista);
        transaccionFragment.commit();
    }

}
