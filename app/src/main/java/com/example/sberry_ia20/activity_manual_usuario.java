package com.example.sberry_ia20;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_manual_usuario extends AppCompatActivity {
Button btnRegresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Set the content view to the custom layout


        setContentView(R.layout.activity_manual_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnRegresar = findViewById(R.id.button_regresar_main);
        WebView webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient()); // To handle page navigation within the WebView
        webView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript if needed
        webView.loadUrl("https://sberry-ia.blogspot.com/2024/09/manual-para-utilizar-sberry-ia.html");


btnRegresar.setOnClickListener(v -> {
    startActivity(new Intent(this, MainActivity.class));
    finish();
});

    }
}