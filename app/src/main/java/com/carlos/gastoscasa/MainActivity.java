package com.carlos.gastoscasa;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web = new WebView(this);
        setContentView(web);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient());
        web.addJavascriptInterface(new Puente(), "Android");
        web.loadUrl("file:///android_asset/index.html");
    }

    private void enviar(String texto, boolean soloWhatsApp) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, texto);
        if (soloWhatsApp) {
            String[] paquetes = {"com.whatsapp", "com.whatsapp.w4b"};
            for (String p : paquetes) {
                try {
                    Intent w = new Intent(i);
                    w.setPackage(p);
                    startActivity(w);
                    return;
                } catch (ActivityNotFoundException ignored) { }
            }
        }
        startActivity(Intent.createChooser(i, "Enviar con"));
    }

    class Puente {
        @JavascriptInterface
        public void compartirWhatsApp(final String texto) {
            runOnUiThread(() -> enviar(texto, true));
        }

        @JavascriptInterface
        public void compartirTexto(final String texto) {
            runOnUiThread(() -> enviar(texto, false));
        }
    }

    @Override
    public void onBackPressed() {
        web.evaluateJavascript(
            "(function(){var m=document.getElementById('modal');if(m.style.display==='flex'){cerrarModal();return 1}" +
            "if(document.getElementById('v-hoy').style.display==='none'){ver('hoy');return 1}return 0})()",
            r -> { if ("0".equals(r)) MainActivity.super.onBackPressed(); });
    }
}
