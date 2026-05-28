package com.github.guimart1.urbanFlow.controller;

import com.github.guimart1.urbanFlow.domain.Cruzamento;
import com.github.guimart1.urbanFlow.service.RoteamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rotas")
@CrossOrigin(origins = "*")
public class RoteamentoController {

    @Autowired
    private RoteamentoService roteamentoService;

    /**
     * Endpoint para calcular a rota mais rápida entre dois pontos.
     * Exemplo de chamada: GET http://localhost:8080/api/rotas/otimizar?origemId=1&destinoId=3
     */
    @GetMapping("/otimizar")
    public ResponseEntity<List<Cruzamento>> obterMelhorRota(
            @RequestParam Long origenId,
            @RequestParam Long destinoId) {

        List<Cruzamento> caminho = roteamentoService.calcularMelhorRota(origenId, destinoId);

        if (caminho.isEmpty()) {
            // Se a lista voltar vazia, significa que o destino está inacessível (ex: todas as ruas alagadas)
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(caminho);
    }
}