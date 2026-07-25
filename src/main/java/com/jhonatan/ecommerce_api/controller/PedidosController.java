package com.jhonatan.ecommerce_api.controller;

import com.jhonatan.ecommerce_api.dto.pedido.AtualizarPedidoRequestDTO;
import com.jhonatan.ecommerce_api.dto.pedido.PedidoRequestDTO;
import com.jhonatan.ecommerce_api.dto.pedido.PedidoResponseDTO;
import com.jhonatan.ecommerce_api.enums.StatusPedido;
import com.jhonatan.ecommerce_api.model.Usuario;
import com.jhonatan.ecommerce_api.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidosController {

    private final PedidoService pedidoService;

    public PedidosController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> createPedido(@RequestBody @Valid PedidoRequestDTO pedidoRequestDTO,
                                                          @AuthenticationPrincipal Usuario usuarioLogado) {
        PedidoResponseDTO pedido = pedidoService.criarPedido(pedidoRequestDTO, usuarioLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @GetMapping
    public ResponseEntity<Page<PedidoResponseDTO>> listarPedidos(
            @RequestParam(required = false) StatusPedido status,
            Pageable pageable) {
        return ResponseEntity.ok(pedidoService.listarTodosPedidos(status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPedidoPorId(@PathVariable Long id,
                                                               @AuthenticationPrincipal Usuario usuarioLogado) {
        return ResponseEntity.ok(pedidoService.buscarPedidoPorId(id, usuarioLogado));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Page<PedidoResponseDTO>> listarPedidosUsuarioPorId(
            @PathVariable Long usuarioId,
            @RequestParam(required = false) StatusPedido status,
            @AuthenticationPrincipal Usuario usuarioLogado,
            Pageable pageable) {
        return ResponseEntity.ok(pedidoService.listarPedidosUsuario(usuarioId, status, usuarioLogado, pageable));
    }

    @PatchMapping("/{idPedido}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable Long idPedido,
                                                             @RequestBody @Valid AtualizarPedidoRequestDTO dto,
                                                             @AuthenticationPrincipal Usuario usuarioAutenticado) {
        PedidoResponseDTO pedido = pedidoService.atualizarStatus(idPedido, dto.novoStatus(), usuarioAutenticado);
        return ResponseEntity.ok(pedido);
    }
}