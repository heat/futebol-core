package validators;

import validators.exceptions.ValidadorExcpetion;

public interface IValidador<E> {

    void validate(E entity) throws ValidadorExcpetion;
}
