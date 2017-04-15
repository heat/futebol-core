package models.apostas.odd.resultados.exato;

import models.apostas.Calculadora;
import models.apostas.Odd;
import models.apostas.Taxa;
import models.apostas.mercado.Mercado;
import models.apostas.mercado.ResultadoExatoMercado;
import models.apostas.odd.resultados.termino.CasaResultadoFinalOdd;
import models.eventos.futebol.ResultadoFutebol;

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

    public ResultadoExatoOdd() {
    }

    public ResultadoExatoOdd(String codigo, ResultadoExatoMercado.Posicao posicao) {
        super(codigo);
        this.posicao = posicao;
    }

    @Override
    public Mercado getMercado() {
        return Mercado.ResultadoExato;
    }

    @Override
    public String getNome() {
        String  nome = String.format("Resultado %dx%d", posicao.getCasa(), posicao.getFora());
        return nome;
    }

    @Override
    public String getAbreviacao() {
        String  abreviacao = String.format("%dx%d", posicao.getCasa(), posicao.getFora());
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

    @Override
    public Calculadora getCalculadora(Taxa taxa) {

        return new CalculadoraM(posicao);
    }

    public class CalculadoraM implements Calculadora<ResultadoFutebol> {

        ResultadoExatoMercado.Posicao posicao;

        public CalculadoraM(ResultadoExatoMercado.Posicao posicao) {
            this.posicao = posicao;
        }

        @Override
        public boolean calcular(ResultadoFutebol resultado) {
            switch (posicao) {
                case c0x0:
                    return resultado.casaSegundoTempo.getPontos() == 0L && resultado.foraSegundoTempo.getPontos() == 0L;
                case c0x1:
                    return resultado.casaSegundoTempo.getPontos() == 0L && resultado.foraSegundoTempo.getPontos() == 1L;
                case c0x2:
                    return resultado.casaSegundoTempo.getPontos() == 0L && resultado.foraSegundoTempo.getPontos() == 2L;
                case c0x3:
                    return resultado.casaSegundoTempo.getPontos() == 0L && resultado.foraSegundoTempo.getPontos() == 3L;
                case c0x4:
                    return resultado.casaSegundoTempo.getPontos() == 0L && resultado.foraSegundoTempo.getPontos() == 4L;
                case c0x5:
                    return resultado.casaSegundoTempo.getPontos() == 0L && resultado.foraSegundoTempo.getPontos() == 5L;
                case c1x0:
                    return resultado.casaSegundoTempo.getPontos() == 1L && resultado.foraSegundoTempo.getPontos() == 0L;
                case c1x1:
                    return resultado.casaSegundoTempo.getPontos() == 1L && resultado.foraSegundoTempo.getPontos() == 1L;
                case c1x2:
                    return resultado.casaSegundoTempo.getPontos() == 1L && resultado.foraSegundoTempo.getPontos() == 2L;
                case c1x3:
                    return resultado.casaSegundoTempo.getPontos() == 1L && resultado.foraSegundoTempo.getPontos() == 3L;
                case c1x4:
                    return resultado.casaSegundoTempo.getPontos() == 1L && resultado.foraSegundoTempo.getPontos() == 4L;
                case c1x5:
                    return resultado.casaSegundoTempo.getPontos() == 1L && resultado.foraSegundoTempo.getPontos() == 5L;
                case c2x0:
                    return resultado.casaSegundoTempo.getPontos() == 2L && resultado.foraSegundoTempo.getPontos() == 0L;
                case c2x1:
                    return resultado.casaSegundoTempo.getPontos() == 2L && resultado.foraSegundoTempo.getPontos() == 1L;
                case c2x2:
                    return resultado.casaSegundoTempo.getPontos() == 2L && resultado.foraSegundoTempo.getPontos() == 2L;
                case c2x3:
                    return resultado.casaSegundoTempo.getPontos() == 2L && resultado.foraSegundoTempo.getPontos() == 3L;
                case c2x4:
                    return resultado.casaSegundoTempo.getPontos() == 2L && resultado.foraSegundoTempo.getPontos() == 4L;
                case c2x5:
                    return resultado.casaSegundoTempo.getPontos() == 2L && resultado.foraSegundoTempo.getPontos() == 5L;
                case c3x0:
                    return resultado.casaSegundoTempo.getPontos() == 3L && resultado.foraSegundoTempo.getPontos() == 0L;
                case c3x1:
                    return resultado.casaSegundoTempo.getPontos() == 3L && resultado.foraSegundoTempo.getPontos() == 1L;
                case c3x2:
                    return resultado.casaSegundoTempo.getPontos() == 3L && resultado.foraSegundoTempo.getPontos() == 2L;
                case c3x3:
                    return resultado.casaSegundoTempo.getPontos() == 3L && resultado.foraSegundoTempo.getPontos() == 3L;
                case c3x4:
                    return resultado.casaSegundoTempo.getPontos() == 3L && resultado.foraSegundoTempo.getPontos() == 4L;
                case c3x5:
                    return resultado.casaSegundoTempo.getPontos() == 3L && resultado.foraSegundoTempo.getPontos() == 5L;
                case c4x0:
                    return resultado.casaSegundoTempo.getPontos() == 4L && resultado.foraSegundoTempo.getPontos() == 0L;
                case c4x1:
                    return resultado.casaSegundoTempo.getPontos() == 4L && resultado.foraSegundoTempo.getPontos() == 1L;
                case c4x2:
                    return resultado.casaSegundoTempo.getPontos() == 4L && resultado.foraSegundoTempo.getPontos() == 2L;
                case c4x3:
                    return resultado.casaSegundoTempo.getPontos() == 4L && resultado.foraSegundoTempo.getPontos() == 3L;
                case c4x4:
                    return resultado.casaSegundoTempo.getPontos() == 4L && resultado.foraSegundoTempo.getPontos() == 4L;
                case c4x5:
                    return resultado.casaSegundoTempo.getPontos() == 4L && resultado.foraSegundoTempo.getPontos() == 5L;
                case c5x0:
                    return resultado.casaSegundoTempo.getPontos() == 5L && resultado.foraSegundoTempo.getPontos() == 0L;
                case c5x1:
                    return resultado.casaSegundoTempo.getPontos() == 5L && resultado.foraSegundoTempo.getPontos() == 1L;
                case c5x2:
                    return resultado.casaSegundoTempo.getPontos() == 5L && resultado.foraSegundoTempo.getPontos() == 2L;
                case c5x3:
                    return resultado.casaSegundoTempo.getPontos() == 5L && resultado.foraSegundoTempo.getPontos() == 3L;
                case c5x4:
                    return resultado.casaSegundoTempo.getPontos() == 5L && resultado.foraSegundoTempo.getPontos() == 4L;
                case c5x5:
                    return resultado.casaSegundoTempo.getPontos() == 5L && resultado.foraSegundoTempo.getPontos() == 5L;

            }

            return false;
        }
    }
}
