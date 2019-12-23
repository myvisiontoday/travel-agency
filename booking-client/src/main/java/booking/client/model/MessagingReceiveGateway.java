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

    /**
     * connects to the activemq server.
     * @param MESSAGE_QUEUE
     * @throws JMSException
     */
    public MessagingReceiveGateway(String MESSAGE_QUEUE) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        connection = connectionFactory.createConnection();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        receiveDestination = session.createQueue(MESSAGE_QUEUE);

        messageConsumer = session.createConsumer(receiveDestination);
        connection.start();
    }

    /**
     * sets the message listener for the consumer message.
     * @param messageListener
     * @throws JMSException
     */
    public void SetListener(MessageListener messageListener) throws JMSException {
        messageConsumer.setMessageListener(messageListener);
    }

    /**
     * returns the receive destination
     * @return
     */
    public Destination getReceiveDestination() {
        return receiveDestination;
    }

    /**
     * sets the receive destination
     * @param receiveDestination
     */
    public void setReceiveDestination(Destination receiveDestination) {
        this.receiveDestination = receiveDestination;
    }
}
