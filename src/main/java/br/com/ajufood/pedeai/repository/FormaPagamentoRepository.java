package br.com.ajufood.pedeai.repository;

//import br.com.ajufood.pedeai.model.ClienteModel;
import br.com.ajufood.pedeai.model.FormaPagamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamentoModel, Integer> {

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
            "FROM FormaPagamentoModel f WHERE LOWER(f.nome) = LOWER(:nome)")
    boolean existsByNomeIgnoreCase(@Param("nome") String nome);
}
