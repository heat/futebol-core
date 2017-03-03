package models.financeiro;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Saldo {

    private BigDecimal saldo;

    public Saldo() {
    }

    public Saldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
