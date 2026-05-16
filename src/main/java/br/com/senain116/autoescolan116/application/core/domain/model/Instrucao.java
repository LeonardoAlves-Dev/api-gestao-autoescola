package br.com.senain116.autoescolan116.application.core.domain.model;

import br.com.senain116.autoescolan116.application.core.cancelamento.MotivoCancelamento;
import java.time.LocalDateTime;

public class Instrucao {
    private Long id;
    private Aluno aluno;
    private Instrutor instrutor;
    private LocalDateTime data;
    private MotivoCancelamento motivoCancelamento;
    private Boolean cancelada = false;

    public Instrucao() {
    }

    // Construtor antigo (usado no agendamento do professor)
    public Instrucao(Long id, Aluno aluno, Instrutor instrutor, LocalDateTime data) {
        this.id = id;
        this.aluno = aluno;
        this.instrutor = instrutor;
        this.data = data;
    }

    // Construtor novo (usado no mecanismo de cancelamento)
    public Instrucao(Long id, Aluno aluno, Instrutor instrutor, LocalDateTime data, MotivoCancelamento motivoCancelamento, Boolean cancelada) {
        this.id = id;
        this.aluno = aluno;
        this.instrutor = instrutor;
        this.data = data;
        this.motivoCancelamento = motivoCancelamento;
        this.cancelada = cancelada != null ? cancelada : false;
    }

    public Long getId() { return id; }
    public Aluno getAluno() { return aluno; }
    public Instrutor getInstrutor() { return instrutor; }
    public LocalDateTime getData() { return data; }
    public MotivoCancelamento getMotivoCancelamento() { return motivoCancelamento; }
    public Boolean isCancelada() { return cancelada; }

    public void cancelar(MotivoCancelamento motivo) {
        this.motivoCancelamento = motivo;
        this.cancelada = true;
    }
}