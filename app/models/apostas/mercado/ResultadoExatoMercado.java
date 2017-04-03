package models.apostas.mercado;

public class ResultadoExatoMercado extends Mercado {

    private static final String NOME = "Resultado Exato";

    private static final TipoMercado TIPO = TipoMercado.S;

    @Override
    public String getId() {
        return "resultado-exato";
    }

    public enum Posicao {
        c0x0(0, 0),
        c0x1(0, 1);

        private final int fora;
        private final int casa;

        Posicao(int casa, int fora) {
            this.casa = casa;
            this.fora = fora;
        }

        static Posicao of(int casa, int fora) {
            String nome = String.format("c%dx%d", casa, fora);
            return valueOf(nome);
        }

        public int getFora() {
            return fora;
        }

        public int getCasa() {
            return casa;
        }
    }

    public ResultadoExatoMercado() {
        super(NOME, TIPO);
    }
}
