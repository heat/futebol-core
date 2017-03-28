package api.json;

import models.apostas.Apostavel;
import models.eventos.Evento;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class EventoJson implements Convertable<Evento>, Jsonable {

    public static final String TIPO = "eventos";

    public final String id;
    public final String casa;
    public final String fora;
    public final String dataEvento;
    public final Evento.Situacao situacao;
    public final Long campeonato;

    public EventoJson(String id, String casa, String fora, String dataEvento, Evento.Situacao situacao, Long campeonato) {
        this.id = id;
        this.casa = casa;
        this.fora = fora;
        this.dataEvento = dataEvento;
        this.situacao = situacao;
        this.campeonato = campeonato;
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
                evento.getCampeonato().getId());
    }

    public static List<Jsonable> of(List<Evento> eventos) {
        return eventos.stream().map( c -> EventoJson.of(c) ).collect(Collectors.toList());
    }

    public Evento to() {
        return new Evento(null, null, null, null, null, null, null);
    }

    private static String calendarToString(Calendar calendar){

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dataAsString = s.format(calendar.getTime());
        return dataAsString;
    }

}
