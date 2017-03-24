package models.eventos.futebol;

import models.eventos.Resultado;
import models.eventos.ResultadoEvento;

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
}
