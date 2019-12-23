package booking.client.ApplicationGateway;

import booking.client.model.ClientBookingReply;
import booking.client.model.ClientBookingRequest;
import booking.client.model.MessagingReceiveGateway;
import booking.client.model.MessagingSendGateway;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.HashMap;
import java.util.Map;

public abstract class ClientApplicationGateway {
    private static final String MESSAGE_REQUEST_QUEUE = "bookingRequestQueue" ;
    private static final String MESSAGE_REPLY_QUEUE = "bookingReplyQueue" ;

    private MessagingSendGateway messagingSendGateway ;
    private MessagingReceiveGateway messagingReceiveGateway;

    final Logger logger = LoggerFactory.getLogger(getClass());
    private Map<String, ClientBookingRequest> requestMap = new HashMap<>();

    public ClientApplicationGateway() throws JMSException {
        messagingSendGateway = new MessagingSendGateway(MESSAGE_REQUEST_QUEUE);
        messagingReceiveGateway = new MessagingReceiveGateway(MESSAGE_REPLY_QUEUE);

        messagingReceiveGateway.SetListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                Gson gson = new Gson();
                ClientBookingReply bookingReply;
                ClientBookingRequest bookingRequest;
                try {
                    bookingReply = gson.fromJson(((TextMessage) message).getText(), ClientBookingReply.class);
                    logger.info("Received the booking reply: " + bookingReply + " with messageID: " + message.getJMSMessageID());

                    bookingRequest = requestMap.get(message.getJMSCorrelationID());
                    if (bookingRequest != null){
                        replyArrived(bookingRequest,bookingReply);
                    }
                    else {
                        logger.info("reply ID did not match");
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This method is called when booking reply arrived.
     * @param bookingReply
     */
    public abstract void replyArrived(ClientBookingRequest bookingRequest, ClientBookingReply bookingReply);

    /**
     * This method serialize the booking request object and send it to the message-broker. C
     * @param bookingRequest
     */
    public void requestBooking(ClientBookingRequest bookingRequest){
        try {
            Gson gson = new Gson();
            String requestMessage = gson.toJson(bookingRequest);
            Message message = messagingSendGateway.createMessage(requestMessage);
            message.setJMSReplyTo(messagingReceiveGateway.getReceiveDestination());
            messagingSendGateway.SendMessage(message);
            requestMap.put(message.getJMSMessageID(), bookingRequest);

            logger.info("sent: " + bookingRequest + " with messageID: " + message.getJMSMessageID());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}