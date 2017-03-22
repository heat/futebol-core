package models.apostas.odd.resultados.exato;

import models.apostas.Odd;
import models.apostas.mercado.ResultadoExatoMercado;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class ResultadoExatoOdd extends Odd<ResultadoExatoMercado.Posicao> {

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "posicao")
    ResultadoExatoMercado.Posicao posicao;

    @Override
    public ResultadoExatoMercado.Posicao getPosicao() {
        return this.posicao;
    }
}
