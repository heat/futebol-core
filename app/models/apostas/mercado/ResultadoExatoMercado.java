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
        c0x1(0, 1),
        c0x2(0, 2),
        c0x3(0, 3),
        c0x4(0, 4),
        c0x5(0, 5),
        c1x0(1, 0),
        c1x1(1, 1),
        c1x2(1, 2),
        c1x3(1, 3),
        c1x4(1, 4),
        c1x5(1, 5),
        c2x0(2, 0),
        c2x1(2, 1),
        c2x2(2, 2),
        c2x3(2, 3),
        c2x4(2, 4),
        c2x5(2, 5),
        c3x0(3, 0),
        c3x1(3, 1),
        c3x2(3, 2),
        c3x3(3, 3),
        c3x4(3, 4),
        c3x5(3, 5),
        c4x0(4, 0),
        c4x1(4, 1),
        c4x2(4, 2),
        c4x3(4, 3),
        c4x4(4, 4),
        c4x5(4, 5),
        c5x0(5, 0),
        c5x1(5, 1),
        c5x2(5, 2),
        c5x3(5, 3),
        c5x4(5, 4),
        c5x5(5, 5);

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
