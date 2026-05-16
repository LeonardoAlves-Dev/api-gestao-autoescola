package br.com.senain116.autoescolan116.application.port.out;

import br.com.senain116.autoescolan116.application.core.domain.model.Instrucao;

import java.time.LocalDateTime;
import java.util.Optional;

public interface InstrucaoRepository {
    boolean existsByAlunoIdAndDataBetween(Long id, LocalDateTime inicio, LocalDateTime fim);

    boolean existsByInstrutorIdAndData(Long id, LocalDateTime data);

    Instrucao save(Instrucao instrucao);

    // Adicione estas duas assinaturas abaixo:
    boolean existsById(Long id);
    Optional<Instrucao> findById(Long id);
}