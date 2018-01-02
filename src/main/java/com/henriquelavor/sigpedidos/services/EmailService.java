package com.henriquelavor.sigpedidos.services;

import org.springframework.mail.SimpleMailMessage;

import com.henriquelavor.sigpedidos.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);

}
