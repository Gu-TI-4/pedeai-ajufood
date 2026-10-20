package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.EnderecoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.EnderecoResponseDTO;
import br.com.ajufood.pedeai.service.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @Operation(summary = "Busca endereço pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> obterPorId(
            @PathVariable int id) {

        EnderecoResponseDTO endereco =
                enderecoService.obterPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(endereco);
    }

    @Operation(summary = "Lista todos os endereços")
    @GetMapping
    public ResponseEntity<List<EnderecoResponseDTO>> obterTodos() {

        List<EnderecoResponseDTO> enderecos =
                enderecoService.obterTodos();

        return ResponseEntity.ok(enderecos);
    }

    @Operation(summary = "Cadastra um novo endereço")
    @ApiResponse(responseCode = "201",
            description = "Endereço criado com sucesso")
    @PostMapping
    public ResponseEntity<EnderecoResponseDTO> salvar(
            @Valid @RequestBody EnderecoRequestDTO enderecoDTO) {

        EnderecoResponseDTO enderecoNovo =
                enderecoService.salvar(enderecoDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enderecoNovo);
    }

    @Operation(summary = "Atualiza endereço")
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> atualizar(
            @PathVariable int id,
            @Valid @RequestBody EnderecoRequestDTO enderecoDTO) {

        EnderecoResponseDTO enderecoAtualizado =
                enderecoService.atualizar(id, enderecoDTO);

        return ResponseEntity.ok(enderecoAtualizado);
    }

    @Operation(summary = "Remove endereço")
    @ApiResponse(responseCode = "204",
            description = "Endereço removido com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {

        enderecoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}