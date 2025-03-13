package com.example.appstandalonedaltonismo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;


import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Random;

import android.content.Intent;
import java.util.HashMap;

import android.util.Log;

public class StartActivity extends AppCompatActivity {


    private Button btnStartTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnStartTest = findViewById(R.id.btn_start); // Ahora btnStartTest está declarado

        // Listener para el botón "Resultados"

        btnStartTest.setOnClickListener(v -> {
            Intent intent = new Intent(
                    StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        Log.d("Mensaje", "Inicio");
         });

}

}
