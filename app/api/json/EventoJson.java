package api.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import models.apostas.Apostavel;
import models.eventos.Campeonato;
import models.eventos.Evento;
import models.eventos.Time;
import models.serializacoes.CalendarDeserializer;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventoJson implements Convertable<Evento>, Jsonable {

    public static  String TIPO = "eventos";

    public String id;
    public String casa;
    public String fora;
    public String dataEvento;
    public Evento.Situacao situacao;
    public Long campeonato;
    public Evento.Modalidade modalidade;

    protected EventoJson() {
    }

    public EventoJson(String id, String casa, String fora, String dataEvento, Evento.Situacao situacao, Long campeonato, Evento.Modalidade modalidade) {
        this.id = id;
        this.casa = casa;
        this.fora = fora;
        this.dataEvento = dataEvento;
        this.situacao = situacao;
        this.campeonato = campeonato;
        this.modalidade = modalidade;
    }

    @Override
    public String type() {
        return TIPO;
    }

    public static EventoJson of(Evento evento) {

        return new EventoJson(String.valueOf(evento.getId()),
                String.valueOf(evento.getCasa().getId()),
                String.valueOf(evento.getFora().getId()),
                calendarToString(evento.getDataEvento()),
                evento.getSituacao(),
                evento.getCampeonato().getId(),
                evento.getModalidade());
    }

    public static List<Jsonable> of(List<Evento> eventos) {
        return eventos.stream().map( c -> EventoJson.of(c) ).collect(Collectors.toList());
    }

    public Evento to() {
        CalendarDeserializer deserializer = new CalendarDeserializer();
        Evento evento = null;
        try {
            evento = new Evento(null, Campeonato.ref(campeonato), Time.ref(Long.valueOf(casa)), Time.ref(Long.valueOf(fora)),
                    deserializer.deserialize(dataEvento), modalidade);
            evento.setSituacao(situacao);
            return evento;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return evento;
    }

    private static String calendarToString(Calendar calendar){

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dataAsString = s.format(calendar.getTime());
        return dataAsString;
    }

}
