package api.json;

import dominio.validadores.Validador;
import models.apostas.Odd;
import models.apostas.OddConfiguracao;
import models.apostas.mercado.Mercado;
import models.vo.Parametro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ParametroJson implements Serializable, Convertable<Validador>, Jsonable {

    public static final String TIPO = "parametros";

    public Long id;
    public String nome;
    public String regra;
    public BigDecimal decimal;
    public String texto;
    public Boolean logico;
    public Long inteiro;

    public ParametroJson() {
    }

    public ParametroJson(Long id, String nome, String regra, BigDecimal decimal, String texto, Boolean logico, Long inteiro) {
        this.id = id;
        this.nome = nome;
        this.regra = regra;
        this.decimal = decimal;
        this.texto = texto;
        this.logico = logico;
        this.inteiro = inteiro;
    }

    public static ParametroJson of(Validador validador) {

        return new ParametroJson(validador.getId(), validador.getNome(), validador.getRegra(),
                validador.getValorDecimal(), validador.getValorTexto(), validador.getValorLogico(),
                validador.getValorInteiro());
    }

    @Override
    public Validador to() {
        return null;
    }

    public Validador to(Validador validador) {

        validador.setValorLogico(logico);
        validador.setValorTexto(texto);
        validador.setValorInteiro(inteiro);
        validador.setValorDecimal(decimal);

        return validador;
    }

    @Override
    public String type() {
        return TIPO;
    }

}
