package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.ProdutoDisponibilidadeRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ProdutoCategoriaResponseDTO;
import br.com.ajufood.pedeai.rest.dto.request.ProdutoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ProdutoResponseDTO;
import br.com.ajufood.pedeai.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Operation(summary = "Busca produto pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> obterPorId(
            @PathVariable int id) {

        ProdutoResponseDTO produto = produtoService.obterPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(produto);
    }

    @Operation(summary = "Cadastra um novo produto")
    @ApiResponse(responseCode = "201",
            description = "Produto criado com sucesso")
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> salvar(
            @Valid @RequestBody ProdutoRequestDTO produtoDTO) {

        ProdutoResponseDTO produtoNovo =
                produtoService.salvar(produtoDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(produtoNovo);
    }

    @Operation(summary = "Atualiza produto")
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(
            @PathVariable int id,
            @Valid @RequestBody ProdutoRequestDTO produtoDTO) {

        ProdutoResponseDTO produtoAtualizado =
                produtoService.atualizar(id, produtoDTO);

        return ResponseEntity.ok(produtoAtualizado);
    }

    @Operation(summary = "Remove produto")
    @ApiResponse(responseCode = "204",
            description = "Produto removido com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {

        produtoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Lista produtos disponíveis")
    /*com filtro de categoria (UC-01) feitooooooo/*
    /* @GetMapping("/disponiveis") mudei para alterar a url */
    @GetMapping
    public ResponseEntity<List<ProdutoCategoriaResponseDTO>> obterProdutosDisponiveis(
            @RequestParam(required = false, name = "categoriaId") Integer categoriaId) {

        List<ProdutoCategoriaResponseDTO> produtos =
                produtoService.obterDisponiveisPorCategoria(categoriaId);

        return ResponseEntity.ok(produtos);
    }
    @Operation(summary = "Ativa ou desativa a disponibilidade de um produto")
    @PatchMapping("/{produtoId}/disponibilidade")
    public ResponseEntity<ProdutoResponseDTO> atualizarDisponibilidade(
            @PathVariable Integer produtoId,
            @RequestBody ProdutoDisponibilidadeRequestDTO disponibilidadeDTO) {

        ProdutoResponseDTO produtoAtualizado = produtoService.atualizarDisponibilidade(
                produtoId,
                disponibilidadeDTO);

        return ResponseEntity.ok(produtoAtualizado);
    }
}