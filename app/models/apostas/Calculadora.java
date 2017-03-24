package models.apostas;

import models.eventos.Resultado;
import models.eventos.ResultadoEvento;

/**
 * Created by kb5w on 24/03/2017.
 */
public interface Calculadora<V extends ResultadoEvento> {
    public boolean calcular(V resultado);
}
