package api.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import models.apostas.Taxa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ImportacaoJson {

    public String time1;
    public String time2;
    public String campeonato;
    public String codigo;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ssZ", timezone="America/Sao_Paulo")
    public Calendar dataJogo;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ssZ", timezone="America/Sao_Paulo")
    public Calendar atualizadoEm;
    public String sistema;
    public BigDecimal oddCasa;
    public BigDecimal oddEmpate;
    public BigDecimal oddFora;
    public BigDecimal oddCasaEmpate;
    public BigDecimal oddForaEmpate;
    public BigDecimal oddCasaFora;
    public BigDecimal oddCasa15;
    public BigDecimal oddFora15;
    public BigDecimal oddAmbosMarcam;
    public BigDecimal oddApenasMarca;
    public BigDecimal oddAcima25;
    public BigDecimal oddAbaixo25;
    public BigDecimal oddCasa1T;
    public BigDecimal oddEmpate1T;
    public BigDecimal oddFora1T;
    public BigDecimal oddGolPar;
    public BigDecimal oddGolImpar;
    public BigDecimal oddAcima05;
    public BigDecimal oddAcima15;
    public BigDecimal oddAcima35;
    public BigDecimal oddAcima45;
    public BigDecimal oddAbaixo05;
    public BigDecimal oddAbaixo15;
    public BigDecimal oddAbaixo35;
    public BigDecimal oddAbaixo45;
    public BigDecimal oddResultado00;
    public BigDecimal oddResultado10;
    public BigDecimal oddResultado20;
    public BigDecimal oddResultado30;
    public BigDecimal oddResultado40;
    public BigDecimal oddResultado50;
    public BigDecimal oddResultado01;
    public BigDecimal oddResultado11;
    public BigDecimal oddResultado21;
    public BigDecimal oddResultado31;
    public BigDecimal oddResultado41;
    public BigDecimal oddResultado51;
    public BigDecimal oddResultado02;
    public BigDecimal oddResultado12;
    public BigDecimal oddResultado22;
    public BigDecimal oddResultado32;
    public BigDecimal oddResultado42;
    public BigDecimal oddResultado52;
    public BigDecimal oddResultado03;
    public BigDecimal oddResultado13;
    public BigDecimal oddResultado23;
    public BigDecimal oddResultado33;
    public BigDecimal oddResultado43;
    public BigDecimal oddResultado53;
    public BigDecimal oddResultado04;
    public BigDecimal oddResultado14;
    public BigDecimal oddResultado24;
    public BigDecimal oddResultado34;
    public BigDecimal oddResultado44;
    public BigDecimal oddResultado54;
    public BigDecimal oddResultado05;
    public BigDecimal oddResultado15;
    public BigDecimal oddResultado25;
    public BigDecimal oddResultado35;
    public BigDecimal oddResultado45;
    public BigDecimal oddResultado55;
    public BigDecimal oddCasa05;
    public BigDecimal oddCasa25;
    public BigDecimal oddCasa35;
    public BigDecimal oddFora05;
    public BigDecimal oddFora25;
    public BigDecimal oddFora35;
    public BigDecimal oddResultadoCasaCasa;
    public BigDecimal oddResultadoCasaEmpate;
    public BigDecimal oddResultadoCasaFora;
    public BigDecimal oddResultadoEmpateCasa;
    public BigDecimal oddResultadoEmpateEmpate;
    public BigDecimal oddResultadoEmpateFora;
    public BigDecimal oddResultadoForaCasa;
    public BigDecimal oddResultadoForaEmpate;
    public BigDecimal oddResultadoForaFora;

    @JsonIgnore
    private List<Taxa> taxas = new ArrayList<>();

    public void addTaxa(Taxa t) {
        this.taxas.add(t);
    }

    public List<Taxa> getTaxas() {
        return taxas;
    }
}
