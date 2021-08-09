package edu.pdx.cs410J.heathhan;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class SearchActivity extends AppCompatActivity {


    private TextView setStartDate, setStartTime, setEndDate, setEndTime;
    private String startDate, startTime, endDate, endTime;
    private static final int dateStartId = 0;
    private static final int timeStartId = 1;
    private static final int dateEndId = 2;
    private static final int timeEndId = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        PrettyPrinter pretty;
        BufferedReader reader;
        StringBuilder text = new StringBuilder();
        String line;

        Bundle b = getIntent().getExtras();
        AppointmentBook book = (AppointmentBook) b.get("appointmentBook");
        //File file = getFile(book.getOwnerName());

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



    }

    private void toast(String message) {
        Toast.makeText(SearchActivity.this, message, Toast.LENGTH_LONG).show();
    }

    protected Dialog onCreateDialog(int id) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hr = 12;
        int min = 0;


        switch (id) {
            case dateStartId:

                return new DatePickerDialog(SearchActivity.this, date_start_listener, year,
                        month, day);
            case timeStartId:

                return new TimePickerDialog(SearchActivity.this, time_start_listener, hr,
                        min, false);

            case dateEndId:

                return new DatePickerDialog(SearchActivity.this, date_end_listener, year, month, day);

            case timeEndId:

                return new TimePickerDialog(SearchActivity.this, time_end_listener, hr, min, false);

        }
        return null;
    }

    // Date picker dialog
    DatePickerDialog.OnDateSetListener date_start_listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // store the data in one string and set it to text
            startDate = String.valueOf(month+1) + "/" + String.valueOf(day)
                    + "/" + String.valueOf(year);
            setStartDate.setText(startDate);

        }
    };
    TimePickerDialog.OnTimeSetListener time_start_listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hr, int min) {
            String marker;

            if(hr == 12){
                marker = "PM";
            } else if(hr > 12){
                hr -= 12;
                marker = "PM";
            } else if(hr == 0){
                hr += 12;
                marker = "AM";
            } else {
                marker = "AM";
            }

            startTime = String.valueOf(hr) + ":" + String.valueOf(min) + " " + marker;
            setStartTime.setText(startTime);
        }

    };

    DatePickerDialog.OnDateSetListener date_end_listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // store the data in one string and set it to text
            endDate = String.valueOf(month+1) + "/" + String.valueOf(day) + "/" + String.valueOf(year);
            setEndDate.setText(endDate);

        }
    };
    TimePickerDialog.OnTimeSetListener time_end_listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hr, int min) {
            String marker;

            if(hr == 12){
                marker = "PM";
            } else if(hr > 12){
                hr -= 12;
                marker = "PM";
            } else if(hr == 0){
                hr += 12;
                marker = "AM";
            } else {
                marker = "AM";
            }

            endTime = String.valueOf(hr) + ":" + String.valueOf(min) + " " + marker;
            setEndTime.setText(endTime);
        }

    };






    //*******************************************************************************8



    /**
     * Method to convert Strings to a Date variable
     *
     * @param date - String
     *        String of the date MM/DD/YYYY
     *
     * @param time - String
     *         String of the time HH:MM
     *
     * @param marker - String
     *          String of AM or PM
     *
     * @return - Date
     *         New Date variable returned
     *
     */
    private static Date convertStringToDate(String date, String time, String marker) {
        String str = date + " " + time + " " + marker;
        Date d = null;
        try {
            d = new SimpleDateFormat("M/d/yy h:mm a").parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * Method to check if begin time and date precedes the end time and date
     *
     * @param beginDate
     *         When the appointment begins day/month/year
     *
     * @param beginTime
     *          When the appointment begins hr:min
     *
     * @param marker1
     *          AM/PM marker for begin time
     *
     * @param endDate
     *          When the appointment ends day/month/year
     *
     * @param endTime
     *          When the appointment ends hr:min
     *
     * @param marker2
     *          AM/PM marker for end time
     *
     * @return - boolean
     *          True if end date is after begin, false if end date isn't after begin.
     */
    private static boolean endIsAfterBegin(String beginDate, String beginTime, String marker1, String endDate, String endTime, String marker2){
        String delim = "[/]";
        String delim2 = "[:]";
        String[] date;
        int[] dateInt;
        int[] timeInt;
        LocalDateTime begin;
        LocalDateTime end;

        date = beginDate.split(delim);
        dateInt = parse(date);
        date = beginTime.split(delim2);
        timeInt = parse(date);
        if(marker1.equalsIgnoreCase("PM") && timeInt[0] != 12){
            timeInt[0] += 12;
        }
        else if(marker1.equalsIgnoreCase("AM") && timeInt[0] == 12){
            timeInt[0] = 0;
        }
        begin = LocalDateTime.of(dateInt[2], dateInt[0], dateInt[1], timeInt[0], timeInt[1]);

        date = endDate.split(delim);
        dateInt = parse(date);
        date = endTime.split(delim2);
        timeInt = parse(date);
        if(marker2.equalsIgnoreCase("PM") && timeInt[0] != 12){
            timeInt[0] += 12;
        }
        else if(marker2.equalsIgnoreCase("AM") && timeInt[0] == 12){
            timeInt[0] = 0;
        }
        end = LocalDateTime.of(dateInt[2], dateInt[0], dateInt[1], timeInt[0], timeInt[1]);

        if(begin.isBefore(end)){
            return true;

        }
        else{
            return false;
        }
    }

    /**
     * Method for int parsing the date and time strings
     *
     * @param str
     *         Date or time string
     *
     * @return - int array
     *         Holds the int values of the string
     */
    private static int[] parse(String[] str){
        int[] num = new int[str.length];

        for(int i = 0; i < str.length; ++i){
            num[i] = Integer.parseInt(str[i]);
        }
        return num;
    }
}
