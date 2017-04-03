package api.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.apostas.Apostavel;
import models.apostas.EventoAposta;
import models.apostas.Taxa;
import models.eventos.Evento;
import play.api.Application;
import play.api.Play;
import play.libs.Json;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class ApostaJson implements Convertable<EventoAposta>, Jsonable {

    public static final String TIPO = "apostas";

    public final String id;
    public final String evento;
    public final Apostavel.Situacao situacao;
    public final Boolean visivel;
    public final ObjectNode links;

    public ApostaJson(String id, String evento, Apostavel.Situacao situacao, Boolean visivel) {
        this.id = id;
        this.evento = evento;
        this.situacao = situacao;
        this.visivel = visivel;
        this.links = Json.newObject();
        this.links.put("taxas", getContext() + "/jogos/" + evento + "/taxas/");
    }

    public static ApostaJson of(EventoAposta aposta) {
        Evento evento = aposta.getEvento();

        return new ApostaJson(
                String.valueOf(aposta.getId()),
                String.valueOf(aposta.getEvento().getId()),
                aposta.getSituacao(),
                aposta.isPermitir());
    }

    private static String calendarToString(Calendar calendar){
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dataAsString = s.format(calendar.getTime());
        return dataAsString;
    }

    @Override
    public EventoAposta to() {
        return null;
    }

    @Override
    public String type() {
        return null;
    }

}
