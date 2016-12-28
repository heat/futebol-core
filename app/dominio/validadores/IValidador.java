package dominio.validadores;

import dominio.validadores.exceptions.ValidadorExcpetion;

public interface IValidador<E> {

    void validate(E entity) throws ValidadorExcpetion;
}
