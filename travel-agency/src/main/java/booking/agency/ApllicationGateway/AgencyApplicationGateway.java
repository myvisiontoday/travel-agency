package booking.agency.ApllicationGateway;

import booking.agency.model.AgencyReply;
import booking.agency.model.AgencyRequest;
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

public abstract class AgencyApplicationGateway {
    private static Map<String, Integer> requestIdAndAggregationId = new HashMap<>();
    final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String AGENCY_REPLY_QUEUE = "agencyReplyQueue" ;

    private MessagingSendGateway messagingSendGateway;
    private MessagingReceiveGateway messagingReceiveGateway;
    public AgencyApplicationGateway(String queueName) {
        try {
            messagingSendGateway = new MessagingSendGateway(AGENCY_REPLY_QUEUE);
            messagingReceiveGateway = new MessagingReceiveGateway(queueName);
            messagingReceiveGateway.SetListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    Gson gson = new Gson();
                    try {
                        AgencyRequest agencyRequest = gson.fromJson(((TextMessage) message).getText(), AgencyRequest.class);
                        String replyId = agencyRequest.getId();
                        int aggregationID = message.getIntProperty("aggregationID");
                        brokerRequestArrived(agencyRequest);

                        //put requestId and relevant aggregation id
                        requestIdAndAggregationId.put(replyId, aggregationID);
                        logger.info("Request received from broker "+ agencyRequest+replyId + "aggID" + aggregationID);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public abstract void brokerRequestArrived(AgencyRequest agencyRequest);

    public void sendReplyToBroker(String replyId, AgencyReply reply) {
        try {
            Gson gson = new Gson();
            String replyMessage = gson.toJson(reply);
            Message message = messagingSendGateway.createMessage(replyMessage);
            message.setJMSCorrelationID(replyId);
            message.setIntProperty("aggregationID",requestIdAndAggregationId.get(replyId));
            messagingSendGateway.SendMessage(message);
            logger.info("Reply to the broker"+ reply + replyId);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
