package edu.pdx.cs410J.heathhan;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import android.view.View;


//import androidx.navigation.ui.AppBarConfiguration;


import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.heathhan.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class for the Main Activity/Entry Point
 */
public class MainActivity extends AppCompatActivity {

    public static final int GET_NEW_APPT = 42;
    private String owner = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //private AppBarConfiguration appBarConfiguration;
        edu.pdx.cs410J.heathhan.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


        //OnClick Create AppointmentBook
        Button launchApptBook = findViewById(R.id.create_apptbook);
        launchApptBook.setOnClickListener(view -> {
            owner = ownerToString(getOwnerInput());

            if(ownerRequired(getOwnerInput(), owner)) {
                createAppointmentBookFile(owner);
                String message = "AppointmentBook Created for: " + owner;
                toast(message);
            }

        });

        //onClick Add Appointment
        Button launchAddAppt = findViewById(R.id.add_appt);
        launchAddAppt.setOnClickListener(view -> {
            owner = ownerToString(getOwnerInput());

            if(ownerRequired(getOwnerInput(), owner)) {
                Intent intent = new Intent(MainActivity.this, AddApptActivity.class);
                startActivityForResult(intent, GET_NEW_APPT);
            }
        });

        //onClick ViewAll Appointments
        Button launchViewAll = findViewById(R.id.view_all);
        launchViewAll.setOnClickListener(view -> {
            owner = ownerToString(getOwnerInput());
            AppointmentBook book = new AppointmentBook(owner);

            if(ownerRequired(getOwnerInput(), owner)) {
                File file = getFile(owner);

                if (file.exists()) {
                    TextParser parser = null;
                    try {
                        parser = new TextParser(new FileReader(file));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        assert parser != null;
                        book = parser.parse();
                    } catch (ParserException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(MainActivity.this, ViewAllActivity.class);
                    intent.putExtra("appointmentBook", book);
                    startActivity(intent);
                }
            } else {
                toast("No AppointmentBook for Owner: " + owner);
            }

            //onClick Search Appointments
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
        return ownerInput.getText().toString();
    }

    private EditText getOwnerInput(){
        return findViewById(R.id.owner_input);
    }

    private Boolean ownerRequired(EditText ownerInput, String owner){
        if(owner.trim().equals("")) {
            ownerInput.setError("Owner Name is Required");
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

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Context context = this;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_readme) {
            // custom dialog
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom);
            dialog.setTitle("README");

            TextView text = dialog.findViewById(R.id.readme);
            text.setText("README\n\n " +
                    "Hannah Heath - heathhan@pdx.edu \n" +
                    "Project 5\n\n\n " +
                    "Enter the appointment book's owner that you want to work with.\n\n" +
                    "Options include: Create AppointmentBook, Add Appointment, View All Appointments, and Search Appointments\n\n" +
                    "Create AppointmentBook - creates a new AppointmentBook for the owner\n\n" +
                    "Add Appointment - creates a new appointment for the given owner\n\n" +
                    "View All Appointments - shows all appointments for a given owner\n\n" +
                    "Search Appointments - searches a given owners appointments that fall between two dates and times.\n");

            dialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
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

    /**
     * Method to get the owner's file
     *
     * @param owner -
     *      The name of the AppointmentBook owner
     *
     * @return - File
     *      The owner's file, based on their name
     */
    @NonNull
    private File getFile(String owner){
        String str = replaceSpace(owner);
        str = str + ".txt";

        File contextDirectory = getApplicationContext().getDataDir();

        return new File(contextDirectory, str);
    }

    /**
     * Creates a file with the owners name and writes the owner to it.
     *
     * @param owner
     *      The name of the AppointmentBook owner
     */
    @NonNull
    private File createAppointmentBookFile(String owner){
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

        return file;

    }

}