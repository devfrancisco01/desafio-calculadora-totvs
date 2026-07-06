package com.totvs.calculadora.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ParcelaResponse(
    LocalDate dataCompetencia,
    BigDecimal valorEmprestimo,
    BigDecimal saldoDevedor,
    String consolidada,
    BigDecimal parcelaTotal,
    BigDecimal amortizacaoPrincipal,
    BigDecimal saldo, 
    BigDecimal jurosProvisao,
    BigDecimal jurosAcumulado,
    BigDecimal jurosPago
) {}