package edu.pdx.cs410J.heathhan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddApptActivity extends AppCompatActivity {
    public static final String EXTRA_APPT = "Appointment";
    private Appointment appt = new Appointment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appt);

        Button addReturn = findViewById(R.id.add_appt);
        addReturn.setOnClickListener(view -> addAppointment());
        addReturn.setOnClickListener(view -> sendApptBackToMain());
    }

    private void addAppointment(){
        EditText descriptionInput = findViewById(R.id.description_input);
        EditText startDateInput = findViewById(R.id.start_date);
        EditText startTimeInput = findViewById(R.id.start_time);
        EditText startMarkerInput = findViewById(R.id.start_marker);
        EditText endDateInput = findViewById(R.id.end_date);
        EditText endTimeInput = findViewById(R.id.end_time);
        EditText endMarkerInput = findViewById(R.id.end_marker);

        String description = descriptionInput.getText().toString();
        String startDate = startDateInput.getText().toString();
        String startTime = startTimeInput.getText().toString();
        String startMarker = startMarkerInput.getText().toString();
        String endDate = endDateInput.getText().toString();
        String endTime = endTimeInput.getText().toString();
        String endMarker = endMarkerInput.getText().toString();

        //Run checks for date and time correctness?

        this.appt = new Appointment(description, startDate, startTime, startMarker, endDate, endTime, endMarker);

    }

    private void sendApptBackToMain() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_APPT, this.appt);
        //startActivity(intent);
        setResult(RESULT_OK, intent);
        finish();
    }

}