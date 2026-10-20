package br.com.ajufood.pedeai.rest.controller;
import br.com.ajufood.pedeai.rest.dto.response.VendaCategoriaResponseDTO;
import br.com.ajufood.pedeai.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/relatorios")

public class RelatorioController {
    @Autowired
    private PedidoService pedidoService;

    @Operation(summary = "Relatório de vendas por categoria")
    @GetMapping("/vendas-por-categoria")
    public ResponseEntity<List<VendaCategoriaResponseDTO>> obterRelatorioVendasPorCategoria(

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataInicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataFim) {

        List<VendaCategoriaResponseDTO> relatorio =
                pedidoService.obterRelatorioVendasPorCategoria(
                        dataInicio,
                        dataFim);

        return ResponseEntity.ok(relatorio);
    }
}
