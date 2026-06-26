package com.jhonatan.ecommerce_api.validation;

import com.jhonatan.ecommerce_api.dto.pedido.PedidoRequestDTO;
import com.jhonatan.ecommerce_api.exception.RegraDeNegocioException;
import com.jhonatan.ecommerce_api.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidarUsuarioExistente implements ValidadorCriacaoPedido {

    private final UsuarioRepository usuarioRepository;

    public ValidarUsuarioExistente(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void validar(PedidoRequestDTO dadosPedido) {

        if (dadosPedido.usuarioId() == null) {
            throw new RegraDeNegocioException("Usuário obrigatório para o pedido");
        }

        var usuario = usuarioRepository.findById(dadosPedido.usuarioId())
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado"));

        if (!usuario.isAtivo()) {
            throw new RegraDeNegocioException("Usuário inativo");
        }
    }
}
