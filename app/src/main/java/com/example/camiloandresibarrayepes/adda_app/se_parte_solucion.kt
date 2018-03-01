package com.example.camiloandresibarrayepes.adda_app

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_se_parte_solucion.*

class se_parte_solucion : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_se_parte_solucion)


        button_registro.setOnClickListener{
            val intent = Intent(this, notificaciones_activar::class.java)
            startActivity(intent)
        }

    }


}
