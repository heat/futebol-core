package api.json;

import models.eventos.Campeonato;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class CampeonatoJson implements Serializable, Convertable<Campeonato>, Jsonable {

    public static final String tipoObjeto = "campeonato";
    public static final String tipoLista = "campeonatos";

    public final String id;

    public final String nome;
    
    public CampeonatoJson(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public CampeonatoJson() {
        id = null;
        nome = null;
    }


    @Override
    public String type() {
        return tipoObjeto;
    }
    public String typeLista() {
        return tipoLista;
    }

    public static CampeonatoJson of(Campeonato campeonato) {

        return new CampeonatoJson(String.valueOf(campeonato.getId()), campeonato.getNome());
    }

    public static List<Jsonable> of(List<Campeonato> campeonatoes) {
        return campeonatoes.stream().map( c -> CampeonatoJson.of(c) ).collect(Collectors.toList());
    }
    public Campeonato to() {
        return new Campeonato(null, nome);
    }
}
