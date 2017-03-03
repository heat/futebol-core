package models.financeiro;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Saldo {

    public BigDecimal saldo;
}
