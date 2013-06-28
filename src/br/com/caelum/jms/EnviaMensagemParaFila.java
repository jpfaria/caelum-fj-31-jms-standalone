package br.com.caelum.jms;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class EnviaMensagemParaFila {

	public static void main(String[] args) throws NamingException, JMSException {
		
		Properties jndiProperties = new Properties();
		jndiProperties.put(Context.SECURITY_PRINCIPAL, "jms");
		jndiProperties.put(Context.SECURITY_CREDENTIALS, "caelum");
		
		InitialContext ic = new InitialContext(jndiProperties);
		
		QueueConnectionFactory qcf = (QueueConnectionFactory) ic.lookup("jms/RemoteConnectionFactory");
		
		QueueConnection qc = qcf.createQueueConnection("jms", "caelum");
		
		QueueSession qs = qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		
		TextMessage tm = qs.createTextMessage();
		tm.setText("Mensagem de texto para um Queue");
		
		Queue q = (Queue) ic.lookup("jms/queue/loja");
		
		QueueSender sender = qs.createSender(q);
		sender.send(tm);
		
		System.out.println("Enviando mensagens");
		
		qc.close();

	}

}
