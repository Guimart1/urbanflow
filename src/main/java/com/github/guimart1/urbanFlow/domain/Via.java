package com.github.guimart1.urbanFlow.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_via")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Via {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da rua ou avenida é obrigatório")
    @Column(nullable = false)
    private String nome;

    // Ponto de partida da rua (Origem)
    @NotNull(message = "O cruzamento de origem é obrigatório")
    @ManyToOne
    @JoinColumn(name = "origem_id", nullable = false)
    private Cruzamento origem;

    // Ponto de chegada da rua (Destino)
    @NotNull(message = "O cruzamento de destino é obrigatório")
    @ManyToOne
    @JoinColumn(name = "destino_id", nullable = false)
    private Cruzamento destino;

    // Peso base: Distância em metros ou tempo estimado em minutos com trânsito livre
    @NotNull(message = "O peso/distância da via é obrigatório")
    @Positive(message = "O peso deve ser um valor maior que zero")
    private Double pesoBase;

    // Fator de trânsito multiplicador (1.0 = livre, 2.0 = trânsito lento, etc.)
    private Double multiplicadorTransito = 1.0;

    // Aqui está o que você pediu: o controle de alagamento direto na entidade
    private Boolean alagada = false;

    // Método para calcular o custo real da rota em tempo real para o algoritmo de Dijkstra
    public Double getPesoAtual() {
        if (this.alagada) {
            // Se estiver alagada, o peso vai para o "infinito", fazendo o algoritmo desviar dela
            return Double.MAX_VALUE;
        }
        return this.pesoBase * this.multiplicadorTransito;
    }
}