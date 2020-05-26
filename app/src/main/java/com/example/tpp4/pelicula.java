package com.example.tpp4;

import android.graphics.Bitmap;

public class pelicula {
     String _titulo;
     String _anio;
     String _posterurl;
     Bitmap _poster;


    public pelicula(String tit, String a,String url, Bitmap pos ){

        _titulo=tit;
        _anio=a;
        _posterurl=url;
        _poster=pos;
    }
    public pelicula( ){


    }
}
