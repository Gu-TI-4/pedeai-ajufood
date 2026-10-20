package br.com.ajufood.pedeai.rest.controller;

import br.com.ajufood.pedeai.rest.dto.request.ItensPedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ItensPedidoResponseDTO;
import br.com.ajufood.pedeai.service.ItensPedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itens-pedido")
public class ItensPedidoController {

    @Autowired
    private ItensPedidoService itensPedidoService;

    @Operation(summary = "Busca item do pedido pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<ItensPedidoResponseDTO> obterPorId(
            @PathVariable long id) {

        ItensPedidoResponseDTO item =
                itensPedidoService.obterPorId(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(item);
    }

    @Operation(summary = "Lista todos os itens do pedido")
    @GetMapping
    public ResponseEntity<List<ItensPedidoResponseDTO>> obterTodos() {

        List<ItensPedidoResponseDTO> itens =
                itensPedidoService.obterTodos();

        return ResponseEntity.ok(itens);
    }

    @Operation(summary = "Cadastra um novo item do pedido")
    @ApiResponse(
            responseCode = "201",
            description = "Item do pedido criado com sucesso"
    )
    @PostMapping
    public ResponseEntity<ItensPedidoResponseDTO> salvar(
            @Valid @RequestBody ItensPedidoRequestDTO itemDTO) {

        ItensPedidoResponseDTO itemNovo =
                itensPedidoService.salvar(itemDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(itemNovo);
    }

    @Operation(summary = "Atualiza item do pedido")
    @PutMapping("/{id}")
    public ResponseEntity<ItensPedidoResponseDTO> atualizar(
            @PathVariable long id,
            @Valid @RequestBody ItensPedidoRequestDTO itemDTO) {

        ItensPedidoResponseDTO itemAtualizado =
                itensPedidoService.atualizar(id, itemDTO);

        return ResponseEntity.ok(itemAtualizado);
    }

    @Operation(summary = "Remove item do pedido")
    @ApiResponse(
            responseCode = "204",
            description = "Item do pedido removido com sucesso"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable long id) {

        itensPedidoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}