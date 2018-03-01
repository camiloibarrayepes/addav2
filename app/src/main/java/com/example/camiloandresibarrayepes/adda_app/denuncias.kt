package com.example.camiloandresibarrayepes.adda_app

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_denuncias.*

class denuncias : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_denuncias)

        button_enviar_denuncia.setOnClickListener{
            val intent = Intent(this, denuncia_gracias::class.java)
            startActivity(intent)
        }
    }
}
