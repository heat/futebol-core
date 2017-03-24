package models.apostas.odd.resultados.gols;

import models.apostas.Calculadora;
import models.apostas.Odd;
import models.apostas.Taxa;
import models.apostas.mercado.GolImparParMercado;
import models.apostas.mercado.Mercado;
import models.eventos.futebol.ResultadoFutebol;

import javax.persistence.Entity;

@Entity
public class ImparGolImparParOdd extends Odd<GolImparParMercado.Posicao> {

    private static final String DESCRICAO = "Ganha quando o número de gols na partida é impar";

    private static final String NOME = "Impar";

    private static final String ABREVIACAO = "I";

    public ImparGolImparParOdd() {
    }

    public ImparGolImparParOdd(String codigo) {
        super(codigo);
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public Mercado getMercado() {
        return Mercado.ResultadoFinal;
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
        return GolImparParMercado.Posicao.IMPAR;
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
            return ((pontosCasa + pontosFora) % 2) != 0;
        }
    }
}
