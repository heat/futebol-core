package models.eventos;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "times")
public class Time implements Serializable{

    @Id
    @SequenceGenerator(name="times_time_id_seq", sequenceName = "times_time_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "times_time_id_seq")
    @Column(name = "time_id",updatable = false)
    Long id;

    @Column(name = "tenant_id")
    Long tenant;

    @Column
    String nome;

    public void Time(String nome){
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
