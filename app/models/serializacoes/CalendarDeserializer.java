package models.serializacoes;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarDeserializer extends JsonDeserializer<Calendar> {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");


    @Override
    public Calendar deserialize(JsonParser jsonParser,
                                DeserializationContext deserializationContext)
            throws IOException {

        String dateAsString = jsonParser.getText();
        return deserialize(dateAsString);
    }

    public Calendar deserialize(String dataEvento) throws IOException {
        try {
            DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
            DateTime str = fmt.parseDateTime(dataEvento);
            //Date date = formatter.parse(dataEvento);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(str.toDate());
            return calendar;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}