package edu.pdx.cs410J.heathhan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Activity for viewing all Appointments in an owner's AppointmentBook
 *
 */
public class ViewAllActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        PrettyPrinter pretty;
        BufferedReader reader;
        StringBuilder text = new StringBuilder();
        String line;

        Bundle b = getIntent().getExtras();
        AppointmentBook book = (AppointmentBook) b.get("appointmentBook");
        File file = getFile(book.getOwnerName());

        try {
            pretty = new PrettyPrinter(new FileWriter(file));
            pretty.dump(book);
        } catch (IOException e) {
            toast("Error while writing file: " + e.getMessage());
        }

        try {
            reader = new BufferedReader(new FileReader(file));

            while((line = reader.readLine()) != null){
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            toast("Error while reading file: " + e.getMessage());
        }


        TextView outputOfAppts = findViewById(R.id.appointments);
        outputOfAppts.setText(text);


    }

    /**
     * Method to get the owner's file, but adds a '2' on the end for PrettyPrinting
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
        str = str + "2" + ".txt";

        File contextDirectory = getApplicationContext().getDataDir();

        return new File(contextDirectory, str);
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
     * Method to output small message to user
     *
     * @param message
     *      String to be outputted
     */
    private void toast(String message) {
        Toast.makeText(ViewAllActivity.this, message, Toast.LENGTH_LONG).show();
    }

}