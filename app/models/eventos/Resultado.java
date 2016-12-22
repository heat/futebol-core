package models.eventos;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "resultados")
public class Resultado implements Serializable{

    @Id
    @SequenceGenerator(name="resultados_resultado_id_seq", sequenceName = "resultados_resultado_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resultados_resultado_id_seq")
    @Column(name = "resultado_id",updatable = false)
    private Long id;

    @Column(name="momento")
    private String momento;

    @Column(name="pontos")
    private Long pontos;

    @OneToOne
    @JoinColumn(name = "time_id")
    private Time time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMomento() {
        return momento;
    }

    public void setMomento(String momento) {
        this.momento = momento;
    }

    public Long getPontos() {
        return pontos;
    }

    public void setPontos(Long pontos) {
        this.pontos = pontos;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
