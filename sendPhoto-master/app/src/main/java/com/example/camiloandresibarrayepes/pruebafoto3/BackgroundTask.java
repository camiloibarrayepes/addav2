package com.example.camiloandresibarrayepes.pruebafoto3;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Gersain on 08/03/2018.
 */

public class BackgroundTask extends AsyncTask<String,Void,String> {
    AlertDialog alertDialog;
    Context ctx;
    BackgroundTask(Context ctx){
        this.ctx = ctx;
    }
    @Override
    protected void onPreExecute() {

        alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Informacion");
    }

    @Override
    protected String doInBackground(String... params) {
        String reg_url = "http://yamgo.com.co/adda/connection.php";
        String method = params[0];
        if(method.equals("enviar")){
            String nombre = params[1];
            String telefono = params[2];
            String comentario = params[3];
            String latitud = params[4];
            String longitud = params[5];

            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data = URLEncoder.encode("nombre","UTF-8")+"="+URLEncoder.encode(nombre,"UTF-8")+"&"
                        +URLEncoder.encode("telefono","UTF-8")+"="+URLEncoder.encode(telefono,"UTF-8")+"&"+
                        URLEncoder.encode("comentario","UTF-8")+"="+URLEncoder.encode(comentario,"UTF-8")+"&"+
                        URLEncoder.encode("latitud","UTF-8")+"="+URLEncoder.encode(latitud,"UTF-8")+"&"+
                        URLEncoder.encode("longitud","UTF-8")+"="+URLEncoder.encode(longitud,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
              //  Log.d("Parametro1",longitud);
                return "Registro Exitoso";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("Datos Incorrectos")) {
            alertDialog.setMessage(result);
            alertDialog.show();
        }
    }
}

