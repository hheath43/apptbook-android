package edu.pdx.cs410J.heathhan;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.pdx.cs410J.AbstractAppointment;


/**
 * This class represents an <code>Appointment</code>
 */
public class Appointment extends AbstractAppointment implements Comparable<Appointment> {

    private final String description;
    private final String beginDate;
    private final String beginTime;
    private final String midday1;
    private final String endDate;
    private final String endTime;
    private final String midday2;


    /**
     *Creates a new <code>Appointment</code> - default constructor
     */
    public Appointment() {
        super();
        this.description = null;
        this.beginDate = null;
        this.beginTime = null;
        this.midday1 = null;
        this.endDate = null;
        this.endTime = null;
        this.midday2 = null;
    }

    /**
     * Creates a new <code>Appointment</code> - with arguments
     *
     * @param description
     *        Describes what the appointment is being made for.
     * @param beginDate
     *        The date that the appointment begins.
     *
     * @param beginTime
     *        The time that the appointment begins.
     *
     * @param endDate
     *        The date that the appointment ends.
     *
     * @param endTime
     *        The time that the appointment ends.
     */
    public Appointment(String description, String beginDate, String beginTime, String midday1, String endDate, String endTime, String midday2) {
        super();
        this.description = description;
        this.beginDate = beginDate;
        this.beginTime = beginTime;
        this.midday1 = midday1;
        this.endDate = endDate;
        this.endTime = endTime;
        this.midday2 = midday2;
    }

    /**
     * Method to get the begin date and time of an appointment.
     *
     * @return String
     *         Begin date and time, or error message that begin doesn't exist.
     */
    @Override
    public String getBeginTimeString(){
        if(beginDate == null || beginTime == null /*|| midday1 == null*/){
            System.err.println("Appointment Missing Begin Date, Time and/OR AM/PM Marker");
            return "Appointment Missing Begin Date, Time and/OR AM/PM Marker";
        }
        else {
            String str = beginDate + " " + beginTime + " " + midday1;
            Date begin = null;
            try {
                begin = new SimpleDateFormat("MM/dd/yyyy hh:mm a").parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(begin);
        }
    }

    /**
     * Method to get the end date and time of an appointment.
     *
     * @return String
     *        End date and time, or error message that end doesn't exist.
     */
    @Override
    public String getEndTimeString() {
        if(endDate == null || endTime == null || midday2 == null){
            System.err.println("Appointment Missing End Date, Time and/OR AM/PM Marker");
            return "Appointment Missing End Date, Time and/OR AM/PM Marker";
        }
        else {
            String str = endDate + " " + endTime + " " + midday2;
            Date end = null;
            try {
                end = new SimpleDateFormat("MM/dd/yy hh:mm a").parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(end);
        }
    }

    /**
     * Method to get the description of the appointment.
     * @return String
     *         The description of the appointment, or error message that the description doesn't exist.
     */
    @Override
    public String getDescription() {
        if(description == null) {
            System.err.println("Appointment Description Missing");
            return "Appointment Description Missing";
        }
        else {
            return this.description;
        }
    }

    /**
     * Method that gets the begin date and time of an appointment.
     *
     * @return - Date
     *        Returns the begin date and time as a Date object.
     */
    @Override
    public Date getBeginTime() {
        String str = beginDate + " " + beginTime + " " + midday1;
        Date begin = null;
        try {
            begin = new SimpleDateFormat("M/d/yy h:mm a").parse(str);
            return begin;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return begin;
    }

    /**
     * Method that gets the end date and time of an appointment.
     *
     * @return - Date
     *        Returns the end date and time as a Date object.
     */
    @Override
    public Date getEndTime() {
        String str = endDate + " " + endTime + " " + midday2;
        Date end = null;
        try {
            end = new SimpleDateFormat("M/d/yy h:mm a").parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return end;
    }

    /**
     * Method to compare appointment objects.
     *
     * @param a - Appointment
     *          The appointment for comparison.
     *
     * @return - int
     *        Number represents 1 when a should go before, -1 when q should go after
     */
    @Override
    public int compareTo(Appointment a){
        int begin = this.getBeginTime().compareTo(a.getBeginTime());
        int end = this.getEndTime().compareTo(a.getEndTime());
        assert this.description != null;
        assert a.description != null;
        int desc = this.description.compareTo(a.description);

        if(begin == 0){
            return ((end == 0) ? desc : end);
        }
        else{
            return begin;
        }
    }

}
