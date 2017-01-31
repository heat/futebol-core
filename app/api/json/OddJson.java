package api.json;

import models.apostas.Odd;

import java.io.Serializable;

public class OddJson implements Serializable, Convertable<Odd>, Jsonable {

    public static final String TIPO = "odds";

    public final String nome;

    public final String descricao;


    public OddJson(String nome, String descricao) {

        this.nome = nome;
        this.descricao = nome;
    }

    public static OddJson of(Odd odd) {

        return new OddJson(odd.getNome(), odd.getDescricao());
    }


    @Override
    public Odd to() {
        return new Odd(nome, null, '0', null, descricao, 0L, 0L);
    }

    @Override
    public String type() {
        return null;
    }
}
