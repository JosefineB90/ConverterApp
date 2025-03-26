package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText inputValue;
    Spinner fromSpinner, toSpinner;
    Button convertButton;
    TextView resultText;

    String[] units = {
            "inch", "foot", "yard", "mile", "cm", "km",
            "pound", "ounce", "ton", "g", "kg",
            "celsius", "fahrenheit", "kelvin"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputValue = findViewById(R.id.inputValue);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        convertButton = findViewById(R.id.convertButton);
        resultText = findViewById(R.id.resultText);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onClick(View v) {
                try {
                    double value = Double.parseDouble(inputValue.getText().toString());
                    String fromUnit = fromSpinner.getSelectedItem().toString();
                    String toUnit = toSpinner.getSelectedItem().toString();

                    double result = convert(value, fromUnit, toUnit);
                    resultText.setText(String.format("%.4f %s", result, toUnit));
                } catch (NumberFormatException e) {
                    resultText.setText("Please enter a valid number.");
                } catch (IllegalArgumentException e) {
                    resultText.setText("Unsupported conversion.");
                }
            }
        });
    }

    public double convert(double value, String fromUnit, String toUnit) {
        fromUnit = fromUnit.toLowerCase();
        toUnit = toUnit.toLowerCase();

        Map<String, Double> lengthToCm = new HashMap<>();
        lengthToCm.put("inch", 2.54);
        lengthToCm.put("foot", 30.48);
        lengthToCm.put("yard", 91.44);
        lengthToCm.put("mile", 160934.0);
        lengthToCm.put("cm", 1.0);
        lengthToCm.put("km", 100000.0);

        Map<String, Double> weightToGram = new HashMap<>();
        weightToGram.put("pound", 453.592);
        weightToGram.put("ounce", 28.3495);
        weightToGram.put("ton", 907185.0);
        weightToGram.put("g", 1.0);
        weightToGram.put("kg", 1000.0);

        if (lengthToCm.containsKey(fromUnit) && lengthToCm.containsKey(toUnit)) {
            double cm = value * lengthToCm.get(fromUnit);
            return cm / lengthToCm.get(toUnit);
        }

        if (weightToGram.containsKey(fromUnit) && weightToGram.containsKey(toUnit)) {
            double grams = value * weightToGram.get(fromUnit);
            return grams / weightToGram.get(toUnit);
        }

        switch (fromUnit + "_to_" + toUnit) {
            case "celsius_to_fahrenheit":
                return (value * 1.8) + 32;
            case "fahrenheit_to_celsius":
                return (value - 32) / 1.8;
            case "celsius_to_kelvin":
                return value + 273.15;
            case "kelvin_to_celsius":
                return value - 273.15;
        }

        throw new IllegalArgumentException("Unsupported conversion: " + fromUnit + " to " + toUnit);
    }
}
