package br.com.caelum.jms;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EnviaMensagemParaTopico {

	public static void main(String[] args) throws NamingException, JMSException {

		Properties jndiProperties = new Properties();
		jndiProperties.put(Context.SECURITY_PRINCIPAL, "jms");
		jndiProperties.put(Context.SECURITY_CREDENTIALS, "caelum");

		InitialContext ic = new InitialContext(jndiProperties);

		TopicConnectionFactory tcf = (TopicConnectionFactory) ic
				.lookup("jms/RemoteConnectionFactory");

		TopicConnection tc = tcf.createTopicConnection("jms", "caelum");

		TopicSession ts = tc
				.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

		Topic t = (Topic) ic.lookup("jms/topic/loja");

		TopicPublisher sender = ts.createPublisher(t);

		TextMessage tm1 = ts.createTextMessage();
		tm1.setText("Mensagem de texto para um Topic para o financeiro");
		tm1.setStringProperty("comDestinatario", "financeiro");
		
		sender.send(tm1);
		
		// Esta mensagem nao deve aparecer no console
		TextMessage tm2 = ts.createTextMessage();
		tm2.setText("Mensagem de texto para um Topic para o fiscal");
		tm2.setStringProperty("comDestinatario", "fiscal");
		
		sender.send(tm2);

		System.out.println("Enviando mensagens");

		tc.close();

	}

}
