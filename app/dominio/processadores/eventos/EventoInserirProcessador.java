package dominio.processadores.eventos;

import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.eventos.Campeonato;
import models.eventos.Evento;
import models.vo.Tenant;
import repositories.CampeonatoRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EventoInserirProcessador implements Processador<Tenant, Campeonato> {

    public static final String REGRA = "evento.inserir";

    CampeonatoRepository campeonatoRepository;

    @Inject
    public EventoInserirProcessador(CampeonatoRepository campeonatoRepository) {
        this.campeonatoRepository = campeonatoRepository;
    }

    public CompletableFuture<Campeonato> executar(Tenant tenant, Campeonato campeonato, List<Validador> validadores) throws ValidadorExcpetion {

        List<Evento> eventos = campeonato.getEventos();
        for (Evento evento: eventos) {
            evento.setTenant(tenant.get());
            for (Validador validador : validadores) {
                validador.validate(evento);
            }
        }
        campeonatoRepository.atualizar(tenant, campeonato.getId(),campeonato);
        return CompletableFuture.completedFuture(campeonato);
    }
}
