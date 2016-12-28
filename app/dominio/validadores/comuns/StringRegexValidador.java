package dominio.validadores.comuns;

import dominio.validadores.IValidador;
import dominio.validadores.exceptions.ValidadorExcpetion;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface StringRegexValidador extends IValidador<Optional<String>> {

    public static StringRegexValidador of(String regex) {
        Pattern pattern = Pattern.compile(regex);
        return new DefaultStringRegexValidador(pattern);
    }

    class DefaultStringRegexValidador implements StringRegexValidador {

        final Pattern pattern;

        public DefaultStringRegexValidador(Pattern pattern) {
            this.pattern = pattern;
        }

        @Override
        public void validate(Optional<String> entity) throws ValidadorExcpetion {
            if(!entity.isPresent())
                throw new ValidadorExcpetion("Informação não é válida.");

            Matcher matcher = pattern.matcher(entity.orElse("").trim());
            if(!matcher.matches()) {
                throw new ValidadorExcpetion("Informação não é válida.");
            }
        }
    }
}
