package models.apostas.odd.resultados.gols;

import models.apostas.Calculadora;
import models.apostas.Odd;
import models.apostas.Taxa;
import models.apostas.mercado.HandicapAsiaticoMercado;
import models.apostas.mercado.Mercado;
import models.apostas.mercado.NumeroGolsMercado;
import models.eventos.futebol.ResultadoFutebol;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class AcimaNumeroGolsOdd extends Odd<NumeroGolsMercado.Posicao> {

    private static final String DESCRICAO = "Ganha quando sai acima de X gols na partida";

    private static final String NOME = "Acima";

    private static final String ABREVIACAO = "A";

    public AcimaNumeroGolsOdd() {
    }

    public AcimaNumeroGolsOdd(String codigo) {
        super(codigo);
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public Mercado getMercado() {
        return Mercado.NumeroGols;
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
    public NumeroGolsMercado.Posicao getPosicao() {
        return NumeroGolsMercado.Posicao.ACIMA;
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

            return (pontosCasa.add(pontosFora)).compareTo(linha) > 1;
        }
    }
}
