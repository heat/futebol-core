package api.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.apostas.Apostavel;
import models.apostas.EventoAposta;
import models.apostas.Taxa;
import models.eventos.Evento;
import play.libs.Json;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class JogoJson extends EventoJson {

    public static final String TIPO = "jogos";

    public final String id;
    public final String evento;
    public final String timeCasa;
    public final String timeFora;
    public final String dataEvento;
    public final String dataJogo;
    public final Apostavel.Situacao situacaoAposta;
    public final Evento.Situacao situacaoEvento;
    public final Evento.Modalidade modalidade;
    public final Boolean permitir;
    public final Integer quantidadeTaxas;
    public final List<Long> favoritas;
    public final ObjectNode links;

    public JogoJson(String id, Apostavel.Situacao situacaoAposta, Boolean permitir,
                    String idEvento, String timeCasa, String timeFora, String dataEvento, Evento.Situacao situacaoEvento, Long campeonato, Evento.Modalidade modalidade, List<Taxa> taxas) {

        super(idEvento, timeCasa, timeFora, dataEvento, situacaoEvento, campeonato, modalidade);
        this.id = id;
        this.evento = idEvento;
        this.timeCasa = timeCasa;
        this.timeFora = timeFora;
        this.dataEvento = dataEvento;
        this.dataJogo = dataEvento;
        this.situacaoAposta = situacaoAposta;
        this.situacaoEvento = situacaoEvento;
        this.modalidade = modalidade;
        this.permitir = permitir;
        this.links = Json.newObject();
        this.links.put("taxas", getContext() + "/jogos/" + evento + "/taxas/");
        this.quantidadeTaxas = taxas.size();
        this.favoritas = taxas.stream().filter(p -> p.isFavorita()).map(m -> m.getOdd().getId()).collect(Collectors.toList());

    }

    public static JogoJson of(EventoAposta aposta) {
        Evento evento = aposta.getEvento();

        return new JogoJson(
                String.valueOf(aposta.getId()),
                aposta.getSituacao(),
                aposta.isPermitir(),
                String.valueOf(evento.getId()),
                evento.getNomeCasa(),
                evento.getNomeFora(),
                calendarToString(evento.getDataEvento()),
                evento.getSituacao(),
                evento.getCampeonato().getId(),
                evento.getModalidade(),
                aposta.getTaxas());
    }

    private static String calendarToString(Calendar calendar){
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dataAsString = s.format(calendar.getTime());
        return dataAsString;
    }
}
