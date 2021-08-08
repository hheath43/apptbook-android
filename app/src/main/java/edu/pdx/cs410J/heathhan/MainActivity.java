package edu.pdx.cs410J.heathhan;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;


import androidx.navigation.ui.AppBarConfiguration;


import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.heathhan.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    public static final int GET_NEW_APPT = 42;
    private String owner = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        //DELETE AND XML WITH THIS
        /*binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Appointment appointment = new Appointment("Learn Java", "08/11/2021", "12:00",  "PM",  "08/11/2021", "1:30", "PM");
                Snackbar.make(view, appointment.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //OnClick Create AppointmentBook
        Button launchApptBook = findViewById(R.id.create_apptbook);
        launchApptBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                owner = ownerToString(getOwnerInput());

                if(ownerRequired(getOwnerInput(), owner)) {
                    createAppointmentBookFile(owner);
                    String message = "AppointmentBook Created for: " + owner;
                    toast(message);
                }

            }
        });

        //onClick Add Appointment
        Button launchAddAppt = findViewById(R.id.add_appt);
        launchAddAppt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                owner = ownerToString(getOwnerInput());

                if(ownerRequired(getOwnerInput(), owner)) {
                    Intent intent = new Intent(MainActivity.this, AddApptActivity.class);
                    startActivityForResult(intent, GET_NEW_APPT);
                }
            }
        });

        //onClick ViewAll Appointments
        Button launchViewAll = findViewById(R.id.view_all);
        launchViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                owner = ownerToString(getOwnerInput());
                AppointmentBook book = new AppointmentBook(owner);

                if(ownerRequired(getOwnerInput(), owner)) {
                    File file = getFile(owner);
                    TextParser parser = null;
                    try {
                        parser = new TextParser(new FileReader(file));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                       book = parser.parse();
                    } catch (ParserException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(MainActivity.this, ViewAllActivity.class);
                    intent.putExtra("appointmentBook", book);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextDumper dumper;
        TextParser parser;
        AppointmentBook book;

        if (resultCode == RESULT_OK && requestCode == GET_NEW_APPT && data != null) {
            Appointment appt = (Appointment) data.getSerializableExtra(AddApptActivity.EXTRA_APPT);
            toast("Appointment Added: " + appt);
            // check file exists
            //      if it does -> parse file to apptbook object, add appt, then Textdump to file

            if(checkFileExists(owner)){
                File file = getFile(owner);
                try {
                    parser = new TextParser(new FileReader(file));
                    book = parser.parse();
                    book.addAppointment(appt);
                    dumper = new TextDumper(new FileWriter(file));
                    dumper.dump(book);

                } catch (ParserException | IOException e) {
                    e.printStackTrace();
                }

            } else {
                File file = createAppointmentBookFile(owner);
                book = new AppointmentBook(owner);
                book.addAppointment(appt);
                System.out.println(book);

                try {
                    dumper = new TextDumper(new FileWriter(file));
                    dumper.dump(book);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


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
            ownerInput.setError("Owner Name is Required");
            //ownerInput.setHint("Owner Name is Required");
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
        /*if(checkFileExists("Hannah Heath")){
            toast("YES");
        }
        else{
            toast("NO");
        }*/
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
        File file = new File(contextDirectory, str);

        return file.exists();
    }

    private File getFile(String owner){
        String str = replaceSpace(owner);
        str = str + ".txt";

        File contextDirectory = getApplicationContext().getDataDir();
        File file = new File(contextDirectory, str);

        return file;
    }

    /**
     * Creates a file with the owners name and writes the owner to it.
     *
     * @param owner
     */
    private File createAppointmentBookFile(String owner){
        String str = replaceSpace(owner);
        str = str + ".txt";
        System.out.println(str);

        File contextDirectory = getApplicationContext().getDataDir();
        File file = new File(contextDirectory, str);

        //REPLACE WITH TEXTDUMPER?
        try (
                PrintWriter pw = new PrintWriter(new FileWriter(file))
        ) {

            pw.println(owner);

            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;

    }

}