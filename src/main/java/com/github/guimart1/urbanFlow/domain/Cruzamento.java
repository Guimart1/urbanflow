package com.github.guimart1.urbanFlow.domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_cruzamento")
@Data // Gera automaticamente Getters, Setters, toString, equals e hashCode
@NoArgsConstructor // Gera o construtor vazio exigido pelo Hibernate
@AllArgsConstructor // Gera um construtor com todos os campos
public class Cruzamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do cruzamento ou ponto de referência é obrigatório")
    @Column(nullable = false, unique = true)
    private String nome;

    // Guardaremos a localização básica por enquanto.
    // Mais para frente podemos evoluir para tipos geométricos complexos.
    private Double latitude;
    private Double longitude;
}