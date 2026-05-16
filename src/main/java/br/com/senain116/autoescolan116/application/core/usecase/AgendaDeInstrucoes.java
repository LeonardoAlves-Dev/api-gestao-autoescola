package br.com.senain116.autoescolan116.application.core.usecase;

import br.com.senain116.autoescolan116.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.senain116.autoescolan116.adapter.in.controller.response.instrucao.DadosDetalhamentoAgendamento;
import br.com.senain116.autoescolan116.application.core.cancelamento.DadosCancelamentoInstrucao; // Import novo
import br.com.senain116.autoescolan116.application.core.cancelamento.validacoes.ValidadorCancelamentoDeInstrucao; // Import novo
import br.com.senain116.autoescolan116.application.core.domain.model.Aluno;
import br.com.senain116.autoescolan116.application.core.domain.model.Instrucao;
import br.com.senain116.autoescolan116.application.core.domain.model.Instrutor;
import br.com.senain116.autoescolan116.application.core.service.EmailNotificacaoService;
import br.com.senain116.autoescolan116.application.core.validation.interfaces.ValidadorAgendamento;
import br.com.senain116.autoescolan116.application.port.out.AlunoRepository;
import br.com.senain116.autoescolan116.application.port.out.InstrucaoRepository;
import br.com.senain116.autoescolan116.application.port.out.InstrutorRepository;
import br.com.senain116.autoescolan116.exception.type.aluno.AlunoNotFoundException;
import br.com.senain116.autoescolan116.exception.type.instrucao.DadosIncompletosException;
import br.com.senain116.autoescolan116.exception.type.instrucao.ValidacaoException; // Import novo
import br.com.senain116.autoescolan116.exception.type.instrutor.InstrutorNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeInstrucoes {
    private final AlunoRepository alunoRepository;
    private final InstrutorRepository instrutorRepository;
    private final InstrucaoRepository repository;
    private final List<ValidadorAgendamento> validadoresAgendamento;
    private final EmailNotificacaoService emailNotificacaoService;
    private final List<ValidadorCancelamentoDeInstrucao> validadoresCancelamento; // Injeção nova

    public AgendaDeInstrucoes(
            AlunoRepository alunoRepository,
            InstrutorRepository instrutorRepository,
            InstrucaoRepository repository,
            List<ValidadorAgendamento> validadoresAgendamento,
            EmailNotificacaoService emailNotificacaoService,
            List<ValidadorCancelamentoDeInstrucao> validadoresCancelamento) { // Parâmetro novo
        this.alunoRepository = alunoRepository;
        this.instrutorRepository = instrutorRepository;
        this.repository = repository;
        this.validadoresAgendamento = validadoresAgendamento;
        this.emailNotificacaoService = emailNotificacaoService;
        this.validadoresCancelamento = validadoresCancelamento;
    }

    @Transactional
    public DadosDetalhamentoAgendamento agendarInstrucao(DadosAgendamento dados) {
        if (!alunoRepository.existsById(dados.idAluno())) {
            throw new AlunoNotFoundException("ID do aluno informado não existe!");
        }
        if (dados.idInstrutor() != null && !instrutorRepository.existsById(dados.idInstrutor())) {
            throw new InstrutorNotFoundException("ID do instrutor informado não existe!");
        }

        //Validações
        validadoresAgendamento.forEach(v -> v.validar(dados));

        Aluno aluno = alunoRepository.getReferenceById(dados.idAluno());
        Instrutor instrutor = escolherInstrutor(dados);

        // Atualizado para o construtor da Instrucao que criamos na interação anterior
        Instrucao instrucao = new Instrucao(
                null,
                aluno,
                instrutor,
                dados.data(),
                null,
                false
        );
        Instrucao saved = repository.save(instrucao);
        emailNotificacaoService.enviarNotificacaoInstrucao(saved, "agendada");
        return new DadosDetalhamentoAgendamento(saved);
    }

    @Transactional
    public void cancelarInstrucao(DadosCancelamentoInstrucao dados) {
        if (!repository.existsById(dados.idInstrucao())) {
            throw new ValidacaoException("Id da instrução informado não existe!");
        }

        // Executa as regras de validação do cancelamento (ex: antecedência de 24h)
        validadoresCancelamento.forEach(v -> v.validar(dados));

        var instrucao = repository.findById(dados.idInstrucao()).get();
        instrucao.cancelar(dados.motivo());

        Instrucao saved = repository.save(instrucao);

        // 👉 INTEGRAÇÃO COM MENSAGERIA (RabbitMQ): Notifica o cancelamento em segundo plano!
        emailNotificacaoService.enviarNotificacaoInstrucao(saved, "cancelada");
    }

    private Instrutor escolherInstrutor(DadosAgendamento dados) {
        if (dados.idInstrutor() != null) {
            return instrutorRepository.getReferenceById(dados.idInstrutor());
        }
        if (dados.especialidade() == null) {
            throw new DadosIncompletosException("Especialidade é obrigatória quando o instrutor não for informado!");
        }
        return instrutorRepository.escolherInstrutorAleatorioDisponivel(
                dados.especialidade(),
                dados.data()
        );
    }
}