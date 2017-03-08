package models.financeiro.comissao;

import models.bilhetes.Bilhete;
import models.vo.Parametro;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class PlanoComissaoBilhete extends PlanoComissao {

    @Override
    public Optional<Comissao> calcular(Comissionavel<Bilhete> b, EVENTO_COMISSAO evento) {
        // Paga comissão somente se o calculo vier de um evento de venda de bilhete
        if(evento != EVENTO_COMISSAO.VENDA_BILHETE) {
            return Optional.empty();
        }

        List<ParametroComissao> parametros = getParametros();

        Bilhete bilhete = b.get();

        Parametro partidas = Parametro.of(bilhete.size());

        Long percentComissao = 0L;

        for( ParametroComissao parametro : parametros) {
            Long _v = parametro.getValor();
            //Verifica se o parametro de partida ainda esta maior que o parametro da comissao;
            if(parametro.getParametro().compareTo(partidas) < 0 && _v > percentComissao) {
                percentComissao = _v;
            }
        }
        if(percentComissao > 0) {
            BigDecimal valorComissao = BigDecimal.valueOf(percentComissao)
                    .multiply(b.valor())
                    .divide(BigDecimal.valueOf(1000));
            Comissao comissao = new Comissao(null, valorComissao, evento);
            return Optional.of(comissao);
        }
        return Optional.empty();
    }
}
