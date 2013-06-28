package br.com.caelum.jms;

import java.util.Properties;
import java.util.Scanner;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class RegistraTratadorNaTopico {

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

		// somente mensagens para o financeiro
		TopicSubscriber tsb = ts.createSubscriber(t, "comDestinatario = 'financeiro'", true);
		tsb.setMessageListener(new TratadorDeMensagem());

		tc.start();

		@SuppressWarnings("resource")
		Scanner teclado = new Scanner(System.in);

		System.out
				.println("Esperando as mensagens no topico. Aperte ENTER para parar");

		teclado.nextLine();

		tc.close();

	}

}
