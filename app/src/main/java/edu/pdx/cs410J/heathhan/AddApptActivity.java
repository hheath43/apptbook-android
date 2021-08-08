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
    private String description = null;
    private String startDate = null;
    private String startTime = null;
    private String startMarker = null;
    private String endDate = null;
    private String endTime = null;
    private String endMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appt);

        Button addAppt = findViewById(R.id.add_appt);
        addAppt.setOnClickListener(view -> sendApptBackToMain());
    }

    private Appointment addAppointment(){
        EditText descriptionInput = findViewById(R.id.description_input);
        EditText startDateInput = findViewById(R.id.start_date);
        EditText startTimeInput = findViewById(R.id.start_time);
        EditText startMarkerInput = findViewById(R.id.start_marker);
        EditText endDateInput = findViewById(R.id.end_date);
        EditText endTimeInput = findViewById(R.id.end_time);
        EditText endMarkerInput = findViewById(R.id.end_marker);

        this.description = descriptionInput.getText().toString();
        this.startDate = startDateInput.getText().toString();
        this.startTime = startTimeInput.getText().toString();
        this.startMarker = startMarkerInput.getText().toString();
        this.endDate = endDateInput.getText().toString();
        this.endTime = endTimeInput.getText().toString();
        this.endMarker = endMarkerInput.getText().toString();


        // Check if null
        // Run checks for date and time correctness?
        //
        Appointment appt = new Appointment(this.description, this.startDate, this.startTime, this.startMarker, this.endDate, this.endTime, this.endMarker);
        return appt;



    }

    private void sendApptBackToMain() {

        Intent intent = new Intent();
        Appointment appt = addAppointment();
        intent.putExtra(EXTRA_APPT, appt);
        setResult(RESULT_OK, intent);
        finish();
    }

}