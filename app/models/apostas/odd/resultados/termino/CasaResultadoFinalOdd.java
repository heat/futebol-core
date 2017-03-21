package models.apostas.odd.resultados.termino;

import models.apostas.Odd;
import models.apostas.mercado.Mercado;
import models.apostas.mercado.ResultadoFinalMercado;
import models.apostas.odd.Posicionavel;

import javax.persistence.Entity;

@Entity
public class CasaResultadoFinalOdd extends Odd<ResultadoFinalMercado.Posicao> {

    @Override
    public ResultadoFinalMercado.Posicao getPosicao() {
        return ResultadoFinalMercado.Posicao.CASA;
    }
}
