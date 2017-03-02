package models.financeiro;

import java.util.Calendar;
import java.util.UUID;

public class DocumentoTransferencia {

    UUID codigo;

    Calendar dataTransferencia;

    Conta origem;

    Conta destino;
}
