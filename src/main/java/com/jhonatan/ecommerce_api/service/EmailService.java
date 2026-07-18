package com.jhonatan.ecommerce_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailDeRecuperacao(String destinatario, String token) {

        // Monta o link que o usuário vai clicar no email.
        // O frontend lê o ?token= da URL e exibe o formulário de nova senha.
        String link = frontendUrl + "/reset-senha?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("Recuperação de senha");
        message.setText("""
                Olá!

                Recebemos uma solicitação para redefinir a senha da sua conta.
                Clique no link abaixo para continuar (válido por 30 minutos):

                %s

                Se você não solicitou a recuperação de senha, ignore este email.
                Sua senha permanece a mesma.
                """.formatted(link));

        mailSender.send(message);
    }

    public void enviarEmailDeConfirmacao(String destinatario, String token) {
        String link = frontendUrl + "/confirmar-conta?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("Confirme sua conta");
        message.setText("""
            Olá!

            Recebemos seu cadastro em nosso E-commerce. Para ativar sua conta e poder fazer login, clique no link abaixo (válido por 24 horas):

            %s

            Se você não fez esse cadastro, ignore este email.
            """.formatted(link));

        mailSender.send(message);
    }
}
