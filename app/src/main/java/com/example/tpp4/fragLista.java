package com.example.tpp4;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class fragLista extends Fragment {

    ListView lista;
    String texto;
    ArrayList<pelicula> arraydepeliculas=new ArrayList<>();
    adaptadordepeliculas adapatador;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vista;
        vista = inflater.inflate(R.layout.listview,container,false);

        MainActivity main= (MainActivity) getActivity();

        texto = main.conseguirTexto();
        lista = vista.findViewById(R.id.lista);

        adapatador = new adaptadordepeliculas(arraydepeliculas,getActivity());

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
                URL miRuta = new URL("http://www.omdbapi.com/?apikey=ef75b81a&s=" + texto +"&limit=1");
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

                    Log.d("Conexion", "Texto ingresado:" + texto);

                    //cargamos el array
                    JsonArray arrSearch = objectoJson.get("Search").getAsJsonArray();


                    for (int i = 0; i < arrSearch.size(); i++) {
                        JsonObject unSearch = arrSearch.get(i).getAsJsonObject();
                        pelicula miPeli = new pelicula();

                        miPeli._titulo = unSearch.get("Title").getAsString();
                        miPeli._anio = unSearch.get("Year").getAsString();
                        miPeli._posterurl = unSearch.get("Poster").getAsString();

                        arraydepeliculas.add(miPeli);
                    }

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

           lista.setAdapter(adapatador);

        }


    }




}
