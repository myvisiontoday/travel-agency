package applicationGateways;

import booking.administration.model.ClientProfile;
import booking.administration.model.ClientType;
import booking.agency.model.AgencyReply;
import booking.client.model.*;
import com.google.gson.Gson;
import enrichers.AdministrationGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routers.DiscountRouter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.HashMap;
import java.util.Map;

/**
 * ClientBrokerGateway.
 * Application Gateway (de)serializes domain objects and sends/receives messages
 * via the Messaging Gateway.
 */
public abstract class ClientBrokerGateway {
    private static final String REPLY_QUEUE = "bookingReplyQueue" ;
    private static final String REQUEST_QUEUE = "bookingRequestQueue" ;

    private Map<String, ClientBookingRequest> clientBookingRequestMap = new HashMap<>();
    private AdministrationGateway administrationGateway;
    private DiscountRouter discountRouter;

    private MessagingSendGateway messagingSendGateway;
    private MessagingReceiveGateway messagingReceiveGateway;

    final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * It has message listener for request message coming from the client.
     * Request message is then forwarded to the Agency via AgencyBrokerGateway.
     */
    public ClientBrokerGateway() {
        try {
            discountRouter = new DiscountRouter();
            administrationGateway = new AdministrationGateway();
            messagingSendGateway = new MessagingSendGateway(REPLY_QUEUE);
            messagingReceiveGateway = new MessagingReceiveGateway(REQUEST_QUEUE);
            messagingReceiveGateway.SetListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        Gson gson = new Gson();
                        ClientBookingRequest clientBookingRequest = gson.fromJson(((TextMessage) message).getText(), ClientBookingRequest.class);
                        if(clientBookingRequest!=null)
                        {
                            clientBookingRequestMap.put(clientBookingRequest.getId(), clientBookingRequest);
                            logger.info("request arrived " + clientBookingRequest.getOriginAirport());
                            clientRequestArrived(clientBookingRequest);
                        }
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public abstract void clientRequestArrived(ClientBookingRequest clientBookingRequest);

    /**
     * This method first request Administration webservice for client profile then apply discount using DiscountRouter.
     * Creates clientBookingReply.
     * Finally serialize the clientBookingReply and sends it back via bookingReplyQueue.
     * @param agencyReply
     * @param replyId
     */
    public void sendReplyToClient(AgencyReply agencyReply, String replyId) {
        int clientID = 0;
        ClientProfile clientProfile = null;
        double total_price = 0.0;

        if(agencyReply==null){
            logger.info("agency reply is null.");
            return;
        }
        ClientBookingReply clientBookingReply = new ClientBookingReply();
        clientBookingReply.setAgencyName(agencyReply.getName());
        clientBookingReply.setId(agencyReply.getId());

        if(clientBookingRequestMap.get(replyId) ==null) {
            logger.info("cannot find the booking request with id" + agencyReply.getId());
        }

        ClientBookingRequest bookingRequest = clientBookingRequestMap.get(replyId);
        clientID = bookingRequest.getClientID();

        if(clientID!=0)
            clientProfile = administrationGateway.getClientProfile(clientID);

        //if clientProfile is null at this line. that means either the client is not registered or clientId is less than <100,
        // then default clientProfile is assigned.
        if(clientProfile == null)
            clientProfile = new ClientProfile(ClientType.STANDARD, 0);

        total_price = discountRouter.calculateTotalPrice(clientProfile, agencyReply.getPrice(), bookingRequest.getNumberOfTravellers());
        clientBookingReply.setTotalPrice(total_price);

        //send clientBookingReply back to client
        try {
            Gson gson = new Gson();
            String reply = gson.toJson(clientBookingReply); // clientReply
            Message message = messagingSendGateway.createMessage(reply);
            message.setJMSCorrelationID(replyId);
            messagingSendGateway.SendMessage(message);
            logger.info(" reply sent to the client: bestPrice" + clientBookingReply.getTotalPrice());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
