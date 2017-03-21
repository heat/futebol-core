package models.apostas.odd.resultados.exato;

import models.apostas.Odd;
import models.apostas.mercado.ResultadoExatoMercado;

import javax.persistence.Entity;

@Entity
public class ResultadoExatoOdd extends Odd<ResultadoExatoMercado.Posicao> {

    //TODO mapear para coluna posicao
    ResultadoExatoMercado.Posicao posicao;

    @Override
    public ResultadoExatoMercado.Posicao getPosicao() {
        return this.posicao;
    }
}
