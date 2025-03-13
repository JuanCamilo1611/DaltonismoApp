package com.example.appstandalonedaltonismo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.util.HashMap;

public class ResultsActivity extends AppCompatActivity {

    // Constantes para los tipos de error
    private static final String ROJO_VERDE = "rojo-verde";
    private static final String VERDE_AMARILLO = "verde-amarillo";
    private static final String AZUL_AMARILLO = "azul-amarillo";
    private static final String VERDE_ROJO = "verde-rojo";
    private static final String ROJO_MARRON = "rojo-marron";
    private static final String AMARILLO_MARRON = "amarillo-marron";
    private static final String AZUL_VIOLETA = "azul-violeta";
    private static final String VIOLETA_AMARILLO = "violeta-amarillo"; // corregido el typo "vieoleta"
    private static final String VIOLETA_BLANCO = "violeta-blanco";
    private static final String VIOLETA_GRIS = "violeta-gris";
    private static final String AMARILLO_GRIS = "amarillo-gris";
    private static final String GENERAL = "general";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // UI Elements
        TextView txtScore = findViewById(R.id.txt_score);
        TextView txtAnalysis = findViewById(R.id.txt_analysis);
        Button btnRestart = findViewById(R.id.btn_restart);
        ImageView logoGif = findViewById(R.id.image_logo);
        Glide.with(this).asGif().load(R.drawable.logoapp).into(logoGif);

        // Recibir datos de la prueba
        int correctAnswers = getIntent().getIntExtra("correctAnswers", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 1);
        HashMap<String, Integer> errorMap = (HashMap<String, Integer>) getIntent().getSerializableExtra("errorMap");

        // Calcular y mostrar puntaje
        int score = (correctAnswers * 100) / totalQuestions;
        txtScore.setText(getString(R.string.score_text, score));

        // Analizar errores y mostrar diagnóstico
        txtAnalysis.setText(analyzeErrors(errorMap));

        // Reiniciar prueba
        btnRestart.setOnClickListener(v -> {
            startActivity(new Intent(ResultsActivity.this, MainActivity.class));
            finish();
        });
    }
    private String analyzeErrors(HashMap<String, Integer> errorMap) {
        StringBuilder analysis = new StringBuilder();

        // Obtener valores (con 0 por defecto si no existen)
        int general = getErrorCount(errorMap, GENERAL);
        int rojoMarron = getErrorCount(errorMap, ROJO_MARRON);
        int amarilloMarron = getErrorCount(errorMap, AMARILLO_MARRON);
        int rojoVerde = getErrorCount(errorMap, ROJO_VERDE);
        int azulAmarillo = getErrorCount(errorMap, AZUL_AMARILLO);

        // Umbrales definidos
        final int UMBRAL_GENERAL = 5;
        final int UMBRAL_ROJO_VERDE = 2;
        final int UMBRAL_AZUL_AMARILLO = 2;
        final int UMBRAL_ROJO_MARRON = 2;
        final int UMBRAL_AMARILLO_MARRON = 2;

        if (general >= UMBRAL_GENERAL) {
            analysis.append("📊 Tipo de confusión: No distingue ningún color\n\n")
                    .append("🔍 Posible daltonismo: Acromatopsia\n")
                    .append("📌 Diagnóstico final: Daltonismo total\n");
        } else if (rojoMarron >= UMBRAL_ROJO_MARRON) {
            analysis.append("📊 Tipo de confusión: Confunde rojo con marrón (o tonos similares)\n\n")
                    .append("🔍 Posible daltonismo: Protanomalía o Protanopía\n")
                    .append("📌 Diagnóstico final: Déficit o ausencia de percepción del rojo\n");
        } else if (amarilloMarron >= UMBRAL_AMARILLO_MARRON) {
            analysis.append("📊 Tipo de confusión: Confunde verde con rojo o amarillo con marrón\n\n")
                    .append("🔍 Posible daltonismo: Deuteranomalía o Deuteranopía\n")
                    .append("📌 Diagnóstico final: Déficit o ausencia de percepción del verde\n");
        } else if (rojoVerde >= UMBRAL_ROJO_VERDE) {
            analysis.append("📊 Tipo de confusión: Confusión frecuente entre rojo y verde\n\n")
                    .append("🔍 Posible daltonismo: Protanopía o Deuteranopía leve\n")
                    .append("📌 Diagnóstico tentativo: Daltonismo rojo-verde\n");
        } else if (azulAmarillo >= UMBRAL_AZUL_AMARILLO) {
            analysis.append("📊 Tipo de confusión: Confunde azul con amarillo\n\n")
                    .append("🔍 Posible daltonismo: Tritanomalía o Tritanopía\n")
                    .append("📌 Diagnóstico tentativo: Daltonismo azul-amarillo\n");
        } else if (rojoVerde > 0 || azulAmarillo > 0 || rojoMarron > 0 || amarilloMarron > 0 || general > 0) {
            analysis.append("🔎 Se detectan errores de color, pero no suficientes para emitir un diagnóstico confiable.\n\n")
                    .append("📌 Recomendación: Repetir la prueba en otro momento o consultar a un especialista.\n\n");
        }

        if (analysis.length() == 0) {
            analysis.append("✅ No se detectaron patrones significativos de daltonismo.");
        }

        return analysis.toString();
    }


    private int getErrorCount(HashMap<String, Integer> errorMap, String key) {
        return errorMap != null ? errorMap.getOrDefault(key, 0) : 0;
    }
}
