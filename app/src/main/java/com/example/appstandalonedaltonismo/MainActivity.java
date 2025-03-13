package com.example.appstandalonedaltonismo;

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
import java.util.Collections;
import java.util.Random;

import android.content.Intent;
import java.util.HashMap;

import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private View colorView;
    private Button btn1, btn2, btn3, btn4, btnNext, btnResultados;
    private ArrayList<String> respuestasUsuario = new ArrayList<>();
    private String colorCorrecto;
    private int correctAnswers  = 0;

    private int ultimoIndiceColor = -1; // Evita repetir el mismo color consecutivamente

    private final String[] coloresHex = {
            "#FF0000", "#00FF00", "#0000FF", "#FFFF00",
            "#FFA500", "#00CED1", "#8A2BE2", "#FFC0CB",
            "#808080", "#A52A2A", "#000000", "#FFFFFF"
    };

    private final String[] nombresColores = {
            "Rojo", "Verde", "Azul", "Amarillo",
            "Naranja", "Celeste", "Violeta", "Rosa",
            "Gris", "Marrón", "Negro", "Blanco"
    };

    // Analisis real de los errores
    private int respuestasCorrectas = 0;
    private HashMap<String, Integer> errorMap = new HashMap<>();


    private void registrarError(String correcto, String respuesta) {
        String claveError = identificarTipoError(correcto, respuesta);
        if (claveError != null) {
            int valor = errorMap.containsKey(claveError) ? errorMap.get(claveError) : 0;
            errorMap.put(claveError, valor + 1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "MainActivity se ha iniciado correctamente");

        // Inicializar vistas
        colorView = findViewById(R.id.colorView);
        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);
        btn3 = findViewById(R.id.btn_3);
        btn4 = findViewById(R.id.btn_4);
        btnNext = findViewById(R.id.btn_next);
        btnResultados = findViewById(R.id.btn_resultados); // Ahora btnResultados está declarado

        // Cargar primera pregunta
        cargarNuevaPregunta();

        // Listener para los botones de opción
        View.OnClickListener optionClickListener = v -> {
            Button selectedButton = (Button) v;
            String respuesta = selectedButton.getText().toString();
            respuestasUsuario.add(respuesta);

            if (respuesta.equals(colorCorrecto)) {
                Toast.makeText(MainActivity.this, "Correcto", Toast.LENGTH_SHORT).show();
                correctAnswers++; // Aquí usamos correctAnswers, que es la oficial
            } else {
                Toast.makeText(MainActivity.this, "Incorrecto, era: " + colorCorrecto, Toast.LENGTH_SHORT).show();
                registrarError(colorCorrecto, respuesta);
            }
        };

        // Asignar listener a los botones de opciones
        btn1.setOnClickListener(optionClickListener);
        btn2.setOnClickListener(optionClickListener);
        btn3.setOnClickListener(optionClickListener);
        btn4.setOnClickListener(optionClickListener);
        // Listener para el botón "Siguiente"
        btnNext.setOnClickListener(v -> cargarNuevaPregunta());

        // Listener para el botón "Resultados"
        btnResultados.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
            intent.putExtra("correctAnswers", correctAnswers);
            intent.putExtra("totalQuestions", respuestasUsuario.size());
            intent.putExtra("errorMap", errorMap);
            startActivity(intent);
            finish();
    });
}



    private void cargarNuevaPregunta() {
        // Seleccionar un color aleatorio sin repetir el último
        Random random = new Random();
        int indiceColor;
        do {
            indiceColor = random.nextInt(coloresHex.length);
        } while (indiceColor == ultimoIndiceColor);

        ultimoIndiceColor = indiceColor;
        colorCorrecto = nombresColores[indiceColor];

        // Cambiar el color de la vista
        colorView.setBackgroundColor(Color.parseColor(coloresHex[indiceColor]));

        // Obtener opciones aleatorias sin repetir
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add(colorCorrecto);
        while (opciones.size() < 4) {
            String opcion = nombresColores[random.nextInt(nombresColores.length)];
            if (!opciones.contains(opcion)) {
                opciones.add(opcion);
            }
        }

        // Para que el orden de las opciones no sea siempre igual
        Collections.shuffle(opciones);

        // Mezclar opciones
        java.util.Collections.shuffle(opciones);

        // Asignar opciones a los botones
        btn1.setText(opciones.get(0));
        btn2.setText(opciones.get(1));
        btn3.setText(opciones.get(2));
        btn4.setText(opciones.get(3));
    }


    private String identificarTipoError(String correcto, String respondido) {
        correcto = correcto.toLowerCase();
        respondido = respondido.toLowerCase();

        if ((correcto.equals("rojo") && respondido.equals("verde")) ||
                (correcto.equals("verde") && respondido.equals("rojo"))) {
            return "rojo-verde";
        }
        if ((correcto.equals("amarillo") && respondido.equals("azul")) ||
                (correcto.equals("azul") && respondido.equals("amarillo"))) {
            return "azul-amarillo";
        }
        if ((correcto.equals("rojo") && respondido.equals("marrón")) ||
                (correcto.equals("marrón") && respondido.equals("rojo"))) {
            return "rojo-marron";
        }
        if ((correcto.equals("amarillo") && respondido.equals("marrón")) ||
                (correcto.equals("marrón") && respondido.equals("amarillo"))) {
            return "amarillo-marron";
        }
        // Patrón general si no se puede clasificar
        return "general";
    }

}