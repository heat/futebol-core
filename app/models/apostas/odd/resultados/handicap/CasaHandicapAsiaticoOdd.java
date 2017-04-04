package models.apostas.odd.resultados.handicap;

import models.apostas.Calculadora;
import models.apostas.Odd;
import models.apostas.Taxa;
import models.apostas.mercado.HandicapAsiaticoMercado;
import models.apostas.mercado.Mercado;
import models.eventos.futebol.ResultadoFutebol;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class CasaHandicapAsiaticoOdd extends Odd<HandicapAsiaticoMercado.Posicao> {

    private static final String DESCRICAO = "Ganha quando casa ganha a partida por diferença de gols";

    private static final String NOME = "Casa";

    private static final String ABREVIACAO = "C";

    public CasaHandicapAsiaticoOdd() {
    }

    public CasaHandicapAsiaticoOdd(String codigo) {
        super(codigo);
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public Mercado getMercado() {
        return Mercado.HandicapAsiatico;
    }

    @Override
    public String getAbreviacao() {
        return ABREVIACAO;
    }

    @Override
    public String getDescricao() {
        return DESCRICAO;
    }

    @Override
    public HandicapAsiaticoMercado.Posicao getPosicao() {
        return HandicapAsiaticoMercado.Posicao.CASA;
    }

    @Override
    public Calculadora getCalculadora(Taxa taxa) {

        return new CalculadoraL(taxa.getLinha());
    }

    public class CalculadoraL implements Calculadora<ResultadoFutebol> {
        final BigDecimal linha;
        public CalculadoraL(BigDecimal linha) {
            this.linha = linha;
        }

        @Override
        public boolean calcular(ResultadoFutebol resultado) {
            BigDecimal pontosCasa = BigDecimal.valueOf(resultado.casaSegundoTempo.getPontos());
            BigDecimal pontosFora = BigDecimal.valueOf(resultado.foraSegundoTempo.getPontos());

            return (pontosCasa.add(linha)).compareTo(pontosFora) > 1;
        }
    }
}
