package validators.comuns;

import validators.IValidador;
import validators.exceptions.ValidadorExcpetion;

import java.util.Calendar;

public interface DataValidador extends IValidador<Calendar> {

    static DataValidador entre(Calendar inicio, Calendar termino) {
        return new DataEntreValidador(inicio, termino);
    }

    static DataValidador anterior(Calendar ponto) {
        return new DataAnteriorValidador(ponto);
    }

    static DataValidador posterior(Calendar ponto) {
        return new DataPosteriorValidador(ponto);
    }

    class DataEntreValidador implements DataValidador {
        final Calendar inicio;
        final Calendar termino;

        public DataEntreValidador(Calendar inicio, Calendar termino) {
            this.inicio = inicio;
            this.termino = termino;
        }

        @Override
        public void validate(Calendar data) throws ValidadorExcpetion {
            if(data.before(inicio) || data.after(termino)) {
                throw new ValidadorExcpetion("Data fora do intervalo");
            }
        }
    }

    class DataPosteriorValidador implements DataValidador {

        private final Calendar ponto;

        public DataPosteriorValidador(Calendar ponto) {
            this.ponto = ponto;
        }

        @Override
        public void validate(Calendar data) throws ValidadorExcpetion {
            if(data.after(ponto)){
                throw new ValidadorExcpetion("Data anterior a data específica");
            }
        }
    }

    class DataAnteriorValidador implements DataValidador {

        private final Calendar ponto;

        public DataAnteriorValidador(Calendar ponto) {
            this.ponto = ponto;
        }

        @Override
        public void validate(Calendar data) throws ValidadorExcpetion {
            if(data.before(ponto)){
                throw new ValidadorExcpetion("Data anterior a data específica");
            }
        }
    }
}
