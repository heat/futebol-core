package api.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.apostas.Apostavel;
import models.apostas.EventoAposta;
import models.eventos.Evento;
import play.libs.Json;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ApostaJson extends EventoJson {

    public static final String TIPO = "apostas";

    public final ObjectNode links;

    public ApostaJson(String id, String casa, String fora, String dataEvento, Apostavel.Situacao situacao, String campeonato) {
        super(id, casa, fora, dataEvento, situacao, campeonato);
        this.links = Json.newObject();
        this.links.put("taxas", "/apostas/" + id + "/taxas");
    }

    public static ApostaJson of(EventoAposta aposta) {
        Evento evento = aposta.getEvento();
        return new ApostaJson(String.valueOf(evento.getId()),
                evento.getNomeCasa(),
                evento.getNomeFora(),
                calendarToString(evento.getDataEvento()),
                aposta.getSituacao(),
                evento.getCampeonato().getNome());
    }

    private static String calendarToString(Calendar calendar){
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dataAsString = s.format(calendar.getTime());
        return dataAsString;
    }
}
