package api.json;

import models.eventos.Evento;
import models.eventos.Resultado;
import models.eventos.Time;
import models.vo.Tenant;

import java.util.List;
import java.util.stream.Collectors;

public class ResultadoJson implements Jsonable, Convertable<Resultado>{

    public static final String TIPO = "resultados";

    public String id;

    public Long evento;

    public Long time;

    public Resultado.Momento momento;

    public Long pontos;

    public ResultadoJson() {
    }

    public ResultadoJson(String id, Long evento, Long time, Resultado.Momento momento, Long pontos) {
        this.id = id;
        this.evento = evento;
        this.time = time;
        this.momento = momento;
        this.pontos = pontos;
    }

    @Override
    public Resultado to() {
        Time _time = Time.ref(null, time);
        return new Resultado(null, momento, pontos, _time);
    }

    public Resultado to(Tenant tenant) {
        Time _time = Time.ref(tenant.get(), time);
        return new Resultado( tenant.get(), momento, pontos, _time);
    }

    @Override
    public String type() {
        return TIPO;
    }

    @Override
    public String toString() {
        return "ResultadoJson{" +
                "time='" + time + '\'' +
                ", momento =" + momento.toString() +
                ", pontos =" + pontos +
                '}';
    }

    public static ResultadoJson of(Evento evento, Resultado resultado) {

        return new ResultadoJson(resultado.getId().toString(), evento.getId(), resultado.getTime().getId(), resultado.getMomento(), resultado.getPontos());
    }

    public static List<Jsonable> of(Evento evento, List<Resultado> resultados) {
        return resultados.stream().map( c -> ResultadoJson.of(evento, c) ).collect(Collectors.toList());
    }
}
