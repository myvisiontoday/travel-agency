package booking.client.model;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MessagingReceiveGateway {
    Connection connection; // to connect to the ActiveMQ

    Session session = null; // session for creating messages, producers and

    private Destination receiveDestination; // reference to a question queue destination
    private MessageConsumer messageConsumer = null; // for receiving message

    public MessagingReceiveGateway() {
    }

    public MessagingReceiveGateway(String MESSAGE_QUEUE) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        connection = connectionFactory.createConnection();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        receiveDestination = session.createQueue(MESSAGE_QUEUE);

        messageConsumer = session.createConsumer(receiveDestination);
        connection.start();
    }

    public void SetListener(MessageListener messageListener) throws JMSException {
        messageConsumer.setMessageListener(messageListener);
    }
    public Destination getReceiveDestination() {
        return receiveDestination;
    }
    public void setReceiveDestination(Destination receiveDestination) {
        this.receiveDestination = receiveDestination;
    }
}
