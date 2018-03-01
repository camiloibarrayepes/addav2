package com.example.camiloandresibarrayepes.adda_app

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Button_denuncia.setOnClickListener{
            val intent = Intent(this, denuncias::class.java)
            startActivity(intent)
        }

        button_info.setOnClickListener{
            val intent = Intent(this, info_adda::class.java)
            startActivity(intent)
        }

        button_llamada.setOnClickListener{
            val intent = Intent(this, llamada_emergencia::class.java)
            startActivity(intent)
        }
    }
}
