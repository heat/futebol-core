package models.financeiro.comissao;

import models.vo.Parametro;

import javax.persistence.*;
import java.text.DecimalFormat;

/**
 * Qualquer informação utilizada como parametro para as classes que implementam um plano de comissao
 */
@Entity
@Table(name = "plano_comissao_parametro")
public class ParametroComissao {

    @Id
    @SequenceGenerator(name="plano_comissao_parametro_id_seq", sequenceName = "plano_comissao_parametro_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plano_comissao_parametro_id_seq")
    @Column(name = "plano_comissao_parametro_id",updatable = false)
    private Long id;

    @Embedded
    Parametro parametro;

    Long valor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Parametro getParametro() {
        return parametro;
    }

    public void setParametro(Parametro parametro) {
        this.parametro = parametro;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }
}
