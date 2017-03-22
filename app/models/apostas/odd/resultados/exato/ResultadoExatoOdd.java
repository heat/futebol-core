package models.apostas.odd.resultados.exato;

import models.apostas.Odd;
import models.apostas.mercado.Mercado;
import models.apostas.mercado.ResultadoExatoMercado;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class ResultadoExatoOdd extends Odd<ResultadoExatoMercado.Posicao> {

    private static final String DESCRICAO = "Ganha quando o placar final é igual ao escolhido";

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "posicao")
    ResultadoExatoMercado.Posicao posicao;

    @Override
    public Mercado getMercado() {
        return Mercado.ResultadoExato;
    }

    @Override
    public String getNome() {
        String  nome = String.format("Resultado {}x{}", posicao.getCasa(), posicao.getFora());
        return nome;
    }

    @Override
    public String getAbreviacao() {
        String  abreviacao = String.format("{}x{}", posicao.getCasa(), posicao.getFora());
        return abreviacao;
    }

    @Override
    public String getDescricao() {
        return DESCRICAO;
    }

    @Override
    public ResultadoExatoMercado.Posicao getPosicao() {
        return this.posicao;
    }
}
