package com.github.guimart1.urbanFlow.repository;

import com.github.guimart1.urbanFlow.domain.Cruzamento;
import com.github.guimart1.urbanFlow.domain.Via;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViaRepository extends JpaRepository<Via, Long> {
    List<Via> findByOrigem(Cruzamento origem);
}