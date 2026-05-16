package br.com.senain116.autoescolan116.application.core.cancelamento.validacoes;

// IMPORTS ATUALIZADOS PARA A ARQUITETURA HEXAGONAL

import br.com.senain116.autoescolan116.application.core.cancelamento.DadosCancelamentoInstrucao;
import br.com.senain116.autoescolan116.application.port.out.InstrucaoRepository;
import br.com.senain116.autoescolan116.exception.type.instrucao.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioAntecedenciaCancelamento implements ValidadorCancelamentoDeInstrucao {

    // Substituição do @Autowired pelo construtor (melhor prática)
    private final InstrucaoRepository repository;

    public ValidadorHorarioAntecedenciaCancelamento(InstrucaoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validar(DadosCancelamentoInstrucao dados) {
        // Trocado o getReferenceById pelo findById().get() exigido na nova arquitetura
        var instrucao = repository.findById(dados.idInstrucao()).get();

        var agora = LocalDateTime.now();
        var diferencaEmHoras = Duration.between(agora, instrucao.getData()).toHours();

        if (diferencaEmHoras < 24) {
            throw new ValidacaoException("Instrução somente pode ser cancelada com antecedência mínima de 24 horas!");
        }
    }
}