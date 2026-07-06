package com.totvs.calculadora.service;

import com.totvs.calculadora.dto.EmprestimoRequest;
import com.totvs.calculadora.dto.ParcelaResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class CalculadoraService {

    public List<ParcelaResponse> gerarDemonstrativo(EmprestimoRequest request) {
        List<ParcelaResponse> demonstrativo = new ArrayList<>();

        Set<LocalDate> datasPagamento = new HashSet<>();
        LocalDate iteradorPagamento = request.primeiroPagamento();
        int totalParcelas = 0;
        
        YearMonth mesFinal = YearMonth.from(request.dataFinal());

        // Laço corrigido para garantir que a última parcela caia na Data Final
        while (!YearMonth.from(iteradorPagamento).isAfter(mesFinal)) {
            if (YearMonth.from(iteradorPagamento).equals(mesFinal)) {
                datasPagamento.add(request.dataFinal());
            } else {
                datasPagamento.add(iteradorPagamento);
            }
            totalParcelas++;
            iteradorPagamento = iteradorPagamento.plusMonths(1);
        }

        Set<LocalDate> linhaDoTempo = new TreeSet<>();
        linhaDoTempo.add(request.dataInicial());
        linhaDoTempo.add(request.dataFinal());
        linhaDoTempo.addAll(datasPagamento);

        LocalDate iteradorFimMes = request.dataInicial().with(TemporalAdjusters.lastDayOfMonth());
        
        while (!YearMonth.from(iteradorFimMes).isAfter(mesFinal)) {
            if (iteradorFimMes.isAfter(request.dataInicial())) {
                linhaDoTempo.add(iteradorFimMes);
            }
            iteradorFimMes = iteradorFimMes.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        }

        BigDecimal saldoDevedor = request.valorEmprestimo();
        BigDecimal jurosAcumulado = BigDecimal.ZERO;
        
        BigDecimal valorAmortizacaoFixa = request.valorEmprestimo()
                .divide(new BigDecimal(totalParcelas), 8, RoundingMode.HALF_UP);

        LocalDate dataAnterior = null;
        int parcelaAtual = 1;

        for (LocalDate dataAtual : linhaDoTempo) {
            
            if (dataAnterior == null) {
                demonstrativo.add(new ParcelaResponse(
                        dataAtual, request.valorEmprestimo(), saldoDevedor, "",
                        BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO
                ));
                dataAnterior = dataAtual;
                continue;
            }

            long diasPassados = ChronoUnit.DAYS.between(dataAnterior, dataAtual);

            // Fórmula de Provisão: (((1 + taxa) ^ (dias / 360)) - 1) * (Saldo + JurosAcumulado)
            double taxaBase = request.taxaJuros().doubleValue();
            double expoente = (double) diasPassados / 360.0;
            double fatorJuros = Math.pow(1.0 + taxaBase, expoente) - 1.0;

            BigDecimal baseCalculo = saldoDevedor.add(jurosAcumulado);
            BigDecimal jurosProvisao = baseCalculo.multiply(new BigDecimal(fatorJuros));

            BigDecimal jurosPago = BigDecimal.ZERO;
            BigDecimal amortizacao = BigDecimal.ZERO;
            String consolidada = "";

            boolean isDiaPagamento = datasPagamento.contains(dataAtual);

            if (isDiaPagamento) {
                jurosPago = jurosAcumulado.add(jurosProvisao);
                amortizacao = valorAmortizacaoFixa;
                consolidada = parcelaAtual + "/" + totalParcelas;
                parcelaAtual++;
            }

            jurosAcumulado = jurosAcumulado.add(jurosProvisao).subtract(jurosPago);
            saldoDevedor = saldoDevedor.subtract(amortizacao);
            BigDecimal parcelaTotal = amortizacao.add(jurosPago);

            demonstrativo.add(new ParcelaResponse(
                    dataAtual,
                    BigDecimal.ZERO, 
                    saldoDevedor.setScale(2, RoundingMode.HALF_UP),
                    consolidada,
                    parcelaTotal.setScale(2, RoundingMode.HALF_UP),
                    amortizacao.setScale(2, RoundingMode.HALF_UP),
                    jurosProvisao.setScale(2, RoundingMode.HALF_UP),
                    jurosAcumulado.setScale(2, RoundingMode.HALF_UP),
                    jurosPago.setScale(2, RoundingMode.HALF_UP)
            ));

            dataAnterior = dataAtual;
        }

        return demonstrativo;
    }
}