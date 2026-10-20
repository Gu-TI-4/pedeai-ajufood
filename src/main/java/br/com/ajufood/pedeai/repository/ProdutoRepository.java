package br.com.ajufood.pedeai.repository;

import br.com.ajufood.pedeai.model.ClienteModel;
import br.com.ajufood.pedeai.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoModel, Integer> {

    @Query("SELECT p FROM ProdutoModel p JOIN FETCH p.categoriaProduto c " +
            "WHERE p.disponivel = true " +
            "AND (:categoriaId IS NULL OR c.id = :categoriaId)")
    List<ProdutoModel> buscarDisponiveisPorCategoria(@Param("categoriaId") Integer categoriaId);

}
