package models.eventos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import models.eventos.futebol.ResultadoFutebol;
import models.serializacoes.CalendarDeserializer;
import models.serializacoes.CalendarSerializer;
import models.vo.Tenant;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Entity
@Table(name = "eventos")
public class Evento implements Serializable {

    public enum Situacao {
        /**
         * Evento ainda não aconteceu
         */
        A("ABERTO"),
        /**
         * Evento adiado
         */
        D("ADIADO"),
        /**
         * Evento já finalizado
         */
        E("ENCERRADO"),
        /**
         * Evento cancelado
         */
        C("CANCELADO");

        private String situacao;

        Situacao(String situacao) {

            this.situacao = situacao;
        }
    }

    public enum Modalidade {

        BASQUETE("Basquete"), FUTEBOL("Futebol"), MMA("MMA");

        private String nome;

        Modalidade(String nome) {

            this.nome = nome;
        }
    }

    @Id
    @SequenceGenerator(name = "eventos_evento_id_seq", sequenceName = "eventos_evento_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eventos_evento_id_seq")
    @Column(name = "evento_id",updatable = false)
    private Long id;

    @Column(name="tenant_id")
    private Long tenant;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "campeonato_id", nullable = false)
    private Campeonato campeonato;

    @OneToOne

    @JoinColumn(name = "time_id_casa", nullable = false)
    private Time casa;

    @OneToOne
    @JoinColumn(name = "time_id_fora", nullable = false)
    private Time fora;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_evento")
    private Calendar dataEvento;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name="evento_id", nullable = false, updatable = true, insertable = true)
    private List<Resultado> resultados;

    @Enumerated(EnumType.STRING )
    @Column(name = "situacao")
    private Situacao situacao = Situacao.A;

    @Enumerated(EnumType.STRING )
    @Column(name = "modalidade")
    private Modalidade modalidade;

    public Evento() {

    }

    public Evento(Long tenant, Campeonato campeonato, Time casa, Time fora, Calendar dataEvento, Modalidade modalidade) {
        this.tenant = tenant;
        this.campeonato = campeonato;
        this.casa = casa;
        this.fora = fora;
        this.dataEvento = dataEvento;
        this.modalidade = modalidade;
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

    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
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

    public void addResultado(Resultado resultado){
        this.resultados.add(resultado);
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public void setModalidade(Modalidade modalidade) {
        this.modalidade = modalidade;
    }

    public Modalidade getModalidade() {
        return modalidade;
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

    public String getNomeCasa() {
        return this.getCasa().getNome();
    }

    public String getNomeFora() {
        return this.getFora().getNome();
    }


    public ResultadoFutebol getResultadoFutebol() {
        Long casa = getCasa().getId();
        Long fora = getFora().getId();

        Resultado casaIntervalo = new Resultado();
        Resultado casaTermino = new Resultado();
        Resultado foraIntervalo = new Resultado();
        Resultado foraTermino = new Resultado();

        for (Resultado resultado : getResultados()){
            if (resultado.getTime().getId().equals(casa) && resultado.getMomento() == Resultado.Momento.I){
                casaIntervalo = resultado;
            } else
            if (resultado.getTime().getId().equals(casa) && resultado.getMomento() == Resultado.Momento.F){
                casaTermino = resultado;
            } else
            if (resultado.getTime().getId().equals(fora) && resultado.getMomento() == Resultado.Momento.I){
                foraIntervalo = resultado;
            } else
            if (resultado.getTime().getId().equals(fora) && resultado.getMomento() == Resultado.Momento.F){
                foraTermino = resultado;
            }
        }

        return new ResultadoFutebol(casaIntervalo, foraIntervalo, casaTermino, foraTermino);
    }

    public static EventoBuilder builder(Tenant tenant) {
        return new EventoBuilder(tenant);
    }
    public static class EventoBuilder {
        Tenant tenant;
        Time casa;
        Time fora;
        Campeonato campeonato;
        Calendar dataEvento;
        private Modalidade modalidade;

        private EventoBuilder(Tenant tenant) {
            this.tenant = tenant;
        }

        public EventoBuilder comTimeCasa(Time casa) {
            this.casa = casa;
            return this;
        }

        public EventoBuilder comTimeFora(Time fora) {
            this.fora = fora;
            return this;
        }

        public EventoBuilder comCampeonato(Campeonato campeonato) {
            this.campeonato = campeonato;
            return this;
        }


        public EventoBuilder em(Calendar dataEvento) {
            this.dataEvento = dataEvento;
            return this;
        }

        public EventoBuilder comModalidade(Modalidade modalidade) {
            this.modalidade = modalidade;
            return this;
        }

        public Evento build() {
            return new Evento(tenant.get(), campeonato, casa, fora, dataEvento, modalidade);
        }
    }
}
