package com.jhonatan.ecommerce_api.service;

import com.jhonatan.ecommerce_api.dto.pedido.PedidoResponseDTO;
import com.jhonatan.ecommerce_api.enums.StatusPedido;
import com.jhonatan.ecommerce_api.enums.TipoUsuario;
import com.jhonatan.ecommerce_api.exception.IdPedidoNotFoundException;
import com.jhonatan.ecommerce_api.exception.PedidoStatusInvalidoException;
import com.jhonatan.ecommerce_api.exception.RegraDeNegocioException;
import com.jhonatan.ecommerce_api.mapper.PedidoMapper;
import com.jhonatan.ecommerce_api.model.Categoria;
import com.jhonatan.ecommerce_api.model.ItemPedido;
import com.jhonatan.ecommerce_api.model.Pedido;
import com.jhonatan.ecommerce_api.model.Produto;
import com.jhonatan.ecommerce_api.model.Usuario;
import com.jhonatan.ecommerce_api.repository.PedidoRepository;
import com.jhonatan.ecommerce_api.repository.ProdutoRepository;
import com.jhonatan.ecommerce_api.validation.ValidadorCriacaoPedido;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PedidoServiceAtualizarStatusTest {

    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private PedidoMapper pedidoMapper;
    @Mock
    private ValidadorCriacaoPedido validador;

    private Usuario dono;
    private Usuario admin;
    private Usuario outroUsuario;
    private Produto produto;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        pedidoService = new PedidoService(
                pedidoRepository,
                produtoRepository,
                pedidoMapper,
                List.of(validador)
        );

        dono = new Usuario("Cliente Dono", "dono@teste.com", "senha123", TipoUsuario.CLIENTE);
        admin = new Usuario("Admin", "admin@teste.com", "senha123", TipoUsuario.ADMIN);
        outroUsuario = new Usuario("Outro Cliente", "outro@teste.com", "senha123", TipoUsuario.CLIENTE);

        ReflectionTestUtils.setField(dono, "id", 1L);
        ReflectionTestUtils.setField(admin, "id", 2L);
        ReflectionTestUtils.setField(outroUsuario, "id", 3L);

        Categoria categoria = new Categoria("Eletrônicos", "Produtos eletrônicos em geral");
        produto = new Produto("Nome", "Descrição",
                new BigDecimal("150.00"), 10, categoria, null);

        pedido = new Pedido(dono);
        pedido.adicionarItem(new ItemPedido(produto, 2, produto.getPreco()));
        // pedido nasce com status PENDENTE (regra do construtor de Pedido)
    }

    @Test
    void adminDeveConseguirAvancarStatusParaQualquerTransicaoValida() {
        //ARRANGE
        BDDMockito.given(pedidoRepository.findById(1L)).willReturn(Optional.of(pedido));
        BDDMockito.given(pedidoMapper.toDTO(pedido)).willReturn(
                new PedidoResponseDTO(1L, null, null, StatusPedido.AGUARDANDO_PAGAMENTO, null, null)
        );

        //ACT
        PedidoResponseDTO resultado = pedidoService.atualizarStatus(1L, StatusPedido.AGUARDANDO_PAGAMENTO, admin);

        //ASSERT
        Assertions.assertEquals(StatusPedido.AGUARDANDO_PAGAMENTO, resultado.statusDePagamento());
        Assertions.assertEquals(StatusPedido.AGUARDANDO_PAGAMENTO, pedido.getStatusPedido());
    }

    @Test
    void donoDevePoderCancelarOProprioPedidoIndependenteDoStatusEnviado() {
        //ARRANGE — mesmo se o dono mandar um status diferente, deve virar CANCELADO
        BDDMockito.given(pedidoRepository.findById(1L)).willReturn(Optional.of(pedido));
        BDDMockito.given(pedidoMapper.toDTO(pedido)).willReturn(
                new PedidoResponseDTO(1L, null, null, StatusPedido.CANCELADO, null, null)
        );

        //ACT
        pedidoService.atualizarStatus(1L, StatusPedido.ENTREGUE, dono);

        //ASSERT
        Assertions.assertEquals(StatusPedido.CANCELADO, pedido.getStatusPedido());
    }

    @Test
    void deveEstornarEstoqueQuandoPedidoForCancelado() {
        //ARRANGE
        BDDMockito.given(pedidoRepository.findById(1L)).willReturn(Optional.of(pedido));
        BDDMockito.given(pedidoMapper.toDTO(pedido)).willReturn(
                new PedidoResponseDTO(1L, null, null, StatusPedido.CANCELADO, null, null)
        );

        //ACT
        pedidoService.atualizarStatus(1L, StatusPedido.CANCELADO, dono);

        //ASSERT
        Assertions.assertEquals(10, produto.getEstoque()); // 8 + 2 devolvidos
        BDDMockito.then(produtoRepository).should().save(produto);
    }

    @Test
    void naoDeveEstornarEstoqueQuandoTransicaoNaoForCancelamento() {
        //ARRANGE
        BDDMockito.given(pedidoRepository.findById(1L)).willReturn(Optional.of(pedido));
        BDDMockito.given(pedidoMapper.toDTO(pedido)).willReturn(
                new PedidoResponseDTO(1L, null, null, StatusPedido.AGUARDANDO_PAGAMENTO, null, null)
        );

        //ACT
        pedidoService.atualizarStatus(1L, StatusPedido.AGUARDANDO_PAGAMENTO, admin);

        //ASSERT
        Assertions.assertEquals(8, produto.getEstoque()); // não deve ter mudado
        BDDMockito.then(produtoRepository).should(BDDMockito.never()).save(any());
    }

    @Test
    void deveLancarRegraDeNegocioExceptionQuandoUsuarioNaoForDonoNemAdmin() {
        //ARRANGE
        BDDMockito.given(pedidoRepository.findById(1L)).willReturn(Optional.of(pedido));

        //ACT + ASSERT
        assertThrows(
                RegraDeNegocioException.class,
                () -> pedidoService.atualizarStatus(1L, StatusPedido.CANCELADO, outroUsuario)
        );

        // status não deve ter sido alterado
        Assertions.assertEquals(StatusPedido.PENDENTE, pedido.getStatusPedido());
        BDDMockito.then(produtoRepository).should(BDDMockito.never()).save(any());
    }

    @Test
    void deveLancarPedidoStatusInvalidoExceptionQuandoTransicaoForInvalida() {
        //ARRANGE — PENDENTE não pode ir direto para ENTREGUE
        BDDMockito.given(pedidoRepository.findById(1L)).willReturn(Optional.of(pedido));

        //ACT + ASSERT
        assertThrows(
                PedidoStatusInvalidoException.class,
                () -> pedidoService.atualizarStatus(1L, StatusPedido.ENTREGUE, admin)
        );

        Assertions.assertEquals(StatusPedido.PENDENTE, pedido.getStatusPedido());
    }

    @Test
    void deveLancarIdPedidoNotFoundExceptionQuandoPedidoNaoExistir() {
        //ARRANGE
        BDDMockito.given(pedidoRepository.findById(99L)).willReturn(Optional.empty());

        //ACT + ASSERT
        assertThrows(
                IdPedidoNotFoundException.class,
                () -> pedidoService.atualizarStatus(99L, StatusPedido.CANCELADO, admin)
        );
    }
}