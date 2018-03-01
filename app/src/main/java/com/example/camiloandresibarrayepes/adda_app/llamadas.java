package com.example.camiloandresibarrayepes.adda_app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

public class llamadas extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamadas);
    }

    public void llamar_bomberos(View view) {

        try {
            final Intent intentLlamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:199"));
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        if (ActivityCompat.checkSelfPermission(llamadas.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        Toast.makeText(getApplicationContext(), "Llamando, Espere por favor...", Toast.LENGTH_LONG).show();
                        startActivity(intentLlamada);
                    }
                };
            Handler h = new Handler();
            h.postDelayed(r, 1); // <-- "1" es el tiempo de retraso en milisegundos.
        }catch (RuntimeException e){
            Toast.makeText(getApplicationContext(), "Llamada no realizada", Toast.LENGTH_LONG).show();
        }
    }

    public void llamar_policia(View view) {

        try {
            final Intent intentLlamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:122"));
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    if (ActivityCompat.checkSelfPermission(llamadas.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Llamando, Espere por favor...", Toast.LENGTH_LONG).show();
                    startActivity(intentLlamada);
                }
            };
            Handler h = new Handler();
            h.postDelayed(r, 1); // <-- "1" es el tiempo de retraso en milisegundos.
        }catch (RuntimeException e){
            Toast.makeText(getApplicationContext(), "Llamada no realizada", Toast.LENGTH_LONG).show();
        }
    }

    public void llamar_CRC(View view) {

        try {
            final Intent intentLlamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:333"));
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    if (ActivityCompat.checkSelfPermission(llamadas.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Llamando, Espere por favor...", Toast.LENGTH_LONG).show();
                    startActivity(intentLlamada);
                }
            };
            Handler h = new Handler();
            h.postDelayed(r, 1); // <-- "1" es el tiempo de retraso en milisegundos.
        }catch (RuntimeException e){
            Toast.makeText(getApplicationContext(), "Llamada no realizada", Toast.LENGTH_LONG).show();
        }
    }

    public void llamar_fiscalia(View view) {

        try {
            final Intent intentLlamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:222"));
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    if (ActivityCompat.checkSelfPermission(llamadas.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Llamando, Espere por favor...", Toast.LENGTH_LONG).show();
                    startActivity(intentLlamada);
                }
            };
            Handler h = new Handler();
            h.postDelayed(r, 1); // <-- "1" es el tiempo de retraso en milisegundos.
        }catch (RuntimeException e){
            Toast.makeText(getApplicationContext(), "Llamada no realizada", Toast.LENGTH_LONG).show();
        }
    }
}
