package models.apostas;

import models.eventos.Resultado;

/**
 * Created by kb5w on 24/03/2017.
 */
public interface Calculadora<V extends Resultado> {
    public boolean calcular(V resultado);
}
