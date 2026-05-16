package br.com.senain116.autoescolan116.application.core.cancelamento.validacoes;

import br.com.senain116.autoescolan116.application.core.cancelamento.DadosCancelamentoInstrucao;

public interface ValidadorCancelamentoDeInstrucao {
    void validar(DadosCancelamentoInstrucao dados);
}