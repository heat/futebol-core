package api.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.apostas.Apostavel;
import models.apostas.EventoAposta;
import models.eventos.Evento;
import play.api.Application;
import play.api.Play;
import play.libs.Json;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ApostaJson extends EventoJson {

    public static final String TIPO = "apostas";

    public final String id;
    public final String evento;
    public final String casa;
    public final String fora;
    public final String dataEvento;
    public final Apostavel.Situacao situacaoAposta;
    public final Evento.Situacao situacaoEvento;
    public final Boolean permitir;

    public final ObjectNode links;

    public ApostaJson(String id, Apostavel.Situacao situacaoAposta, Boolean permitir,
                      String idEvento, String casa, String fora, String dataEvento, Evento.Situacao situacaoEvento, String campeonato) {
        super(idEvento, casa, fora, dataEvento, situacaoEvento, campeonato);
        this.id = id;
        this.evento = idEvento;
        this.casa = casa;
        this.fora = fora;
        this.dataEvento = dataEvento;
        this.situacaoAposta = situacaoAposta;
        this.situacaoEvento = situacaoEvento;
        this.permitir = permitir;
        this.links = Json.newObject();
        this.links.put("taxas", getContext() + "/taxas?aposta=" + id );

    }

    public static ApostaJson of(EventoAposta aposta) {
        Evento evento = aposta.getEvento();
        return new ApostaJson(
                String.valueOf(aposta.getId()),
                aposta.getSituacao(),
                aposta.isPermitir(),
                String.valueOf(evento.getId()),
                evento.getNomeCasa(),
                evento.getNomeFora(),
                calendarToString(evento.getDataEvento()),
                evento.getSituacao(),
                evento.getCampeonato().getNome());
    }

    private static String calendarToString(Calendar calendar){
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dataAsString = s.format(calendar.getTime());
        return dataAsString;
    }
}
