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
