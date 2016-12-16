package validators;

import validators.exceptions.ValidadorExcpetion;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "validadores")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Validator<E> implements Serializable {

    @Id
    @SequenceGenerator(name="campeonatos_idcampeonato_seq", sequenceName = "campeonatos_idcampeonato_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "campeonatos_idcampeonato_seq")
    @Column(name = "idvalidator",updatable = false)
    private Long id;

    @Column(name="tenant_id")
    private Long idTenant;

    @Column(name="valor_inteiro")
    private Long valorInteiro;

    @Column(name="valor_logico")
    private Boolean valorLogico;

    @Column(name="valor_decimal")
    private BigDecimal valorDecimal;

    @Column(name ="valor_texto")
    private String valorTexto;

    @Column(name="regra")
    private String regra;

    protected Validator(){

    }

    public Validator(Long idTenant, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        this.idTenant = idTenant;
        this.valorInteiro = valorInteiro;
        this.valorLogico = valorLogico;
        this.valorDecimal = valorDecimal;
        this.valorTexto = valorTexto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdTenant() {
        return idTenant;
    }

    public Long getValorInteiro() {
        return valorInteiro;
    }

    public Boolean getValorLogico() {
        return valorLogico;
    }

    public BigDecimal getValorDecimal() {
        return valorDecimal;
    }

    public String getValorTexto() {
        return valorTexto;
    }

    public String getRegra() {
        return regra;
    }

    public void setIdTenant(Long idTenant) {
        this.idTenant = idTenant;
    }



    abstract public void validate(E entity) throws ValidadorExcpetion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Validator<?> validator = (Validator<?>) o;

        if (!id.equals(validator.id)) return false;
        return idTenant.equals(validator.idTenant);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + idTenant.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Validator{" +
                "id=" + id +
                ", idTenant=" + idTenant +
                ", valorInteiro=" + valorInteiro +
                ", valorLogico=" + valorLogico +
                ", valorDecimal=" + valorDecimal +
                ", valorTexto='" + valorTexto + '\'' +
                '}';
    }
}
