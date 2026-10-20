package br.com.ajufood.pedeai.repository;

import br.com.ajufood.pedeai.model.ClienteModel;
import br.com.ajufood.pedeai.model.PagamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository<PagamentoModel, Integer> {
}
