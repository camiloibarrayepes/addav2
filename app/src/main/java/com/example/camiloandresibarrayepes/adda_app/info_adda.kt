package com.example.camiloandresibarrayepes.adda_app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.net.Uri
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_info_adda.*


class info_adda : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_adda)

        val vw:WebView = findViewById(R.id.wiew)
        //Enable javascript
        wiew.getSettings().setJavaScriptEnabled(true)
        wiew.setFocusable(true)
        wiew.setFocusableInTouchMode(true)
        //Set render priority to HIGH
        wiew.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH)
        wiew.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE)
        wiew.getSettings().setDomStorageEnabled(true)
        wiew.getSettings().setDatabaseEnabled(true)
        wiew.getSettings().setAppCacheEnabled(true)
        wiew.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY)

        //load url
        wiew.loadUrl("http:www.yamgo.com.co/adda/ver.php")
        wiew.setWebViewClient(WebViewClient())
    }
}
