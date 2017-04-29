package models.eventos.futebol;

import models.eventos.Evento;
import models.eventos.Resultado;
import models.eventos.ResultadoEvento;
import models.eventos.Time;

import java.util.*;

public class ResultadoFutebol implements ResultadoEvento {

    public final Resultado casaPrimeiroTempo;

    public final Resultado foraPrimeiroTempo;

    public final Resultado casaSegundoTempo;

    public final Resultado foraSegundoTempo;

    public ResultadoFutebol(Resultado casaPrimeiroTempo, Resultado foraPrimeiroTempo, Resultado casaSegundoTempo, Resultado foraSegundoTempo) {
        this.casaPrimeiroTempo = casaPrimeiroTempo;
        this.foraPrimeiroTempo = foraPrimeiroTempo;
        this.casaSegundoTempo = casaSegundoTempo;
        this.foraSegundoTempo = foraSegundoTempo;
    }

    public static List<Resultado> _default(Evento evento) {
        Resultado casaPrimeiroTempo = new ResultadoRef( Resultado.Momento.I, evento.getCasa());
        Resultado foraPrimeiroTempo = new ResultadoRef( Resultado.Momento.I, evento.getFora());
        Resultado casaSegundoTempo = new ResultadoRef( Resultado.Momento.F, evento.getCasa());
        Resultado foraSegundoTempo = new ResultadoRef( Resultado.Momento.F, evento.getFora());
        return Arrays.asList(casaPrimeiroTempo, foraPrimeiroTempo, casaSegundoTempo, foraSegundoTempo);
    }

    public static List<Resultado> merge(List<Resultado> resultados, List<Resultado> resultadosDefault) {
        List<Resultado> resultadosMerge = new ArrayList<>();
        resultadosDefault.forEach( resultado -> {
            // busca se tem o resultado na primeira lista
            Optional<Resultado> _res = resultados.stream().filter(r -> r.getTime().equals(resultado.getTime()) && r.getMomento() == resultado.getMomento()).findFirst();
            // adiciona o resultado encontrado ou o resultado default
            resultadosMerge.add(_res.orElse(resultado));
        });
        return resultadosMerge;
    }

    private static class ResultadoRef extends Resultado {
        public ResultadoRef( Momento i, Time time)  {
                super(null, i, 0L, time);

        }

        @Override
        public Long getId() {
            return UUID.randomUUID().getMostSignificantBits();
        }
    }
}
