package com.jhonatan.ecommerce_api.service;

import com.jhonatan.ecommerce_api.dto.pedido.ItemPedidoRequestDTO;
import com.jhonatan.ecommerce_api.dto.pedido.PedidoRequestDTO;
import com.jhonatan.ecommerce_api.dto.pedido.PedidoResponseDTO;
import com.jhonatan.ecommerce_api.enums.TipoUsuario;
import com.jhonatan.ecommerce_api.exception.IdProdutoNotFoundException;
import com.jhonatan.ecommerce_api.mapper.PedidoMapper;
import com.jhonatan.ecommerce_api.model.Categoria;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    // PedidoService recebe List<ValidadorCriacaoPedido> no construtor — o
    // Mockito não resolve esse tipo genérico sozinho via @InjectMocks, então
    // montamos o service manualmente no setUp(), passando o mock dentro de
    // uma List.of(...). O resultado é o mesmo objeto que o @InjectMocks
    // produziria nos demais casos.
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private PedidoMapper pedidoMapper;
    @Mock
    private ValidadorCriacaoPedido validador;

    private Usuario usuario;
    private Produto produto;

    @BeforeEach
    void setUp() {
        pedidoService = new PedidoService(
                pedidoRepository,
                produtoRepository,
                pedidoMapper,
                List.of(validador)
        );

        usuario = new Usuario("Cliente Teste", "cliente@teste.com", "senha123", TipoUsuario.CLIENTE);

        Categoria categoria = new Categoria("Eletrônicos", "Produtos eletrônicos em geral");
        produto = new Produto("Mouse Gamer", "Mouse com sensor óptico",
                new BigDecimal("150.00"), 10, categoria);
    }

    @Test
    void deveCriarPedidoERetornarPedidoResponseDTOQuandoDadosForemValidos() {
        //ARRANGE
        ItemPedidoRequestDTO itemDto = new ItemPedidoRequestDTO(1L, 2);
        PedidoRequestDTO requestDTO = new PedidoRequestDTO(List.of(itemDto));

        PedidoResponseDTO responseDTO = new PedidoResponseDTO(
                1L, null, null, null, new BigDecimal("300.00"), null
        );

        BDDMockito.given(produtoRepository.findById(1L)).willReturn(Optional.of(produto));
        BDDMockito.given(pedidoRepository.save(any(Pedido.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
        BDDMockito.given(pedidoMapper.toDTO(any(Pedido.class))).willReturn(responseDTO);

        //ACT
        PedidoResponseDTO resultado = pedidoService.criarPedido(requestDTO, usuario);

        //ASSERT
        Assertions.assertEquals(1L, resultado.id());
        Assertions.assertEquals(new BigDecimal("300.00"), resultado.valorTotalPedido());
        Assertions.assertEquals(8, produto.getEstoque()); // 10 - 2

        BDDMockito.then(validador).should().validar(requestDTO);
        BDDMockito.then(produtoRepository).should().save(produto);
        BDDMockito.then(pedidoRepository).should().save(any(Pedido.class));
    }

    @Test
    void deveLancarIdProdutoNotFoundExceptionQuandoProdutoNaoExistir() {
        //ARRANGE
        ItemPedidoRequestDTO itemDto = new ItemPedidoRequestDTO(999L, 1);
        PedidoRequestDTO requestDTO = new PedidoRequestDTO(List.of(itemDto));

        BDDMockito.given(produtoRepository.findById(999L)).willReturn(Optional.empty());

        //ACT + ASSERT
        IdProdutoNotFoundException exception = assertThrows(
                IdProdutoNotFoundException.class,
                () -> pedidoService.criarPedido(requestDTO, usuario)
        );
        Assertions.assertTrue(exception.getMessage().contains("999"));

        BDDMockito.then(validador).should().validar(requestDTO);
        BDDMockito.then(pedidoRepository).should(BDDMockito.never()).save(any());
    }

    @Test
    void deveLancarIllegalStateExceptionQuandoEstoqueForInsuficiente() {
        //ARRANGE — produto tem 10 em estoque, pedido pede 999
        ItemPedidoRequestDTO itemDto = new ItemPedidoRequestDTO(1L, 999);
        PedidoRequestDTO requestDTO = new PedidoRequestDTO(List.of(itemDto));

        BDDMockito.given(produtoRepository.findById(1L)).willReturn(Optional.of(produto));

        //ACT + ASSERT
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> pedidoService.criarPedido(requestDTO, usuario)
        );
        Assertions.assertTrue(exception.getMessage().contains("Estoque insuficiente"));
        Assertions.assertEquals(10, produto.getEstoque()); // não deve ter mudado

        BDDMockito.then(pedidoRepository).should(BDDMockito.never()).save(any());
    }
}