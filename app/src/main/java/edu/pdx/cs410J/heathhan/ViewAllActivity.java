package edu.pdx.cs410J.heathhan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ViewAllActivity extends AppCompatActivity {
    private ArrayAdapter<Appointment> appointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        PrettyPrinter pretty;
        BufferedReader reader;
        StringBuilder text = new StringBuilder();
        String line = null;

        Bundle b = getIntent().getExtras();
        AppointmentBook book = (AppointmentBook) b.get("appointmentBook");
        File file = getFile(book.getOwnerName());

        try {
            pretty = new PrettyPrinter(new FileWriter(file));
            pretty.dump(book);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            reader = new BufferedReader(new FileReader(file));

            while((line = reader.readLine()) != null){
                text.append(line);
                text.append('\n');
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //this.appointments = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        TextView outputOfAppts = findViewById(R.id.appointments);
        outputOfAppts.setText((CharSequence) text);
        //appointments.addAll(book.getAppointments());
        //listOfAppt.setAdapter(appointments);
    }

    private File getFile(String owner){
        String str = replaceSpace(owner);
        str = str + "2" + ".txt";

        File contextDirectory = getApplicationContext().getDataDir();
        File file = new File(contextDirectory, str);

        return file;
    }

    private String replaceSpace(String str){
        str = str.replace(" ", "_");
        return str;
    }

}