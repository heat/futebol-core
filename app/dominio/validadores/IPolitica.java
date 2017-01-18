package dominio.validadores;

/**
 * Classe de aplicação de politica
 * @param <T> entidade da politica
 */
public interface IPolitica<T> {

    T politica( T entidade );
}
