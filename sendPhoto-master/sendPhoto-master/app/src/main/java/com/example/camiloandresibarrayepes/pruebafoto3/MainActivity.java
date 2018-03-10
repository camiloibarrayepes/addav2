package com.example.camiloandresibarrayepes.pruebafoto3;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.EachExceptionsHandler;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    private ProgressDialog progressDialog;

    ImageView ivCamera, ivGallery, ivImage, ivUpload;
    EditText nombre,telefono,comentario;

    CameraPhoto cameraPhoto;

    GalleryPhoto galleryPhoto;

    final int CAMERA_REQUEST = 1100;
    final int GALLERY_REQUEST = 2200;

    String selectedPhoto;

    private static final int SOLICITUD_PERMISO_CALL_PHONE = 1;
    private static final int SOLICITUD_PERMISO_CAMARA = 2;
    private static final int SOLICITUD_PERMISO_STORAGE_READ = 3;
    private static final int SOLICITUD_PERMISO_STORAGE_WRITE = 4;
    private static final int SOLICITUD_PERMISO_GPS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombre = (EditText)findViewById(R.id.nombre);
        telefono = (EditText)findViewById(R.id.telefono);
        comentario = (EditText)findViewById(R.id.comentario);

        //Permiso CALL

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            // startActivity(intentLLamada);
            // Toast.makeText(this, "Permiso Concedido", Toast.LENGTH_SHORT).show();
        } else {
            explicarUsoPermisoLlamada();
            solicitarPermisoHacerLlamada();
        }

        //Permiso CAMARA

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // startActivity(intentLLamada);
            // Toast.makeText(this, "Permiso Concedido", Toast.LENGTH_SHORT).show();
        } else {
            explicarUsoPermisoCamara();
            //solicitarPermisoHacerCamara();
        }

        //Permiso STORAGE READ

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // startActivity(intentLLamada);
            // Toast.makeText(this, "Permiso Concedido", Toast.LENGTH_SHORT).show();
        } else {
            explicarUsoPermisoStogareRead();
            //solicitarPermisoHacerStorageRead();
        }

        //Permiso STORAGE WRITE

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // startActivity(intentLLamada);
            // Toast.makeText(this, "Permiso Concedido", Toast.LENGTH_SHORT).show();
        } else {
            explicarUsoPermisoStogareWrite();
            //solicitarPermisoHacerStorageWrite();
        }

        //Permiso GPS fine

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // startActivity(intentLLamada);
            // Toast.makeText(this, "Permiso Concedido", Toast.LENGTH_SHORT).show();
        } else {
            explicarUsoPermisoGPSFine();
            //solicitarPermisoHacerGPSFine();
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());


        progressDialog = new ProgressDialog(this);


        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());

        ivCamera = (ImageView) findViewById(R.id.ivCamera);
        ivGallery = (ImageView) findViewById(R.id.ivGallery);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivUpload = (ImageView) findViewById(R.id.ivUpload);

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                    cameraPhoto.addToGallery();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),
                            "SALIO ALGO MAL", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        });


        ivUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedPhoto == null){
                   Toast.makeText(getApplicationContext(),"NO HAY IMAGEN", Toast.LENGTH_SHORT).show();
                    return;
                }


                try {
                    Bitmap bitmap = ImageLoader.init().from(selectedPhoto).requestSize(200, 200).getBitmap();
                    String encodedImage = ImageBase64.encode(bitmap);
                    Log.d(TAG, encodedImage);

                    //SEND TO PHP SCRIPT
                    HashMap<String, String> postData = new HashMap<String, String>();
                    postData.put("image", encodedImage);


                    PostResponseAsyncTask task = new PostResponseAsyncTask(MainActivity.this, postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                         //   Log.d("RESPUESTA PHP",s);

                            if(s.contains("upload_success")){
                                Toast.makeText(getApplicationContext(),"ENVIADO CON EXITO", Toast.LENGTH_SHORT).show();

                                try {
                                    enviar();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }else{

                                //TODO
                                //Change text, error por exito
                                Toast.makeText(getApplicationContext(),
                                        "Imagen no enviada", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });

                    task.execute("http://yamgo.com.co/adda/connection.php");
                    progressDialog.setMessage("Enviando tu denuncia, por favor espera, puede tardar unos segundos...");
                    progressDialog.show();
                    task.setEachExceptionsHandler(new EachExceptionsHandler() {
                        @Override
                        public void handleIOException(IOException e) {
                            Toast.makeText(getApplicationContext(),
                                    "ERROR CONECTANDO", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleMalformedURLException(MalformedURLException e) {
                            Toast.makeText(getApplicationContext(),
                                    "URL ERROR", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleProtocolException(ProtocolException e) {
                            Toast.makeText(getApplicationContext(),
                                    "ERROR PROTOCOLO", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                            Toast.makeText(getApplicationContext(),
                                    "ENCODING ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "SALIO ALGO MAL CODIFICANDO LAS FOTOS", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void enviar() throws ExecutionException, InterruptedException {
      //  gps g = new gps();
        String method = "enviar";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method,nombre.getText().toString(),telefono.getText().toString(),comentario.getText().toString(),"22","44");
        //String e = backgroundTask.get();


    }

    /*---------- LLAMADAS ------------*/

    private void explicarUsoPermisoLlamada() {
        //Este IF es necesario para saber si el usuario ha marcado o no la casilla [] No volver a preguntar
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            Toast.makeText(this, "Se necesita permiso para realizar llamadas de emergencia", Toast.LENGTH_SHORT).show();
            //Explicarle al usuario porque necesitas el permiso (Opcional)
            alertDialogLlamada();
        }
    }

    private void solicitarPermisoHacerLlamada() {
        //Pedimos el permiso o los permisos con un cuadro de dialogo del sistema
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CALL_PHONE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },SOLICITUD_PERMISO_GPS);
        //   Toast.makeText(this, "Pedimos el permiso con un cuadro de dialogo del sistema", Toast.LENGTH_SHORT).show();
    }

    /*---------- CAMARA ------------*/

    private void explicarUsoPermisoCamara() {
        //Este IF es necesario para saber si el usuario ha marcado o no la casilla [] No volver a preguntar
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Toast.makeText(this, "Se necesita permiso para capturar fotografías", Toast.LENGTH_SHORT).show();
            //Explicarle al usuario porque necesitas el permiso (Opcional)
            alertDialogCamara();
        }
    }



    /*---------- STORAGE WRITE ------------*/

    private void explicarUsoPermisoStogareWrite() {
        //Este IF es necesario para saber si el usuario ha marcado o no la casilla [] No volver a preguntar
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Se necesita permiso para guardar", Toast.LENGTH_SHORT).show();
            //Explicarle al usuario porque necesitas el permiso (Opcional)
            alertDialogWriteStorage();
        }
    }


    /*---------- STORAGE READ ------------*/

    private void explicarUsoPermisoStogareRead() {
        //Este IF es necesario para saber si el usuario ha marcado o no la casilla [] No volver a preguntar
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Se necesita permiso para guardar", Toast.LENGTH_SHORT).show();
            //Explicarle al usuario porque necesitas el permiso (Opcional)
            alertDialogReadStorage();
        }
    }

    /*---------- STORAGE READ ------------*/

    private void explicarUsoPermisoGPSFine() {
        //Este IF es necesario para saber si el usuario ha marcado o no la casilla [] No volver a preguntar
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "Se necesita permiso para usar GPS", Toast.LENGTH_SHORT).show();
            //Explicarle al usuario porque necesitas el permiso (Opcional)
            alertDialogGPS();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /**
         * Si tubieramos diferentes permisos solicitando permisos de la aplicacion, aqui habria varios IF
         */
        if (requestCode == SOLICITUD_PERMISO_CALL_PHONE) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Realizamos la accion
                //  startActivity(intentLLamada);
                //    Toast.makeText(this, "Permiso Concedido", Toast.LENGTH_SHORT).show();
            } else {
                //1-Seguimos el proceso de ejecucion sin esta accion: Esto lo recomienda Google
                //2-Cancelamos el proceso actual
                //3-Salimos de la aplicacion
                //    Toast.makeText(this, "Permiso No Concedido", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == SOLICITUD_PERMISO_CAMARA) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Realizamos la accion
                //  startActivity(intentLLamada);
                //    Toast.makeText(this, "Permiso Concedido", Toast.LENGTH_SHORT).show();
            } else {
                //1-Seguimos el proceso de ejecucion sin esta accion: Esto lo recomienda Google
                //2-Cancelamos el proceso actual
                //3-Salimos de la aplicacion
                //    Toast.makeText(this, "Permiso No Concedido", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == SOLICITUD_PERMISO_STORAGE_READ) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Realizamos la accion
                //  startActivity(intentLLamada);
                //    Toast.makeText(this, "Permiso Concedido", Toast.LENGTH_SHORT).show();
            } else {
                //1-Seguimos el proceso de ejecucion sin esta accion: Esto lo recomienda Google
                //2-Cancelamos el proceso actual
                //3-Salimos de la aplicacion
                //    Toast.makeText(this, "Permiso No Concedido", Toast.LENGTH_SHORT).show();
            }
        }


        if (requestCode == SOLICITUD_PERMISO_STORAGE_WRITE) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Realizamos la accion
                //  startActivity(intentLLamada);
                //    Toast.makeText(this, "Permiso Concedido", Toast.LENGTH_SHORT).show();
            } else {
                //1-Seguimos el proceso de ejecucion sin esta accion: Esto lo recomienda Google
                //2-Cancelamos el proceso actual
                //3-Salimos de la aplicacion
                //    Toast.makeText(this, "Permiso No Concedido", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == SOLICITUD_PERMISO_GPS) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Realizamos la accion
                //  startActivity(intentLLamada);
                //    Toast.makeText(this, "Permiso Concedido", Toast.LENGTH_SHORT).show();
            } else {
                //1-Seguimos el proceso de ejecucion sin esta accion: Esto lo recomienda Google
                //2-Cancelamos el proceso actual
                //3-Salimos de la aplicacion
                //    Toast.makeText(this, "Permiso No Concedido", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void alertDialogLlamada() {
        // 1. Instancia de AlertDialog.Builder con este constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 2. Encadenar varios métodos setter para ajustar las características del diálogo
        builder.setMessage("Permiso para realizar llamadas concedido");
        builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
    }

    public void alertDialogCamara() {
        // 1. Instancia de AlertDialog.Builder con este constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 2. Encadenar varios métodos setter para ajustar las características del diálogo
        builder.setMessage("Permiso para realizar capturar fotografias");
        builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
    }


    public void alertDialogReadStorage() {
        // 1. Instancia de AlertDialog.Builder con este constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 2. Encadenar varios métodos setter para ajustar las características del diálogo
        builder.setMessage("Permiso para leer datos de almacenamiento");
        builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
    }

    public void alertDialogWriteStorage() {
        // 1. Instancia de AlertDialog.Builder con este constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 2. Encadenar varios métodos setter para ajustar las características del diálogo
        builder.setMessage("Permiso para almacenar datos");
        builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
    }

    public void alertDialogGPS() {
        // 1. Instancia de AlertDialog.Builder con este constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 2. Encadenar varios métodos setter para ajustar las características del diálogo
        builder.setMessage("Permiso para user GPS");
        builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_REQUEST){
                String photoPath = cameraPhoto.getPhotoPath();
                selectedPhoto = photoPath;
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    ivImage.setImageBitmap(bitmap);
                    //ivImage.setImageBitmap(getRotateBitmap(bitmap, 90));
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "SALIO ALGO MAL SUBIENDO LAS FOTOS", Toast.LENGTH_SHORT).show();
                }

            }

            else if(requestCode == GALLERY_REQUEST){
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                selectedPhoto = photoPath;
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    ivImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "SALIO ALGO MAL ESCOGIENDO LAS FOTOS", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    /*
    private Bitmap getRotateBitmap(Bitmap source, int angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap bitmap1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        return bitmap1;
    }*/
}