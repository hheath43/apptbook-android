package edu.pdx.cs410J.heathhan;

import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.*;

/**
 * Class represents an <code>AppointmentBook</code>
 */
public class AppointmentBook extends AbstractAppointmentBook<Appointment> {

    private final String owner;
    private Collection<Appointment> appointments = new TreeSet<>();

    /**
     * Creates an <code>AppointmentBook</code> object - default constructor
     */
    public AppointmentBook(){
        this.owner = null;
    }

    /**
     * Creates an <code>AppointmentBook</code> object.
     *
     * @param owner
     *        The owner of the AppointmentBook.
     */
    public AppointmentBook(String owner) {
        this.owner = owner;
    }

    /**
     * Method to get the owner name of the AppointmentBook
     *
     * @return - String
     *          Returns a string with the owner's name, or error message that owner is missing.
     */
    @Override
    public String getOwnerName(){
        if(this.owner == null){
            System.err.println("Appointment Book Owner is Missing");
            return "Appointment Book Owner is Missing";
        }
        else {
            return this.owner;
        }
    }

    /**
     * Method to get all the appointments in the appointmentBook.
     *
     * @return - Collection
     *
     */
    @Override
    public Collection<Appointment> getAppointments(){
        return this.appointments;

    }

    /**
     * Method to add an appointment to the appointmentBook list.
     *
     * @param appointment
     *        Appointment adding to the appointmentBook.
     */
    @Override
    public void addAppointment(Appointment appointment){
        this.appointments.add(appointment);
    }


    public AppointmentBook searchDates(Date start, Date end){
        Date begin  = null;
        AppointmentBook tBook = new AppointmentBook(this.getOwnerName());
        for(Appointment appt: this.appointments){
            begin = appt.getBeginTime();

            if(begin.equals(start) || begin.equals(end) || (begin.after(start) && begin.before(end))){
                tBook.addAppointment(appt);
            }

        }

        if(tBook.appointments.isEmpty()){
            System.out.println(this.owner + " Has No Appointments Between " + start + " and " + end);
        }

        return tBook;
    }

}
