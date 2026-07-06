package com.totvs.calculadora.controller;

import com.totvs.calculadora.dto.EmprestimoRequest;
import com.totvs.calculadora.dto.ParcelaResponse;
import com.totvs.calculadora.service.CalculadoraService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calculadora")
@CrossOrigin(origins = "*")
public class CalculadoraController {

    private final CalculadoraService calculadoraService;

    public CalculadoraController(CalculadoraService calculadoraService) {
        this.calculadoraService = calculadoraService;
    }

    @PostMapping("/calcular")
    public ResponseEntity<List<ParcelaResponse>> calcular(@Valid @RequestBody EmprestimoRequest request) {
        
        if (request.dataFinal().isBefore(request.dataInicial()) || request.dataFinal().isEqual(request.dataInicial())) {
            return ResponseEntity.badRequest().build();
        }
        
        if (request.primeiroPagamento().isBefore(request.dataInicial()) || 
            request.primeiroPagamento().isEqual(request.dataInicial()) || 
            request.primeiroPagamento().isAfter(request.dataFinal())) {
            return ResponseEntity.badRequest().build();
        }

        List<ParcelaResponse> resultado = calculadoraService.gerarDemonstrativo(request);
        return ResponseEntity.ok(resultado);
    }
}