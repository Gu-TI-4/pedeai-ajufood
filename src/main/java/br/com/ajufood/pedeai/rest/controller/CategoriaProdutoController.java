package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.CategoriaProdutoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.CategoriaProdutoResponseDTO;
import br.com.ajufood.pedeai.service.CategoriaProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaProdutoController {

    @Autowired
    private CategoriaProdutoService categoriaProdutoService;

    @Operation(summary = "Busca categoria pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaProdutoResponseDTO> obterPorId(
            @PathVariable int id) {

        CategoriaProdutoResponseDTO categoria =
                categoriaProdutoService.obterPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(categoria);
    }

    @Operation(summary = "Lista todas as categorias")
    @GetMapping
    public ResponseEntity<List<CategoriaProdutoResponseDTO>> obterTodos() {

        List<CategoriaProdutoResponseDTO> categorias =
                categoriaProdutoService.obterTodos();

        return ResponseEntity.ok(categorias);
    }

    @Operation(summary = "Cadastra uma nova categoria")
    @ApiResponse(responseCode = "201",
            description = "Categoria criada com sucesso")
    @PostMapping
    public ResponseEntity<CategoriaProdutoResponseDTO> salvar(
            @Valid @RequestBody CategoriaProdutoRequestDTO categoriaDTO) {

        CategoriaProdutoResponseDTO categoriaNova =
                categoriaProdutoService.salvar(categoriaDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriaNova);
    }

    @Operation(summary = "Atualiza categoria")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaProdutoResponseDTO> atualizar(
            @PathVariable int id,
            @Valid @RequestBody CategoriaProdutoRequestDTO categoriaDTO) {

        CategoriaProdutoResponseDTO categoriaAtualizada =
                categoriaProdutoService.atualizar(id, categoriaDTO);

        return ResponseEntity.ok(categoriaAtualizada);
    }

    @Operation(summary = "Remove categoria")
    @ApiResponse(responseCode = "204",
            description = "Categoria removida com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {

        categoriaProdutoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}