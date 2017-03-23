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


    Map<String, BigDecimal> mapLinha = new HashMap<>();


    public ConversorOdd(List<Odd> odds) {
        this.odds = odds;
        this.carregaMapOdds();
        this.carregaMapLinhas();

    }

    public ConversorOdd() {

        this.carregaMapOdds();
        this.carregaMapLinhas();
    }

    public Optional<Odd> from(String nome) {
        String nomeOddSistema = map.get(nome);
        return odds.stream().filter( odd -> odd.getCodigo().equals(nomeOddSistema))
                .findFirst();
    }

    public BigDecimal linha(String oddName) {
        if(!mapLinha.containsKey(oddName))
            return BigDecimal.ZERO;
        return mapLinha.get(oddName);
    }

    public Optional<String> getKeyFromValue(String value) {
        for (String o : map.keySet()) {
            if (map.get(o).equals(value)) {
                return Optional.of(o);
            }
        }
        return Optional.empty();
    }

    private void carregaMapOdds(){
        map.put("oddCasa", "resultado-final.casa");
        map.put("oddEmpate", "resultado-final.empate");
        map.put("oddFora", "resultado-final.fora");
        map.put("oddCasaEmpate", "aposta-dupla.casa_empate");
        map.put("oddForaEmpate", "aposta-dupla.empate_fora");
        map.put("oddCasaFora", "aposta-dupla.casa_fora");
        map.put("oddCasa05", "handicap-asiatico.casa");
        map.put("oddCasa15", "handicap-asiatico.casa");
        map.put("oddCasa25", "handicap-asiatico.casa");
        map.put("oddCasa35", "handicap-asiatico.casa");
        map.put("oddFora05", "handicap-asiatico.fora");
        map.put("oddFora15", "handicap-asiatico.fora");
        map.put("oddFora25", "handicap-asiatico.fora");
        map.put("oddFora35", "handicap-asiatico.fora");
        map.put("oddAcima05", "numero-gols.acima");
        map.put("oddAcima15", "numero-gols.acima");
        map.put("oddAcima25", "numero-gols.acima");
        map.put("oddAcima35", "numero-gols.acima");
        map.put("oddAcima45", "numero-gols.acima");
        map.put("oddAbaixo05", "numero-gols.abaixo");
        map.put("oddAbaixo15", "numero-gols.abaixo");
        map.put("oddAbaixo25", "numero-gols.abaixo");
        map.put("oddAbaixo35", "numero-gols.abaixo");
        map.put("oddAbaixo45", "numero-gols.abaixo");
    }

    private void carregaMapLinhas(){

        mapLinha.put("oddCasa05", BigDecimal.valueOf(0.5));
        mapLinha.put("oddCasa15", BigDecimal.valueOf(1.5));
        mapLinha.put("oddCasa25", BigDecimal.valueOf(2.5));
        mapLinha.put("oddCasa35", BigDecimal.valueOf(3.5));
        mapLinha.put("oddFora05", BigDecimal.valueOf(0.5));
        mapLinha.put("oddFora15", BigDecimal.valueOf(1.5));
        mapLinha.put("oddFora25", BigDecimal.valueOf(2.5));
        mapLinha.put("oddFora35", BigDecimal.valueOf(3.5));
        mapLinha.put("oddAcima05", BigDecimal.valueOf(0.5));
        mapLinha.put("oddAcima15", BigDecimal.valueOf(1.5));
        mapLinha.put("oddAcima25", BigDecimal.valueOf(2.5));
        mapLinha.put("oddAcima35", BigDecimal.valueOf(3.5));
        mapLinha.put("oddAcima45", BigDecimal.valueOf(4.5));
        mapLinha.put("oddAbaixo05", BigDecimal.valueOf(0.5));
        mapLinha.put("oddAbaixo15", BigDecimal.valueOf(1.5));
        mapLinha.put("oddAbaixo25", BigDecimal.valueOf(2.5));
        mapLinha.put("oddAbaixo35", BigDecimal.valueOf(3.5));
        mapLinha.put("oddAbaixo45", BigDecimal.valueOf(4.5));

    }
}
