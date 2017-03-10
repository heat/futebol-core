package filters;

public class FiltroBilhete {

    public String inicio;
    public String termino;
    public String aposta;
    public String dono;
    public Long evento;

    public FiltroBilhete() {
    }

    public FiltroBilhete(String inicio, String termino, String aposta, String dono, Long evento) {
        this.inicio = inicio;
        this.termino = termino;
        this.aposta = aposta;
        this.dono = dono;
        this.evento = evento;
    }
}
