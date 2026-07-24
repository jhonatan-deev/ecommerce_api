package com.jhonatan.ecommerce_api.service;

import com.jhonatan.ecommerce_api.dto.pedido.ItemPedidoRequestDTO;
import com.jhonatan.ecommerce_api.dto.pedido.PedidoRequestDTO;
import com.jhonatan.ecommerce_api.dto.pedido.PedidoResponseDTO;
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
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public PedidoResponseDTO buscarPedidoPorId(Long idPedido, Usuario usuarioAutenticado) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(
                () -> new IdPedidoNotFoundException("Pedido não encontrado!"));
        if(usuarioAutenticado.getId().equals(pedido.getUsuario().getId()) || usuarioAutenticado.getTipo() == TipoUsuario.ADMIN){
            return pedidoMapper.toDTO(pedido);
        }
        throw new RegraDeNegocioException("Você não tem permissão para acessar este pedido.");

    }

    public Page<PedidoResponseDTO> listarPedidosUsuario(
            Long usuarioId,
            Usuario usuarioAutenticado,
            Pageable pageable) {

        if (usuarioAutenticado.getTipo() != TipoUsuario.ADMIN
                && !usuarioId.equals(usuarioAutenticado.getId())) {
            throw new RegraDeNegocioException("Você não tem permissão para visualizar esses pedidos.");
        }
        Page<Pedido> pedidos = pedidoRepository.findByUsuarioId(usuarioId, pageable);
        return pedidos.map(pedidoMapper::toDTO);
    }

}