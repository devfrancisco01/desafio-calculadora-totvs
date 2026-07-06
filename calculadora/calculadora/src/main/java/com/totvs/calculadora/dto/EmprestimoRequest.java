package com.totvs.calculadora.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record EmprestimoRequest(
    @NotNull LocalDate dataInicial,
    @NotNull LocalDate dataFinal,
    @NotNull LocalDate primeiroPagamento,
    @NotNull @Positive BigDecimal valorEmprestimo,
    @NotNull @Positive BigDecimal taxaJuros
) {}