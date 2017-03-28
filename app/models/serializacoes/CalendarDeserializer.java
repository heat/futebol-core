package models.serializacoes;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarDeserializer extends JsonDeserializer<Calendar> {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    @Override
    public Calendar deserialize(JsonParser jsonParser,
                                DeserializationContext deserializationContext)
            throws IOException {

        String dateAsString = jsonParser.getText();
        return deserialize(dateAsString);
    }

    public Calendar deserialize(String dataEvento) throws IOException {
        try {
            Date date = formatter.parse(dataEvento);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}