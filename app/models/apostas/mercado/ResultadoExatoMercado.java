package models.apostas.mercado;

public class ResultadoExatoMercado {

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
    }
}
