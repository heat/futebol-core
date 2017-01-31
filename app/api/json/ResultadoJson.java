package api.json;

import models.eventos.Resultado;
import models.eventos.Time;

import java.util.List;
import java.util.stream.Collectors;

public class ResultadoJson implements Jsonable, Convertable<Resultado>{

    public static final String TIPO = "resultados";

    public Long time;

    public Resultado.Momento momento;

    public Long pontos;

    public ResultadoJson() {
    }

    public ResultadoJson(Long time, Resultado.Momento momento, Long pontos) {
        this.time = time;
        this.momento = momento;
        this.pontos = pontos;
    }

    @Override
    public Resultado to() {
        Time _time = Time.ref(time);
        return new Resultado(null, momento, pontos, _time);
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

    public static ResultadoJson of(Resultado resultado) {

        return new ResultadoJson( resultado.getTime().getId(), resultado.getMomento(), resultado.getPontos());
    }

    public static List<Jsonable> of(List<Resultado> resultados) {
        return resultados.stream().map( c -> ResultadoJson.of(c) ).collect(Collectors.toList());
    }
}
