package com.example.tpp4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



public class adaptadordepeliculas extends BaseAdapter {
    private ArrayList<pelicula> arrayPeliculas;
    private Context miContexto;
    pelicula mipeli=new pelicula();
    TextView titulo,anio;
    ImageView POSTER;

    public adaptadordepeliculas(ArrayList<pelicula> arrPeliculas, Context contexto) {
        arrayPeliculas = arrPeliculas;
        miContexto = contexto;
    }

    @Override
    public int getCount() {
        return arrayPeliculas.size();
    }

    @Override
    public pelicula getItem(int position) {
        pelicula peli;
        peli = arrayPeliculas.get(position);
        return peli;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vista;

        LayoutInflater inflador;
        inflador = (LayoutInflater) miContexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vista = inflador.inflate(R.layout.customlistview, parent, false);



        titulo = vista.findViewById(R.id.textotitulo);
        anio = vista.findViewById(R.id.textoanio);
        POSTER = vista.findViewById(R.id.imagenposter);


        mipeli= getItem(position);
        titulo.setText(mipeli._titulo);
        anio.setText(mipeli._anio);


        descargarFoto miTarea= new descargarFoto();
        miTarea.execute(arrayPeliculas.get(position)._posterurl);



        return vista;
    }


    private class descargarFoto extends AsyncTask<Object,Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(Object... elementos) {
            Bitmap imagenconvertida;
            imagenconvertida = null;

            try{

                URL miRuta;
                miRuta =new URL((String) elementos[0]);


                HttpURLConnection conexionUrl;
                conexionUrl=(HttpURLConnection) miRuta.openConnection();

                if(conexionUrl.getResponseCode() == 200)
                {

                    InputStream cuerpoDatos =conexionUrl.getInputStream();
                    BufferedInputStream lector= new BufferedInputStream(cuerpoDatos);

                    imagenconvertida = BitmapFactory.decodeStream(lector);
                    mipeli._poster = imagenconvertida;

                    conexionUrl.disconnect();
                }
            }
            catch (Exception error){
                Log.d("Conexion","Error:" + error.getMessage() );
            }
            return imagenconvertida;
        }

        @Override
        protected void onPostExecute(Bitmap imagenResultante) {

           if(mipeli._poster != null){
               POSTER.setImageBitmap(mipeli._poster);
            }
            else {
               POSTER.setImageResource(android.R.drawable.ic_dialog_alert);

           }


        }
    }




}