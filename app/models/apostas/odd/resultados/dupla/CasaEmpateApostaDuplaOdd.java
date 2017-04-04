package models.apostas.odd.resultados.dupla;

import models.apostas.Calculadora;
import models.apostas.Odd;
import models.apostas.Taxa;
import models.apostas.mercado.ApostaDuplaMercado;
import models.apostas.mercado.Mercado;
import models.apostas.mercado.ResultadoFinalMercado;
import models.eventos.futebol.ResultadoFutebol;

import javax.persistence.Entity;

@Entity
public class CasaEmpateApostaDuplaOdd extends Odd<ApostaDuplaMercado.Posicao> {

    private static final String DESCRICAO = "Ganha quando casa ganha ou empata";

    private static final String NOME = "CasaEmpate";

    private static final String ABREVIACAO = "CE";

    public CasaEmpateApostaDuplaOdd() {
    }

    public CasaEmpateApostaDuplaOdd(String codigo) {
        super(codigo);
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public Mercado getMercado() {
        return Mercado.ApostaDupla;
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
    public ApostaDuplaMercado.Posicao getPosicao() {
        return ApostaDuplaMercado.Posicao.CASA_EMPATE;
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
            return (pontosCasa > pontosFora) || (pontosCasa == pontosFora);
        }
    }
}
