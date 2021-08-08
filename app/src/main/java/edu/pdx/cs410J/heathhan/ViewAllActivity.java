package edu.pdx.cs410J.heathhan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ViewAllActivity extends AppCompatActivity {
    private ArrayAdapter<Appointment> appointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        Bundle b = getIntent().getExtras();
        AppointmentBook book = (AppointmentBook) b.get("appointmentBook");


        this.appointments = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        ListView listOfAppt = findViewById(R.id.appointments);
        appointments.addAll(book.getAppointments());
        listOfAppt.setAdapter(appointments);
    }

}