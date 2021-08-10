package edu.pdx.cs410J.heathhan;

import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.concurrent.TimeUnit;


/**
 * Class represents an <code>AppointmentBook</code>
 */
public class PrettyPrinter implements AppointmentBookDumper<AppointmentBook> {

    private Writer writer;

    /**
     * Creates a new <code>PrettyPrinter</code> For StdOut
     */
    public PrettyPrinter(){

    }


    /**
     * Creates a new <code>PrettyPrinter</code> For file
     *
     * @param writer - Writer
     *               File to write the
     */
    public PrettyPrinter(Writer writer) {
        this.writer = writer;
    }


    /**
     * Method to dump an appointment book to a file nicely.
     *
     * @param book
     *      Appointment book to be pretty printed to a file.
     *
     * @throws IOException
     *          IOException if the file is unreachable.
     */
    public void dump(AppointmentBook book) throws IOException {
        try {
            writer.write("Appointment Book Owner: " + book.getOwnerName() + "\n\n");
            Collection<Appointment> s = book.getAppointments();
            long duration;

            if(s.isEmpty()) {
                writer.write("No Appointments");
                writer.flush();
            } else {

                for (Appointment appt : s) {
                    writer.write("Appointment Description: " + appt.getDescription() + "\n");
                    writer.write("Begins: " + appt.getBeginTimeString() + "\n");
                    writer.write("Ends: " + appt.getEndTimeString() + "\n");
                    duration = Math.abs(appt.getEndTime().getTime() - appt.getBeginTime().getTime());
                    duration = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);
                    writer.write("Duration: " + String.valueOf(duration) + " Minutes");
                    writer.write("\n\n");
                }

                writer.flush();
            }
        } catch (IOException e) {
            throw new IOException("File could not be found" + e);
        }
    }


    /**
     * Method to StandardOut an appointment book nicely.
     *
     * @param book
     *      Appointment book to be pretty printed.
     */
    public void dumpStandardOut(AppointmentBook book){
        System.out.println("Appointment Book Owner: " + book.getOwnerName() + "\n");
        Collection<Appointment> apptbook = book.getAppointments();
        long duration;

        if(apptbook.isEmpty()){
            System.out.println("No Appointments");
        }
        else{
            for (Appointment appt : apptbook) {
                System.out.println("Appointment Description: " + appt.getDescription());
                System.out.print("Begins: " + appt.getBeginTimeString() + "\n" + "Ends: " + appt.getEndTimeString() + "\n");
                duration = Math.abs(appt.getEndTime().getTime() - appt.getBeginTime().getTime());
                duration = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);
                System.out.println("Duration: " + String.valueOf(duration) + " Minutes");
                System.out.println("\n");
            }
        }
    }

}
