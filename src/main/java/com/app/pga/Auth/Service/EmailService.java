package com.app.pga.Auth.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.Encoder;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    public void sendEmail(String destinatario, String asunto, String contrasena) {
        //nueva inidostancia de tipo SimpleMailMessage
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject(asunto);
        message.setText(
                "Usuario: " + destinatario + "\n\n" +
                        "Contraseña temporal: " + contrasena + "\n\n" +
                        "Debes cambiarla al iniciar sesión."
        );
        message.setFrom("aplataformagestioneducativa@gmail.com");

        emailSender.send(message);
    }
}
