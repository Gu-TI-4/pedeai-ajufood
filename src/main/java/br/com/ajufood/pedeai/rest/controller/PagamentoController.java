package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.PagamentoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.PagamentoResponseDTO;
import br.com.ajufood.pedeai.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamento")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @Operation(summary = "Busca pagamento pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> obterPorId(
            @PathVariable int id) {

        PagamentoResponseDTO pagamento =
                pagamentoService.obterPorId(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(pagamento);
    }

    @Operation(summary = "Lista todos os pagamentos")
    @GetMapping
    public ResponseEntity<List<PagamentoResponseDTO>> obterTodos() {

        List<PagamentoResponseDTO> pagamentos =
                pagamentoService.obterTodos();

        return ResponseEntity.ok(pagamentos);
    }

    @Operation(summary = "Cadastra um novo pagamento")
    @ApiResponse(
            responseCode = "201",
            description = "Pagamento criado com sucesso"
    )
    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> salvar(
            @Valid @RequestBody PagamentoRequestDTO pagamentoDTO) {

        PagamentoResponseDTO pagamentoNovo =
                pagamentoService.salvar(pagamentoDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pagamentoNovo);
    }

    @Operation(summary = "Atualiza pagamento")
    @PutMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> atualizar(
            @PathVariable int id,
            @Valid @RequestBody PagamentoRequestDTO pagamentoDTO) {

        PagamentoResponseDTO pagamentoAtualizado =
                pagamentoService.atualizar(id, pagamentoDTO);

        return ResponseEntity.ok(pagamentoAtualizado);
    }

    @Operation(summary = "Remove pagamento")
    @ApiResponse(
            responseCode = "204",
            description = "Pagamento removido com sucesso"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {

        pagamentoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}