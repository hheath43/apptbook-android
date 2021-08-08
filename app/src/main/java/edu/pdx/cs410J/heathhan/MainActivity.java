package edu.pdx.cs410J.heathhan;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;


import androidx.navigation.ui.AppBarConfiguration;


import edu.pdx.cs410J.heathhan.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private final Map<String, AppointmentBook> books = new HashMap<>();
    public static final int GET_NEW_APPT = 42;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        //DELETE AND XML WITH THIS
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Appointment appointment = new Appointment("Learn Java", "08/11/2021", "12:00",  "PM",  "08/11/2021", "1:30", "PM");
                Snackbar.make(view, appointment.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //OnClick Create AppointmentBook
        Button launchApptBook = findViewById(R.id.create_apptbook);
        launchApptBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Make new ApptBook
                String owner = ownerToString(getOwnerInput());

                if(ownerRequired(getOwnerInput(), owner)) {
                    AppointmentBook book = new AppointmentBook(owner);
                    books.put(owner, book);
                    String message = "AppointmentBook Created for: " + owner;
                    toast(message);
                }

                //Create new file
                createAppointmentBookFile(owner);

            }
        });

        //onClick Add Appointment
        Button launchAddAppt = findViewById(R.id.add_appt);
        launchAddAppt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if ApptBook already exists (Map?, but most likely check file exists)?

                Intent intent = new Intent(MainActivity.this, AddApptActivity.class);
                startActivityForResult(intent, GET_NEW_APPT);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GET_NEW_APPT && data != null) {
            String apptStr;
            apptStr = data.getStringExtra(AddApptActivity.EXTRA_APPT);


        }
    }

    /**
     * Method to get the owner input and convert to a String
     *
     * @return - String
     *      Owner input returned as String
     */
    private String ownerToString(EditText ownerInput){
        String owner = ownerInput.getText().toString();
        return owner;
    }

    private EditText getOwnerInput(){
        EditText ownerInput = findViewById(R.id.owner_input);
        return ownerInput;
    }

    private Boolean ownerRequired(EditText ownerInput, String owner){
        if(owner.trim().equals("")) {
            ownerInput.setHint("Owner Name is Required");
            return false;
        }
        return true;
    }

    private void toast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
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
        //createAppointmentBookFile("Hannah Heath");
        if(checkFileExists("Hannah Heath")){
            toast("YES");
        }
        else{
            toast("NO");
        }
       /* File file = getReadme();
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

        }*/
    }

    @NonNull
    private File getReadme() {
        File contextDirectory = getApplicationContext().getDataDir();
        File readMeFile = new File(contextDirectory, "README.txt");
        return readMeFile;
    }


    private String replaceSpace(String str){
        str = str.replace(" ", "_");
        return str;
    }

    private Boolean checkFileExists(String owner){
        String str = replaceSpace(owner);
        str = str + ".txt";
        File contextDirectory = getApplicationContext().getDataDir();
        File file = new File(contextDirectory, "Hannah_Heath.txt");

        return file.exists();
    }

    /**
     * Creates a file with the owners name and writes the owner to it.
     *
     * @param owner
     */
    private void createAppointmentBookFile(String owner){
        String str = replaceSpace(owner);
        str = str + ".txt";
        System.out.println(str);

        File contextDirectory = getApplicationContext().getDataDir();
        File file = new File(contextDirectory, str);

        try (
                PrintWriter pw = new PrintWriter(new FileWriter(file))
        ) {

            pw.println(owner);

            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}