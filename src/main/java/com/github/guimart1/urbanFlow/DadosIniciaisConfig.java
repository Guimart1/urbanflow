package com.github.guimart1.urbanFlow;

import com.github.guimart1.urbanFlow.domain.Cruzamento;
import com.github.guimart1.urbanFlow.domain.Via;
import com.github.guimart1.urbanFlow.repository.CruzamentoRepository;
import com.github.guimart1.urbanFlow.repository.ViaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DadosIniciaisConfig {

    @Bean
    CommandLineRunner carregarDados(CruzamentoRepository cruzamentoRepo, ViaRepository viaRepo) {
        return args -> {
            // Se já houver dados no banco, não insere novamente para não duplicar
            if (cruzamentoRepo.count() > 0) return;

            // 1. Criando os Cruzamentos (Nós do Grafo)
            Cruzamento cruzamentoA = new Cruzamento(null, "Cruzamento A (Origem)", -23.55, -46.63);
            Cruzamento cruzamentoB = new Cruzamento(null, "Cruzamento B (Rua Alagada à frente)", -23.56, -46.64);
            Cruzamento cruzamentoC = new Cruzamento(null, "Cruzamento C (Caminho Alternativo Seco)", -23.57, -46.65);
            Cruzamento cruzamentoD = new Cruzamento(null, "Cruzamento D (Destino)", -23.58, -46.66);

            cruzamentoRepo.save(cruzamentoA);
            cruzamentoRepo.save(cruzamentoB);
            cruzamentoRepo.save(cruzamentoC);
            cruzamentoRepo.save(cruzamentoD);

            // 2. Criando as Vias (Arestas do Grafo)
            // ROTA 1: Passando por B (Mais rápida no papel, mas BD está alagada)
            Via viaAB = new Via(null, "Avenida Principal - Trecho 1", cruzamentoA, cruzamentoB, 10.0, 1.0, false);
            Via viaBD = new Via(null, "Avenida Principal - Trecho 2", cruzamentoB, cruzamentoD, 10.0, 1.0, true); // ALAGADA = true

            // ROTA 2: Passando por C (Mais longa, porém totalmente seca)
            Via viaAC = new Via(null, "Rua Secundária - Trecho 1", cruzamentoA, cruzamentoC, 15.0, 1.0, false);
            Via viaCD = new Via(null, "Rua Secundária - Trecho 2", cruzamentoC, cruzamentoD, 15.0, 1.0, false);

            viaRepo.save(viaAB);
            viaRepo.save(viaBD);
            viaRepo.save(viaAC);
            viaRepo.save(viaCD);

            System.out.println("====== MAPA DE TESTE URBANO CARREGADO COM SUCESSO! ======");
        };
    }
}