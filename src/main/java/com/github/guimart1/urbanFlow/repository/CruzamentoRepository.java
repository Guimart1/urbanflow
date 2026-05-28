package com.github.guimart1.urbanFlow.repository;

import com.github.guimart1.urbanFlow.domain.Cruzamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CruzamentoRepository extends JpaRepository<Cruzamento, Long> {
    Optional<Cruzamento> findByNome(String nome);
}