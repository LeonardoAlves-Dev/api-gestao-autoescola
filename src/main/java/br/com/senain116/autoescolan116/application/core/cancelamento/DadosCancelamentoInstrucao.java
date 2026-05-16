package br.com.senain116.autoescolan116.application.core.cancelamento;

import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoInstrucao(
        @NotNull
        Long idInstrucao,

        @NotNull
        MotivoCancelamento motivo
) {
}