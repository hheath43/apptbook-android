package edu.pdx.cs410J.heathhan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Activity for searching an AppointmentBook between two times
 */
public class SearchActivity extends AppCompatActivity {


    private TextView setStartDate, setStartTime, setEndDate, setEndTime;
    private String startDate = null;
    private String startTime = null;
    private String endDate = null;
    private String endTime = null;
    private static final int dateStartId = 0;
    private static final int timeStartId = 1;
    private static final int dateEndId = 2;
    private static final int timeEndId = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        //Get items from MainActivity
        Bundle b = getIntent().getExtras();
        AppointmentBook book = (AppointmentBook) b.get("appointmentBook");

        //Start Date Search Button
        Button startDateButton = findViewById(R.id.select_start_date);
        setStartDate = findViewById(R.id.set_start_date);
        startDateButton.setOnClickListener(arg0 -> {

            // Show Date dialog
            showDialog(dateStartId);

        });

        //Start Time Search Button
        Button startTimeButton = findViewById(R.id.select_start_time);
        setStartTime = findViewById(R.id.set_start_time);
        startTimeButton.setOnClickListener(arg0 -> {

            // Show time dialog
            showDialog(timeStartId);
        });

        //End Date Search Button
        Button endDateButton = findViewById(R.id.select_end_date);
        setEndDate = findViewById(R.id.set_end_date);
        endDateButton.setOnClickListener(arg0 -> {

            // Show Date dialog
            showDialog(dateEndId);

        });

        //End Time Search Button
        Button endTimeButton = findViewById(R.id.select_end_time);
        setEndTime = findViewById(R.id.set_end_time);
        endTimeButton.setOnClickListener(arg0 -> {

            // Show time dialog
            showDialog(timeEndId);
        });

        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(view -> {
            if(startDate != null && startTime != null && endDate != null && endTime != null) {

                Date dateStart = convertStringToDate(startDate, startTime);

                Date dateEnd = convertStringToDate(endDate, endTime);
                if (dateStart.before(dateEnd)) {
                    AppointmentBook tbook = book.searchDates(dateStart, dateEnd);


                    File file = getFile(tbook.getOwnerName());
                    PrettyPrinter pretty;
                    BufferedReader reader;
                    StringBuilder text = new StringBuilder();
                    String line;

                    try {
                        pretty = new PrettyPrinter(new FileWriter(file));
                        pretty.dump(tbook);
                    } catch (IOException e) {
                        toast("Error while writing file: " + e.getMessage());
                    }

                    try {
                        reader = new BufferedReader(new FileReader(file));

                        while ((line = reader.readLine()) != null) {
                            text.append(line);
                            text.append('\n');
                        }
                    } catch (IOException e) {
                        toast("Error while reading file: " + e.getMessage());
                    }

                    TextView outputOfAppts = findViewById(R.id.search_appts);
                    outputOfAppts.setText(text);


                } else {
                    toast("End Must Be After Start");
                }
            } else {
                toast("Must Select Start Date and Time and End Date and Time");
            }
        });

    }


    /**
     * Method to output small message to user
     *
     * @param message
     *      String to be outputted
     */
    private void toast(String message) {
        Toast.makeText(SearchActivity.this, message, Toast.LENGTH_LONG).show();
    }


    /**
     * Method for onClick of the Dialogs
     *
     * @param id - int
     *          Different id for each Date/Time Picker
     *
     * @return - Dialog
     *         Returns the correct Dialog per id number argument
     */
    protected Dialog onCreateDialog(int id) {
        Calendar calendar = Calendar.getInstance();
        int yr = calendar.get(Calendar.YEAR);
        int mon = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hr = 0;
        int min = 0;


        switch (id) {
            case dateStartId:

                return new DatePickerDialog(SearchActivity.this, date_start_listener, yr,
                        mon, day);
            case timeStartId:

                return new TimePickerDialog(SearchActivity.this, time_start_listener, hr,
                        min, false);

            case dateEndId:

                return new DatePickerDialog(SearchActivity.this, date_end_listener, yr, mon, day);

            case timeEndId:

                return new TimePickerDialog(SearchActivity.this, time_end_listener, hr, min, false);

        }
        return null;
    }

    /**
     * Date Picker for Start Date
     */
    DatePickerDialog.OnDateSetListener date_start_listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            startDate = (mm + 1) + "/" + dd + "/" + yy;
            setStartDate.setText(startDate);

        }
    };

    /**
     * Time Picker for Start Time
     */
    TimePickerDialog.OnTimeSetListener time_start_listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hh, int mm) {
            String marker;

            if(hh == 12){
                marker = "PM";
            } else if(hh > 12){
                hh -= 12;
                marker = "PM";
            } else if(hh == 0){
                hh += 12;
                marker = "AM";
            } else {
                marker = "AM";
            }

            startTime = hh + ":" + mm + " " + marker;
            setStartTime.setText(startTime);
        }

    };

    /**
     * Date Picker for End Date
     */
    DatePickerDialog.OnDateSetListener date_end_listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            endDate = (mm + 1) + "/" + dd + "/" + yy;
            setEndDate.setText(endDate);

        }
    };

    /**
     * Time Picker for End Time
     */
    TimePickerDialog.OnTimeSetListener time_end_listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hh, int mm) {
            String marker;

            if(hh == 12){
                marker = "PM";
            } else if(hh > 12){
                hh -= 12;
                marker = "PM";
            } else if(hh == 0){
                hh += 12;
                marker = "AM";
            } else {
                marker = "AM";
            }

            endTime = hh + ":" + mm + " " + marker;
            setEndTime.setText(endTime);
        }

    };




    /**
     * Method to convert Strings to a Date variable
     *
     * @param date - String
     *        String of the date MM/DD/YYYY
     *
     * //@param time - String
     *         String of the time HH:MM
     *
     * //@param marker - String
     *          String of AM or PM
     *
     * @return - Date
     *         New Date variable returned
     *
     */
    private Date convertStringToDate(String date, String time) {
        String str = date + " " + time;
        Date d = null;
        try {
            d = new SimpleDateFormat("M/d/yy h:mm a").parse(str);
        } catch (ParseException e) {
            toast(new StringBuilder().append("Error while parsing string to date: ").append(e.getMessage()).toString());
        }
        return d;
    }


    /**
     * Method to get the owner's file
     *
     * @param owner -
     *      The name of the AppointmentBook owner
     *
     * @return - File
     *      The owner's file, based on their name with "Search" added
     */
    @NonNull
    private File getFile(String owner){
        String str = replaceSpace(owner);
        str = str + "Search" + ".txt";

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



}
