package models.apostas.odd.resultados.gols;

import models.apostas.Calculadora;
import models.apostas.Odd;
import models.apostas.Taxa;
import models.apostas.mercado.GolImparParMercado;
import models.apostas.mercado.Mercado;
import models.apostas.mercado.NumeroGolsMercado;
import models.eventos.futebol.ResultadoFutebol;

import javax.persistence.Entity;

@Entity
public class ParGolImparParOdd extends Odd<GolImparParMercado.Posicao> {

    private static final String DESCRICAO = "Ganha quando o número de gols na partida é par";

    private static final String NOME = "Par";

    private static final String ABREVIACAO = "P";

    public ParGolImparParOdd() {
    }

    public ParGolImparParOdd(String codigo) {
        super(codigo);
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public Mercado getMercado() {
        return Mercado.GolImparPar;
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
    public GolImparParMercado.Posicao getPosicao() {
        return GolImparParMercado.Posicao.PAR;
    }

    @Override
    public Calculadora getCalculadora(Taxa taxa) {

        return new CalculadoraM();
    }

    public class CalculadoraM implements Calculadora<ResultadoFutebol> {
        @Override
        public boolean calcular(ResultadoFutebol resultado) {
            Long pontosCasa = resultado.casaSegundoTempo.getPontos();
            Long pontosFora = resultado.foraSegundoTempo.getPontos();
            return ((pontosCasa + pontosFora) % 2) == 0;
        }
    }
}
