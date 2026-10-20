package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.PedidoStatusPatchRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoStatusResponseDTO;

import br.com.ajufood.pedeai.rest.dto.request.PedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoResponseDTO;
import br.com.ajufood.pedeai.rest.dto.request.PedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.request.PedidoComItensRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoResponseDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoComItensResponseDTO;
import br.com.ajufood.pedeai.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Operation(summary = "Busca pedido pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obterPorId(
            @PathVariable int id) {

        PedidoResponseDTO pedido =
                pedidoService.obterPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(pedido);
    }

    @Operation(summary = "Lista todos os pedidos")
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> obterTodos() {

        List<PedidoResponseDTO> pedidos =
                pedidoService.obterTodos();

        return ResponseEntity.ok(pedidos);
    }

    @Operation(summary = "Cadastra um novo pedido")
    @ApiResponse(responseCode = "201",
            description = "Pedido criado com sucesso")
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> salvar(
            @Valid @RequestBody PedidoRequestDTO pedidoDTO) {

        PedidoResponseDTO pedidoNovo =
                pedidoService.salvar(pedidoDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoNovo);
    }

    @Operation(summary = "Cria um novo pedido com itens e calcula automaticamente")
    @ApiResponse(responseCode = "201",
            description = "Pedido criado com sucesso")
    @PostMapping("/comitens")
    public ResponseEntity<PedidoComItensResponseDTO> salvarComItens(
            @Valid @RequestBody PedidoComItensRequestDTO pedidoComItensDTO) {

        PedidoComItensResponseDTO pedidoNovo =
                pedidoService.salvarComItens(pedidoComItensDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoNovo);
    }

    @Operation(summary = "Atualiza pedido")
    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> atualizar(
            @PathVariable int id,
            @Valid @RequestBody PedidoRequestDTO pedidoDTO) {

        PedidoResponseDTO pedidoAtualizado =
                pedidoService.atualizar(id, pedidoDTO);

        return ResponseEntity.ok(pedidoAtualizado);
    }

    @Operation(summary = "Remove pedido")
    @ApiResponse(responseCode = "204",
            description = "Pedido removido com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {

        pedidoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Atualiza o status de um pedido")
    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso")
    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoStatusResponseDTO> atualizarStatus(
            @PathVariable int id,
            @Valid @RequestBody PedidoStatusPatchRequestDTO statusPatchDTO) {

        PedidoStatusResponseDTO statusAtualizado =
                pedidoService.atualizarStatus(id, statusPatchDTO);

        return ResponseEntity.status(HttpStatus.OK).body(statusAtualizado);
    }
}