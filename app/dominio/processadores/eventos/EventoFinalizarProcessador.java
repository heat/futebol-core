package dominio.processadores.eventos;

import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;
import models.bilhetes.Palpite;
import models.eventos.Evento;
import models.eventos.Resultado;
import models.vo.Tenant;
import repositories.BilheteRepository;
import repositories.EventoRepository;
import repositories.ResultadoRepository;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EventoFinalizarProcessador implements Processador<Tenant, Evento> {

    public static final String REGRA = "evento.finalizar";

    EventoRepository eventoRepository;
    BilheteRepository bilheteRepository;

    ResultadoRepository resultadoRepository;

    @Inject
    public EventoFinalizarProcessador(EventoRepository eventoRepository, ResultadoRepository resultadoRepository, BilheteRepository bilheteRepository) {
        this.eventoRepository = eventoRepository;
        this.resultadoRepository = resultadoRepository;
        this.bilheteRepository = bilheteRepository;
    }

    public CompletableFuture<Evento> executar(Tenant tenant, Evento evento, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
             validador.validate(evento);
        }

        evento.setSituacao(Evento.Situacao.E);

        List<Bilhete> bilhetes = bilheteRepository.todosPorPalpites(tenant, evento.getId());

        bilhetes.forEach(b -> {
            b.getPalpites().forEach(p -> {
                if (p.getTaxa().getEventoAposta() == evento.getId()){
                    p.calcular(evento.getResultadoFutebol());
                }

                if (p.getStatus() == Palpite.Status.E){
                    b.setSituacao(Bilhete.Situacao.E);
                }

            });

            if (b.isPremiado()){
                b.setSituacao(Bilhete.Situacao.C);
            }
        });

        return CompletableFuture.completedFuture(evento);
    }
}
