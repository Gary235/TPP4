package com.example.tpp4;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class fragDetalle extends Fragment {

    TextView Titulo,Fecha,Director,Plot;
    ImageView Poster;
    String texto,titulo,fecha,director,plot,url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vista ;
        vista = inflater.inflate(R.layout.detalles,container,false);


        MainActivity main= (MainActivity) getActivity();

        texto = main.conseguirTexto();
        Titulo = vista.findViewById(R.id.titulodetalle);
        Fecha = vista.findViewById(R.id.fechadetalle);
        Director = vista.findViewById(R.id.directordetalle);
        Plot = vista.findViewById(R.id.plotdetalle);




        tareaAsincronica mitarea = new tareaAsincronica();
        mitarea.execute();




        return vista;
    }



    private class tareaAsincronica extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... parametros) {

            try {
                //api key: ef75b81a
                //declaramos la url como nuestra conexión
                URL miRuta = new URL("http://www.omdbapi.com/?apikey=ef75b81a&t="+texto+"&plot=full");
                Log.d("Conexion", "Me voy a conectar");


                HttpURLConnection miConexion = (HttpURLConnection) miRuta.openConnection();
                Log.d("Conexion", "Ya establecí la conexión");

                if (miConexion.getResponseCode() == 200) {
                    Log.d("Conexion", "Me pude conectar y todo OK");

                    //leemos el JSon
                    InputStream cuerpoRespuesta = miConexion.getInputStream();
                    InputStreamReader lectorRespuesta = new InputStreamReader(cuerpoRespuesta, "UTF-8");
                    JsonParser parseadorDeJson = new JsonParser();
                    JsonObject objectoJson = parseadorDeJson.parse(lectorRespuesta).getAsJsonObject();


                    //cargamos el array
                    JsonObject objSearch = objectoJson.getAsJsonObject();

                    titulo = objSearch.get("Title").getAsString();
                    fecha = objSearch.get("Released").getAsString();
                    director = objSearch.get("Director").getAsString();
                    plot = objSearch.get("Plot").getAsString();
                    url = objSearch.get("Poster").getAsString();

                    Log.d("Conexion", "Url:" + url);

                    descargarFoto mitarea2 = new descargarFoto();
                    mitarea2.execute(url);

                } else {
                    Log.d("Conexion", "Me pude conectar pero algo malo pasó");
                }
                miConexion.disconnect();
            } catch (Exception error) {
                Log.d("Conexion", "Al conectar o procesar ocurrió Error: " + error.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Titulo.setText(titulo);
            Fecha.setText("Fecha: "+fecha);
            Director.setText("Director: "+director);
            Plot.setText("Resumen: "+plot);

        }


    }


    private class descargarFoto extends AsyncTask<String,Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... url) {
            Bitmap imagenconvertida;
            imagenconvertida = null;

            try{

                URL miRuta;
                miRuta =new URL(url[0]);


                HttpURLConnection conexionUrl;
                conexionUrl=(HttpURLConnection) miRuta.openConnection();

                if(conexionUrl.getResponseCode() == 200)
                {

                    InputStream cuerpoDatos =conexionUrl.getInputStream();
                    BufferedInputStream lector= new BufferedInputStream(cuerpoDatos);

                    imagenconvertida = BitmapFactory.decodeStream(lector);
                    Log.d("Conexion", "Imagen:" + imagenconvertida);



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

            if(imagenResultante != null){
                Poster.setImageBitmap(imagenResultante);
            }
            else {
                Poster.setImageResource(android.R.drawable.ic_dialog_alert);

            }


        }
    }


}
