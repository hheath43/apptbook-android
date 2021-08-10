package edu.pdx.cs410J.heathhan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDateTime;

public class AddApptActivity extends AppCompatActivity {
    public static final String EXTRA_APPT = "Appointment";
    private Appointment appt = null;
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
        addAppt.setOnClickListener(view -> addNewAppointment());
        Button returnAppt = findViewById(R.id.return_button);
        returnAppt.setOnClickListener(view -> sendApptBackToMain());
    }

    private Appointment addNewAppointment(){
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


        if(this.description.trim().equals("")){
            descriptionInput.setError("Description Is Required");
            toast("Description Is Required");
        } else if(!validateDate(this.startDate)){
            startDateInput.setError("Please Enter a Valid Date ex: MM/DD/YYYY");
            toast("Please Enter a Valid Date ex: MM/DD/YYYY");
        }  else if(!validateTime(this.startTime)){
            startTimeInput.setError("Please Enter a Valid Time ex: HH:MM");
            toast("Please Enter a Valid Time ex: HH:MM");
        } else if(!this.startMarker.equalsIgnoreCase("AM") && !startMarker.equalsIgnoreCase("PM")){
            startMarkerInput.setError("Please Enter a Valid Time Marker ex: AM/PM");
            toast("Please Enter a Valid Time Marker ex: AM/PM");
        } else if(!validateDate(this.endDate)){
            endDateInput.setError("Please Enter a Valid Date ex: MM/DD/YYYY");
            toast("Please Enter a Valid Date ex: MM/DD/YYYY");
        } else if(!validateTime(this.endTime)) {
            endTimeInput.setError("Please Enter a Valid Time ex: HH:MM");
            toast("Please Enter a Valid Time ex: HH:MM");
        } else if(!this.endMarker.equalsIgnoreCase("AM") && !endMarker.equalsIgnoreCase("PM")){
            endMarkerInput.setError("Please Enter a Valid Time Marker ex: AM/PM");
            toast("Please Enter a Valid Time Marker ex: AM/PM");
        } else if(!endIsAfterBegin(this.startDate, this.startTime, this.startMarker, this.endDate, this.endTime, this.endMarker)){
            endDateInput.setError("End Of Appointment Must Be After Start");
            endTimeInput.setError("End Of Appointment Must Be After Start");
            endMarkerInput.setError("End Of Appointment Must Be After Start");
            toast("End Of Appointment Must Be After Start");
        } else {
            this.appt = new Appointment(this.description, this.startDate, this.startTime, this.startMarker, this.endDate, this.endTime, this.endMarker);
            return this.appt;
        }

        return this.appt;



    }

    private void sendApptBackToMain() {

        Intent intent = new Intent();

        intent.putExtra(EXTRA_APPT, this.appt);
        setResult(RESULT_OK, intent);
        finish();

    }


    private void toast(String message) {
        Toast.makeText(AddApptActivity.this, message, Toast.LENGTH_LONG).show();
    }


    /**
     * Method to validate dates
     *
     * @param date - String
     *        Date for begin or end of the appointment to validate.
     *
     * @return - boolean
     *         Returns false if date isn't valid, or true for valid.
     */
    private static boolean validateDate(String date) {
        int mm, dd, yyyy;
        String delim = "[/]";
        String[] tokens;
        boolean alpha = isAlpha(date);

        if(date.length() > 10 || date.length() < 6) {
            return false;
        }
        else if(alpha == true){
            return false;
        }
        else {
            tokens = date.split(delim);
            mm = Integer.parseInt(tokens[0]);
            dd = Integer.parseInt(tokens[1]);
            yyyy = Integer.parseInt(tokens[2]);
        }

        if(mm > 12) {
            return false;
        }
        else if(dd > 31) {
            return false;
        }


        if(mm == 4 || mm == 6 || mm == 9 || mm == 11){
            if(dd > 30){
                return false;
            }
        }
        else if(mm == 2){
            if(dd > 29){
                return false;
            }
        }

        return true;
    }


    /**
     * Method to check a date has letters in it.
     *
     * @param date
     *      The date to check
     *
     * @return - boolean
     *      Returns boolean value if date has alphabetic letters in it.
     */
    private static boolean isAlpha(String date) {
        for(char ch: date.toCharArray()){
            if(Character.isAlphabetic(ch)){
                return true;
            }
        }
        return false;
    }

    /**
     * Method to validate time
     *
     * @param time
     *        Time for begin or end of the appointment.
     *
     * @return - boolean
     *        False if time is invalid, or true for a valid time.
     */
    private static boolean validateTime(String time) {
        int hh,mm;
        String delim = "[:]";
        String[] tokens;

        if(time.length() > 5 || time.length() < 4) {
            return false;
        }
        else {
            tokens = time.split(delim);
            hh = Integer.parseInt(tokens[0]);
            mm = Integer.parseInt(tokens[1]);
        }

        if(hh > 12) {
            return false;
        }
        else if(mm > 59){
            return false;
        }

        return true;
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