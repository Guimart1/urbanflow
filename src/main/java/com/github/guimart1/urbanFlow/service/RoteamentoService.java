package com.github.guimart1.urbanFlow.service;

import com.github.guimart1.urbanFlow.domain.Cruzamento;
import com.github.guimart1.urbanFlow.domain.Via;
import com.github.guimart1.urbanFlow.repository.CruzamentoRepository;
import com.github.guimart1.urbanFlow.repository.ViaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoteamentoService {

    @Autowired
    private CruzamentoRepository cruzamentoRepository;

    @Autowired
    private ViaRepository viaRepository;

    /**
     * Calcula o caminho mais rápido entre dois cruzamentos usando Dijkstra.
     * Se uma via estiver alagada, ela será ignorada automaticamente.
     */
    public List<Cruzamento> calcularMelhorRota(Long origemId, Long destinoId) {
        Cruzamento origem = cruzamentoRepository.findById(origemId)
                .orElseThrow(() -> new IllegalArgumentException("Cruzamento de origem não encontrado"));
        Cruzamento destino = cruzamentoRepository.findById(destinoId)
                .orElseThrow(() -> new IllegalArgumentException("Cruzamento de destino não encontrado"));

        // Guarda a menor distância/tempo conhecido para chegar em cada cruzamento
        Map<Cruzamento, Double> custos = new HashMap<>();
        // Guarda o "nó pai", ou seja, de qual cruzamento viemos para chegar aqui pelo caminho mais rápido
        Map<Cruzamento, Cruzamento> predecessores = new HashMap<>();
        // Fila de prioridade para analisar sempre o cruzamento com o menor custo atual
        PriorityQueue<CruzamentoCusto> fila = new PriorityQueue<>(Comparator.comparingDouble(CruzamentoCusto::custo));
        // Guarda os cruzamentos que já foram totalmente processados
        Set<Cruzamento> visitados = new HashSet<>();

        // Inicializa o ponto de partida
        custos.put(origem, 0.0);
        fila.add(new CruzamentoCusto(origem, 0.0));

        while (!fila.isEmpty()) {
            CruzamentoCusto atual = fila.poll();
            Cruzamento cruzamentoAtual = atual.cruzamento();

            if (visitados.contains(cruzamentoAtual)) continue;
            visitados.add(cruzamentoAtual);

            // Se chegamos ao destino, podemos parar a busca
            if (cruzamentoAtual.equals(destino)) break;

            // Busca todas as ruas que saem do cruzamento atual
            List<Via> viasSaindo = viaRepository.findByOrigem(cruzamentoAtual);

            for (Via via : viasSaindo) {
                Cruzamento vizinho = via.getDestino();
                if (visitados.contains(vizinho)) continue;

                // Se a via estiver alagada, o peso retorna Double.MAX_VALUE
                double pesoVia = via.getPesoAtual();
                if (pesoVia == Double.MAX_VALUE) {
                    continue; // Ignora a rua completamente (desvio de alagamento)
                }

                double novoCusto = custos.getOrDefault(cruzamentoAtual, Double.MAX_VALUE) + pesoVia;
                double custoVizinho = custos.getOrDefault(vizinho, Double.MAX_VALUE);

                // Se encontramos um caminho mais rápido para chegar no vizinho...
                if (novoCusto < custoVizinho) {
                    custos.put(vizinho, novoCusto);
                    predecessores.put(vizinho, cruzamentoAtual);
                    fila.add(new CruzamentoCusto(vizinho, novoCusto));
                }
            }
        }

        // Reconstrói a lista do caminho de trás para frente usando os predecessores
        return reconstruirCaminho(predecessores, origem, destino);
    }

    private List<Cruzamento> reconstruirCaminho(Map<Cruzamento, Cruzamento> predecessores, Cruzamento origem, Cruzamento destino) {
        LinkedList<Cruzamento> caminho = new LinkedList<>();
        Cruzamento passo = destino;

        // Se o destino não tem predecessor e não é a origem, significa que não há caminho disponível (ex: todas as rotas alagadas)
        if (predecessores.get(passo) == null && !destino.equals(origem)) {
            return Collections.emptyList();
        }

        caminho.add(passo);
        while (predecessores.get(passo) != null) {
            passo = predecessores.get(passo);
            caminho.addFirst(passo);
        }
        return caminho;
    }

    // Usando o recurso de Record do Java 21 para criar uma estrutura leve auxiliar para a fila
    private record CruzamentoCusto(Cruzamento cruzamento, double custo) {}
}