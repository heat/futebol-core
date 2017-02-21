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
    public final Integer quantidadeTaxas;
    public final List<Long> favoritas;
    public final ObjectNode links;

    public ApostaJson(String id, Apostavel.Situacao situacaoAposta, Boolean permitir,
                      String idEvento, String casa, String fora, String dataEvento, Evento.Situacao situacaoEvento, String campeonato, List<Taxa> taxas) {

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
        this.quantidadeTaxas = taxas.size();
        this.favoritas = taxas.stream().filter(p -> p.getOdd().isFavorita()).map(m -> m.getOdd().getId()).collect(Collectors.toList());

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
                evento.getCampeonato().getNome(),
                aposta.getTaxas());

    }

    private static String calendarToString(Calendar calendar){
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dataAsString = s.format(calendar.getTime());
        return dataAsString;
    }
}
