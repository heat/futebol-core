package models.financeiro.comissao;

import models.financeiro.Conta;
import models.seguranca.Usuario;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Table(name = "comissao")
public class Comissao {

    @Id
    @SequenceGenerator(name="comissao_id_seq", sequenceName = "comissao_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comissao_id_seq")
    @Column(name = "comissao_id",updatable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "conta_id")
    private Conta destino;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "criado_em")
    private Calendar criadoEm;

    private BigDecimal valor;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "evento_comissao")
    private PlanoComissao.EVENTO_COMISSAO evento;

    public Comissao() {
    }

    public Comissao(Conta destino, BigDecimal valor, PlanoComissao.EVENTO_COMISSAO evento) {
        this.destino = destino;
        this.criadoEm = Calendar.getInstance();
        this.valor = valor;
        this.evento = evento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlanoComissao.EVENTO_COMISSAO getEvento() {
        return evento;
    }

    public void setEvento(PlanoComissao.EVENTO_COMISSAO evento) {
        this.evento = evento;
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
