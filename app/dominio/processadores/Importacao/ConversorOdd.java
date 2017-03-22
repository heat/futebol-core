package models.Importacao;

import com.google.common.collect.ImmutableMap;
import models.apostas.Odd;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConversorOdd {

    List<Odd> odds;

    Map<String, String> map = new HashMap<>();


    Map<String, BigDecimal> mapLinha = ImmutableMap.of("oddAcima25", BigDecimal.valueOf(2.5));

    public ConversorOdd(List<Odd> odds) {
        this.odds = odds;
        map.put("oddCasa", "resultado-final.casa");
        map.put("oddEmpate", "resultado-final.empate");
        map.put("oddFora", "resultado-final.fora");
        map.put("oddCasaEmpate", "aposta-dupla.casa_empate");
        map.put("oddForaEmpate", "aposta-dupla.empate_fora");
        map.put("oddCasaFora", "aposta-dupla.casa_fora");

    }

    public Optional<Odd> from(String nome) {
        String nomeOddSistema = map.get(nome);
        return odds.stream().filter( odd -> odd.getCodigo().equals(nomeOddSistema))
                .findFirst();
    }

    public BigDecimal linha(String oddName) {
        if(!mapLinha.containsKey(oddName))
            return BigDecimal.ZERO;
        return null;
    }
}
