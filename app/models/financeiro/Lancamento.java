package models.financeiro;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Registro de lancamento de uma informação financeira para um usuario
 * @param <E> origem que surgiu
 */
public abstract class Lancamento<E> {


    public Calendar dataLancamento;

    public BigDecimal valor;

    public Saldo saldo;


    public abstract  E getOrigem();

    public abstract TipoLancamento getTipo();
}
