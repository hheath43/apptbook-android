package edu.pdx.cs410J.heathhan;

import edu.pdx.cs410J.AppointmentBookDumper;


import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

/**
 * This class represents a <code>TextDumper</code>
 */
public class TextDumper implements AppointmentBookDumper<AppointmentBook> {
    private final Writer writer;

    /**
     * Creates a new <code>TextDumper</code>
     *
     * @param writer - Writer
     *        File to write the
     */
    public TextDumper(Writer writer){
        this.writer = writer;
    }


    /**
     *Method to dump an AppointmentBook object to a text file
     *
     * @param book - AppointmentBook
     *        The AppointmentBook object to be dumped to a text file.
     *
     * @throws IOException
     *         If I/O fails
     */
    @Override
    public void dump(AppointmentBook book) throws IOException{
        writer.write(book.getOwnerName()+"\n");
        Collection<Appointment> s = book.getAppointments();

        if(s.isEmpty()){
            writer.flush();
        }
        else {

            for (Appointment appt : s) {
                writer.write(appt.getDescription() + "\n");
                writer.write(appt.getBeginTimeString() + "\n");
                writer.write(appt.getEndTimeString() + "\n");
            }

            writer.flush();
        }

    }
}
