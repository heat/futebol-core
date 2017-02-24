package api.json;

import models.apostas.Odd;

import java.io.Serializable;

public class OddJson implements Serializable, Convertable<Odd>, Jsonable {

    public static final String TIPO = "odds";

    public final Long id;
    public final String nome;
    public final String descricao;
    public final String abreviacao;

    public OddJson(Long id, String nome, String descricao, String abreviacao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.abreviacao = abreviacao;
    }

    public static OddJson of(Odd odd) {

        return new OddJson(odd.getId(), odd.getNome(), odd.getDescricao(), odd.getDescricao());
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
