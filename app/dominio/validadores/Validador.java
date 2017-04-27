package dominio.validadores;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "validadores")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Validador<E> implements Serializable, IValidador<E> {

    public enum Situacao {
        R("R"), E("E");

        private final String situacao;

        Situacao(String situacao) {
            this.situacao = situacao;
        }
    }

    @Id
    @SequenceGenerator(name="validadores_validador_id_seq", sequenceName = "validadores_validador_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "validadores_validador_id_seq")
    @Column(name = "validador_id",updatable = false)
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

    private String regra;

    private String nome;

    @Enumerated(EnumType.STRING)
    private Situacao situacao;

    protected Validador(){

    }

    public Validador(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        this.idTenant = idTenant;
        this.regra = regra;
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

    public String getNome() {
        return nome;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setValorInteiro(Long valorInteiro) {
        this.valorInteiro = valorInteiro;
    }

    public void setValorLogico(Boolean valorLogico) {
        this.valorLogico = valorLogico;
    }

    public void setValorDecimal(BigDecimal valorDecimal) {
        this.valorDecimal = valorDecimal;
    }

    public void setValorTexto(String valorTexto) {
        this.valorTexto = valorTexto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Validador<?> validator = (Validador<?>) o;

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
