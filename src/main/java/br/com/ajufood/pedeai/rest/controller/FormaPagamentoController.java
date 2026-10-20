package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.FormaPagamentoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.FormaPagamentoResponseDTO;
import br.com.ajufood.pedeai.service.FormaPagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {

    @Autowired
    private FormaPagamentoService formaPagamentoService;

    @Operation(summary = "Busca forma de pagamento pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<FormaPagamentoResponseDTO> obterPorId(
            @PathVariable int id) {

        FormaPagamentoResponseDTO formaPagamento =
                formaPagamentoService.obterPorId(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(formaPagamento);
    }

    @Operation(summary = "Lista todas as formas de pagamento")
    @GetMapping
    public ResponseEntity<List<FormaPagamentoResponseDTO>> obterTodos() {

        List<FormaPagamentoResponseDTO> formasPagamento =
                formaPagamentoService.obterTodos();

        return ResponseEntity.ok(formasPagamento);
    }

    @Operation(summary = "Cadastra uma nova forma de pagamento")
    @ApiResponse(responseCode = "201",
            description = "Forma de pagamento criada com sucesso")
    @PostMapping
    public ResponseEntity<FormaPagamentoResponseDTO> salvar(
            @Valid @RequestBody FormaPagamentoRequestDTO formaPagamentoDTO) {

        FormaPagamentoResponseDTO formaPagamentoNova =
                formaPagamentoService.salvar(formaPagamentoDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(formaPagamentoNova);
    }

    @Operation(summary = "Atualiza forma de pagamento")
    @PutMapping("/{id}")
    public ResponseEntity<FormaPagamentoResponseDTO> atualizar(
            @PathVariable int id,
            @Valid @RequestBody FormaPagamentoRequestDTO formaPagamentoDTO) {

        FormaPagamentoResponseDTO formaPagamentoAtualizada =
                formaPagamentoService.atualizar(id, formaPagamentoDTO);

        return ResponseEntity.ok(formaPagamentoAtualizada);
    }

    @Operation(summary = "Remove forma de pagamento")
    @ApiResponse(responseCode = "204",
            description = "Forma de pagamento removida com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {

        formaPagamentoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}