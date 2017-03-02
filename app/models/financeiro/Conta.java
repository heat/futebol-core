package models.financeiro;

import models.seguranca.Usuario;

import java.util.List;

/**
 * Registro de agrega todos os lançamentos financeiros de um usuario
 */
public class Conta {

    public Usuario proprietario;

    public List<Lancamento> lancamentos;


}
