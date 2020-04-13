package com.example.lab2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class Lab2Activity extends AppCompatActivity {

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab2Activity.class);
    }

    private static final String STATE_VIEWS_COUNT = "views_count";

    private Lab2ViewsContainer lab2ViewsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab2);

        EditText inputName = findViewById(R.id.input_name);
        EditText inputRating = findViewById(R.id.input_rate);
        lab2ViewsContainer = findViewById(R.id.container);
        findViewById(R.id.btn_add_view).setOnClickListener(view -> {
            String name = inputName.getText().toString();
            String rating = inputRating.getText().toString();
            if (name.length() > 0 && rating.length() > 0)
            {
                lab2ViewsContainer.addRatingLine(name, Double.valueOf(rating));
            }
            else{
                Toast.makeText(this, "Введите наименование и рейтинг!", Toast.LENGTH_LONG).show();
            }

        });

        if (savedInstanceState != null) {
            lab2ViewsContainer.setRatingLines(savedInstanceState.getParcelableArrayList(STATE_VIEWS_COUNT));
        }
        setTitle(getString(R.string.lab2_title, getClass().getSimpleName()));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_VIEWS_COUNT, lab2ViewsContainer.getRatingLines());
    }
}
