package models.eventos;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import models.apostas.EventoAposta;
import models.serializacoes.CalendarDeserializer;
import models.serializacoes.CalendarSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "eventos")
/*@NamedQueries({
        @NamedQuery(name = "Evento.hasCampeonato", query = "SELECT e FROM Evento e WHERE e.codigo = :codigo ")
})*/
public class Evento implements Serializable{

    @Id
    @SequenceGenerator(name = "eventos_evento_id_seq", sequenceName = "eventos_evento_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eventos_evento_id_seq")
    @Column(name = "evento_id",updatable = false)
    private Long id;

    @Column(name="tenant_id")
    private Long tenant;

    @OneToOne
    @JoinColumn(name = "time_id_casa")
    private Time casa;

    @OneToOne
    @JoinColumn(name = "time_id_fora")
    private Time fora;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_evento")
    private Calendar dataEvento;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name="evento_id")
    private List<Resultado> resultados;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name="evento_id")
    private List<EventoAposta> eventoApostas;

    public Evento() {

    }

    public Evento(Long tenant, Time casa, Time fora, Calendar dataEvento,
                  List<Resultado> resultados, List<EventoAposta> eventoApostas) {

        this.tenant = tenant;
        this.casa = casa;
        this.fora = fora;
        this.dataEvento = dataEvento;
        this.resultados = resultados;
        this.eventoApostas = eventoApostas;
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

    public Time getCasa() {
        return casa;
    }

    public void setCasa(Time casa) {
        this.casa = casa;
    }

    public Time getFora() {
        return fora;
    }

    public void setFora(Time fora) {
        this.fora = fora;
    }

    @JsonDeserialize(using= CalendarDeserializer.class)
    public Calendar getDataEvento() {
        return dataEvento;
    }

    @JsonSerialize(using= CalendarSerializer.class)
    public void setDataEvento(Calendar dataEvento) {
        this.dataEvento = dataEvento;
    }

    public List<Resultado> getResultados() {
        return resultados;
    }

    public void setResultados(List<Resultado> resultados) {
        this.resultados = resultados;
    }

    public List<EventoAposta> getEventoApostas() {
        return eventoApostas;
    }

    public void setEventoApostas(List<EventoAposta> eventoApostas) {
        this.eventoApostas = eventoApostas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Evento evento = (Evento) o;

        if (id != null ? !id.equals(evento.id) : evento.id != null) return false;
        return tenant != null ? tenant.equals(evento.tenant) : evento.tenant == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tenant != null ? tenant.hashCode() : 0);
        return result;
    }
}
