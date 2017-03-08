package models.financeiro.comissao;

import models.financeiro.Conta;
import models.seguranca.Usuario;

import java.math.BigDecimal;
import java.util.Calendar;

public class Comissao {

    Conta destino;

    Calendar criadoEm;

    BigDecimal valor;

    public Comissao(Conta destino, BigDecimal valor) {
        this.destino = destino;
        this.criadoEm = Calendar.getInstance();
        this.valor = valor;
    }

    public Conta getDestino() {
        return destino;
    }

    public void setDestino(Conta destino) {
        this.destino = destino;
    }

    public Calendar getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Calendar criadoEm) {
        this.criadoEm = criadoEm;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comissao comissao = (Comissao) o;

        if (destino != null ? !destino.equals(comissao.destino) : comissao.destino != null) return false;
        if (criadoEm != null ? !criadoEm.equals(comissao.criadoEm) : comissao.criadoEm != null) return false;
        return valor != null ? valor.equals(comissao.valor) : comissao.valor == null;

    }

    @Override
    public int hashCode() {
        int result = destino != null ? destino.hashCode() : 0;
        result = 31 * result + (criadoEm != null ? criadoEm.hashCode() : 0);
        result = 31 * result + (valor != null ? valor.hashCode() : 0);
        return result;
    }
}
