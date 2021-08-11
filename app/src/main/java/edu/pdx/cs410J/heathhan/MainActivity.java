package edu.pdx.cs410J.heathhan;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



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
            String message;
            owner = ownerToString(getOwnerInput());

            if(ownerRequired(getOwnerInput(), owner)) {
                File file = getFile(owner);
                if(!file.exists()){
                    createAppointmentBookFile(owner);
                    message = "AppointmentBook Created for: " + owner;
                } else {
                    message = "AppointmentBook for: " + owner + " already exists";
                }

                toast(message);
                EditText clear = getOwnerInput();
                clear.getText().clear();
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

            EditText clear = getOwnerInput();
            clear.getText().clear();
        });

        //onClick ViewAll Appointments
        Button launchViewAll = findViewById(R.id.view_all);
        launchViewAll.setOnClickListener(view -> {
                    owner = ownerToString(getOwnerInput());
                    AppointmentBook book = getAppointmentBook(owner);

                    if (ownerRequired(getOwnerInput(), owner)) {
                        if (book != null) {
                            Intent intent = new Intent(MainActivity.this, ViewAllActivity.class);
                            intent.putExtra("appointmentBook", book);
                            startActivity(intent);

                        } else {
                            toast("No AppointmentBook for Owner: " + owner);
                        }
                    }

                    EditText clear = getOwnerInput();
                    clear.getText().clear();
                });

            //onClick Search Appointments
            Button launchSearchAppt = findViewById(R.id.search);
            launchSearchAppt.setOnClickListener(view -> {
                owner = ownerToString(getOwnerInput());
                AppointmentBook book1 = getAppointmentBook(owner);

                if (ownerRequired(getOwnerInput(), owner)) {
                    if (book1 != null) {
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        intent.putExtra("appointmentBook", book1);
                        startActivity(intent);

                    } else {
                        toast("No AppointmentBook for Owner: " + owner);
                    }
                }
                EditText clear = getOwnerInput();
                clear.getText().clear();
            });

    }


    /**
     * Method to grab the Appointment sent back from AddApptActivity
     *
     * @param requestCode - Code to verify correct Activity
     * @param resultCode - Code to check successful
     * @param data - Appointment
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextDumper dumper;
        TextParser parser;
        AppointmentBook book;



        if (resultCode == RESULT_OK && requestCode == GET_NEW_APPT && data != null) {
            Appointment appt = (Appointment) data.getSerializableExtra(AddApptActivity.EXTRA_APPT);

            if(appt == null){
                return;
            }


            if(checkFileExists(owner)){
                File file = getFile(owner);
                try {
                    parser = new TextParser(new FileReader(file));
                    book = parser.parse();
                    book.addAppointment(appt);
                    dumper = new TextDumper(new FileWriter(file));
                    dumper.dump(book);

                } catch (ParserException | IOException e) {
                    toast("Error while reading or writing file: " + e.getMessage());
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
                    toast("Error while writing file: " + e.getMessage());
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


    /**
     * Get the EditText input for Owner name
     *
     * @return - EditText
     *
     */
    private EditText getOwnerInput(){
        return findViewById(R.id.owner_input);
    }

    /**
     * Method to check owner field was inputted
     *
     * @param ownerInput - EditText to set error/hint if not
     * @param owner - String to check isn't empty
     *
     * @return - Boolean value
     */
    private Boolean ownerRequired(EditText ownerInput, String owner){
        if(owner.trim().equals("")) {
            ownerInput.setError("Owner Name is Required");
            ownerInput.setHint("Owner Name is Required");
            return false;
        }
        return true;
    }


    /**
     * Method to output small message to user
     *
     * @param message
     *      String to be outputted
     */
    private void toast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }


    /**
     * Method for onClick of the options menu
     *
     * @param menu - menu on main activity
     *
     * @return - boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * Custom Dialog info when clicked
     *
     * @param item - README item
     *
     * @return - Boolean
     *      True if Item clicked, False if not
     */
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
                    "Owner Name - enter the appointment book's owner that you want to work with.\n\n" +
                    "Create AppointmentBook - creates a new AppointmentBook for the owner\n\n" +
                    "Add Appointment - creates a new appointment for the given owner, must hit return button after add appointment\n\n" +
                    "View All Appointments - shows all appointments for a given owner\n\n" +
                    "Search Appointments - searches a given owners appointments that fall between two dates and times.\n\n" +
                    "** Click anywhere outside box to exit **\n\n");

            dialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Method to replace a string with '_'
     *
     * @param str
     *      String of the owner's name
     *
     * @return - String
     *      New String with no spaces in owner's name
     */
    private String replaceSpace(String str){
        str = str.replace(" ", "_");
        return str;
    }


    /**
     * Method to create the file name of the owner to check the file exists.
     *
     * @param owner
     *      String with the owner name input
     *
     * @return - Boolean
     *      True, if file exists, False if it doesn't
     */
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

        File contextDirectory = getApplicationContext().getDataDir();
        File file = new File(contextDirectory, str);

        try (
                PrintWriter pw = new PrintWriter(new FileWriter(file))
        ) {

            pw.println(owner);

            pw.flush();
        } catch (IOException e) {
            toast("Error while writing file: " + e.getMessage());
        }

        return file;

    }


    /**
     * Method to parse the owners file to an AppointmentBook
     *
     * @param owner
     *         String for the owner working with
     *
     * @return - AppointmentBook
     *      New AppointmentBook holding owner's information
     */
    private AppointmentBook getAppointmentBook(String owner) {
        AppointmentBook book = new AppointmentBook(owner);

        File file = getFile(owner);

        if (file.exists()) {
            TextParser parser = null;
            try {
                parser = new TextParser(new FileReader(file));
            } catch (FileNotFoundException e) {
                toast("Error while reading file: " + e.getMessage());
            }
            try {
                assert parser != null;
                book = parser.parse();
            } catch (ParserException e) {
                toast("Error while parsing file: " + e.getMessage());
            }
            return book;
        }

        return null;
    }


}