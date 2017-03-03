package models.financeiro;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Registro de lancamento de uma informação financeira para um usuario
 * @param <E> origem que surgiu
 */
@Entity
@Table(name = "lancamentos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Lancamento<E> {

    @Id
    @SequenceGenerator(name="lancamentos_lancamento_id_seq", sequenceName = "lancamentos_lancamento_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lancamentos_lancamento_id_seq")
    @Column(name = "lancamento_id",updatable = false)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "criado_em")
    private Calendar dataLancamento;

    @Column(name = "valor")
    private BigDecimal valor;

    @Embedded
    public Saldo saldo;

    public abstract E getOrigem();

    public abstract TipoLancamento getTipo();
}
