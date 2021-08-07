package edu.pdx.cs410J.heathhan;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;


import androidx.navigation.ui.AppBarConfiguration;


import edu.pdx.cs410J.heathhan.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private final Map<String, AppointmentBook> books = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Appointment appointment = new Appointment("Learn Java", "08/11/2021", "12:00", "PM", "08/11/2021", "1:30", "PM");
                Snackbar.make(view, appointment.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //OnClick Create AppointmentBook
        Button launchApptBook = findViewById(R.id.create);
        launchApptBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Make new ApptBook
                AppointmentBook book = new AppointmentBook();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_readme) {
            try {
                readme();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readme() throws IOException {
        File file = getReadme();
        if (!file.exists()) {
            return;
        }

        try (
                BufferedReader br = new BufferedReader(new FileReader(file))
        ) {
            String line = null;
            while ((line = br.readLine()) != null) {
                //display line in a list?
            }

        }
    }

    @NonNull
    private File getReadme() {
        File contextDirectory = getApplicationContext().getDataDir();
        File sumsFile = new File(contextDirectory, "README.txt");
        return sumsFile;
    }


}