package api.json;

import models.eventos.Time;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class TimeJson implements Serializable, Convertable<Time>, Jsonable {

    public static final String TIPO = "times";

    public final String id;

    public final String nome;

    public TimeJson(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public TimeJson() {
        id = null;
        nome = null;
    }


    @Override
    public String type() {
        return TIPO;
    }

    public static TimeJson of(Time Time) {

        return new TimeJson(String.valueOf(Time.getId()), Time.getNome());
    }

    public static List<Jsonable> of(List<Time> times) {
        return times.stream().map( c -> TimeJson.of(c) ).collect(Collectors.toList());
    }
    public Time to() {
        return new Time(null, nome);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeJson timeJson = (TimeJson) o;

        if (!id.equals(timeJson.id)) return false;
        return nome.equals(timeJson.nome);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + nome.hashCode();
        return result;
    }
}
