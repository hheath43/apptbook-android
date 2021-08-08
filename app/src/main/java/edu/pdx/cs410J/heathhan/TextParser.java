package edu.pdx.cs410J.heathhan;

import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * This class represents a <code>TextParser</code>
 */
public class TextParser implements AppointmentBookParser<AppointmentBook> {
    private final BufferedReader reader;

    /**
     * Creates a new <code>TextParser</code> object
     *
     * @param reader - Reader
     *        String Reader to read in from server for parsing.
     */
    public TextParser(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    /**
     * Method to parse a file into an AppointmentBook
     *
     * @return - AppointmentBook
     *          Returns an AppointmentBook object
     *
     * @throws ParserException
     *          Exception when reader isn't ready.
     */
    @Override
    public AppointmentBook parse() throws ParserException {
        try {
            if (!reader.ready()) {
                throw new ParserException("Missing owner");
            }
            else {
                String owner = reader.readLine();
                AppointmentBook book = new AppointmentBook(owner);

                while (reader.ready()) {
                    String[] array = new String[3];
                    String delim = "[ \\,]+";
                    String[] tokens;
                    String beginDate = null;
                    String beginTime = null;
                    String midday1 = null;
                    String endDate = null;
                    String endTime = null;
                    String midday2 = null;
                    String begin = null;
                    String description = null;
                    String end = null;

                    for (int i = 0; i <= 2; ++i) {
                        array[i] = reader.readLine();
                    }
                    if(array[0] != null) {
                        description = array[0];
                    }
                    else{
                        return book;
                    }

                    begin = array[1];
                    if (begin != null) {
                        tokens = begin.split(delim);
                        beginDate = tokens[0];
                        beginTime = tokens[1];
                        midday1 = tokens[2];
                    }
                    end = array[2];
                    if (end != null) {
                        tokens = end.split(delim);
                        endDate = tokens[0];
                        endTime = tokens[1];
                        midday2 = tokens[2];
                    }

                    if(description == null || beginDate == null || beginTime == null || midday1 == null || endDate == null || endTime == null || midday2 == null){
                        throw new ParserException("File Malformatted: Please view -README for help");
                    }
                    Appointment appt = new Appointment(description, beginDate, beginTime, midday1, endDate, endTime, midday2);
                    book.addAppointment(appt);
                }
                return book;
            }

        } catch (IOException e) {
            throw new ParserException("While parsing", e);
        }

    }
}
