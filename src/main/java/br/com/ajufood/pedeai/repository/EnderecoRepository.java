package br.com.ajufood.pedeai.repository;


import br.com.ajufood.pedeai.model.ClienteModel;
import br.com.ajufood.pedeai.model.EnderecoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoRepository extends JpaRepository<EnderecoModel, Integer> {

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM EnderecoModel e WHERE e.cliente.id = :clienteId AND e.endereco = :endereco AND e.numero = :numero AND e.cep = :cep")
    boolean existsByClienteIdAndEnderecoAndNumeroAndCep(@Param("clienteId") int clienteId, @Param("endereco") String endereco, @Param("numero") int numero, @Param("cep") String cep);

    long countByClienteId(int clienteId);

    List<EnderecoModel> findByClienteId(int clienteId);

    @Query("SELECT e FROM EnderecoModel e WHERE e.cliente.id = :clienteId AND e.padrao = true")
    EnderecoModel findPadraoByClienteId(@Param("clienteId") int clienteId);
}