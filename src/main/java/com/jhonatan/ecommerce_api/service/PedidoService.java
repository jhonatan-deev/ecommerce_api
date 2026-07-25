package com.jhonatan.ecommerce_api.service;

import com.jhonatan.ecommerce_api.dto.pedido.ItemPedidoRequestDTO;
import com.jhonatan.ecommerce_api.dto.pedido.PedidoRequestDTO;
import com.jhonatan.ecommerce_api.dto.pedido.PedidoResponseDTO;
import com.jhonatan.ecommerce_api.enums.StatusPedido;
import com.jhonatan.ecommerce_api.enums.TipoUsuario;
import com.jhonatan.ecommerce_api.exception.IdProdutoNotFoundException;
import com.jhonatan.ecommerce_api.exception.IdPedidoNotFoundException;
import com.jhonatan.ecommerce_api.exception.RegraDeNegocioException;
import com.jhonatan.ecommerce_api.mapper.PedidoMapper;
import com.jhonatan.ecommerce_api.model.ItemPedido;
import com.jhonatan.ecommerce_api.model.Pedido;
import com.jhonatan.ecommerce_api.model.Produto;
import com.jhonatan.ecommerce_api.model.Usuario;
import com.jhonatan.ecommerce_api.repository.PedidoRepository;
import com.jhonatan.ecommerce_api.repository.ProdutoRepository;
import com.jhonatan.ecommerce_api.validation.ValidadorCriacaoPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoMapper pedidoMapper;
    private final List<ValidadorCriacaoPedido> validadores;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository,
                         PedidoMapper pedidoMapper, List<ValidadorCriacaoPedido> validadores) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoMapper = pedidoMapper;
        this.validadores = validadores;
    }

    @Transactional
    public PedidoResponseDTO criarPedido(PedidoRequestDTO dadosPedido, Usuario usuarioAutenticado) {
        validadores.forEach(validador -> validador.validar(dadosPedido));

        Pedido pedido = new Pedido(usuarioAutenticado);

        for (ItemPedidoRequestDTO itemDto : dadosPedido.itensDoPedido()) {
            Produto produto = produtoRepository.findById(itemDto.produtoId())
                    .orElseThrow(() -> new IdProdutoNotFoundException(
                            "Produto não encontrado com o ID: " + itemDto.produtoId()));
            produto.saidaEstoque(itemDto.quantidade());
            produtoRepository.save(produto);
            ItemPedido item = new ItemPedido(produto, itemDto.quantidade(), produto.getPreco());
            pedido.adicionarItem(item);
        }

        pedido = pedidoRepository.save(pedido);
        return pedidoMapper.toDTO(pedido);
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPedidoPorId(Long idPedido, Usuario usuarioAutenticado) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(
                () -> new IdPedidoNotFoundException("Pedido não encontrado!"));
        if (usuarioAutenticado.getId().equals(pedido.getUsuario().getId()) || usuarioAutenticado.getTipo() == TipoUsuario.ADMIN) {
            return pedidoMapper.toDTO(pedido);
        }
        throw new RegraDeNegocioException("Você não tem permissão para acessar este pedido.");
    }

    @Transactional
    public PedidoResponseDTO atualizarStatus(Long idPedido, StatusPedido statusPedido, Usuario usuarioAutenticado) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IdPedidoNotFoundException("Pedido não encontrado!"));
        StatusPedido novoStatus;
        if (usuarioAutenticado.getTipo() == TipoUsuario.ADMIN || usuarioAutenticado.getTipo() == TipoUsuario.VENDEDOR) {
            novoStatus = statusPedido;
        } else if (pedido.getUsuario().getId().equals(usuarioAutenticado.getId())) {
            novoStatus = StatusPedido.CANCELADO;
        } else {
            throw new RegraDeNegocioException("Você não tem permissão para alterar este pedido.");
        }
        pedido.alterarStatus(novoStatus);
        if (novoStatus == StatusPedido.CANCELADO) {
            estornarEstoque(pedido);
        }
        return pedidoMapper.toDTO(pedido);
    }

    private void estornarEstoque(Pedido pedido) {
        for (ItemPedido item : pedido.getItensDoPedido()) {
            Produto produto = item.getProduto();
            produto.entradaEstoque(item.getQuantidade());
            produtoRepository.save(produto);
        }
    }

    @Transactional(readOnly = true)
    public Page<PedidoResponseDTO> listarTodosPedidos(StatusPedido status, Pageable pageable) {
        if (status != null) {
            return pedidoRepository.findByStatusPedido(status, pageable).map(pedidoMapper::toDTO);
        }
        return pedidoRepository.findAll(pageable).map(pedidoMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<PedidoResponseDTO> listarPedidosUsuario(Long idUsuario, StatusPedido status, Usuario usuarioAutenticado, Pageable pageable) {
        if (usuarioAutenticado.getTipo() != TipoUsuario.ADMIN && !idUsuario.equals(usuarioAutenticado.getId())) {
            throw new RegraDeNegocioException("Você não tem permissão para visualizar esses pedidos.");
        }
        if (status != null) {
            return pedidoRepository.findByUsuarioIdAndStatusPedido(idUsuario, status, pageable).map(pedidoMapper::toDTO);
        }
        return pedidoRepository.findByUsuarioId(idUsuario, pageable).map(pedidoMapper::toDTO);
    }
}