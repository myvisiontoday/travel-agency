package applicationGateways;

import ScatterGather.AgencyRecipientList;
import ScatterGather.AgencySender;
import booking.agency.model.AgencyReply;
import booking.agency.model.AgencyRequest;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AgencyBrokerGateway.
 * Application Gateway (de)serializes domain objects and sends/receives messages
 * via the Messaging Gateway.
 */
public abstract class AgencyBrokerGateway {
    private static final String AGENCY_REPLY_QUEUE = "agencyReplyQueue";

    private MessagingReceiveGateway messagingReceiveGateway;
    private static int counter = 0;

    private Map<Integer, AgencyRecipientList> agencyReplyAggregators = new HashMap<>();
    private List<AgencySender> agencySenders = new ArrayList<>();
    final Logger logger = LoggerFactory.getLogger(getClass());

    public AgencyBrokerGateway() {
        // Load rules from text file and add Travel agency queues to the @agencySenders
        this.loadFromTextFile();

        try {
            messagingReceiveGateway = new MessagingReceiveGateway(AGENCY_REPLY_QUEUE);
            messagingReceiveGateway.SetListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        Gson gson = new Gson();
                        AgencyReply agencyReply = gson.fromJson(((TextMessage) message).getText(), AgencyReply.class);
                        String replyId = message.getJMSCorrelationID();
                        int aggregationID = message.getIntProperty("aggregationID");
                        // get reply aggregator
                        AgencyRecipientList agencyRecipientList = agencyReplyAggregators.get(aggregationID);
                        if(agencyRecipientList!=null)
                        {
                            //add the agency reply to the aggregator
                            agencyRecipientList.addReply(agencyReply, replyId);
                            logger.info("agency reply arrived " + agencyReply + replyId);

                            if(agencyRecipientList.finish()) {
                                AgencyReply bestReply = agencyRecipientList.findBestReply(); // find best reply
                                String bestReplyId = agencyRecipientList.getBestReplyId();
                                if(bestReply!=null) {
                                    agencyReplyArrived(bestReply, bestReplyId);
                                }
                                else
                                    logger.info("bestReply is null");
                            }
                        }
                        else
                            logger.info("cannot find the agencyRelyAggregator with id " + aggregationID);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public abstract void agencyReplyArrived(AgencyReply agencyReply, String bestReplyId);

    public void sendRequestToAgencies(ClientBookingRequest clientBookingRequest) {
        try {
            AgencyRequest agencyRequest = new AgencyRequest();

            if(clientBookingRequest.getClientID()!=0) {
                agencyRequest.setRegisteredClient(true); // if client id is not zero then set it to true
            }
            agencyRequest.setId(clientBookingRequest.getId());
            agencyRequest.setFromAirport(clientBookingRequest.getOriginAirport());
            agencyRequest.setToAirport(clientBookingRequest.getDestinationAirport());
            agencyRequest.setNrTravellers(clientBookingRequest.getNumberOfTravellers());

            Gson gson = new Gson();
            String requestMessage = gson.toJson(agencyRequest);

            // send message
            int nrOfRequest = 0;
            for(AgencySender agency: agencySenders)
            {
                if (agency.evaluateRequest(agencyRequest))
                {
                    MessagingSendGateway sender = agency.getSender();
                    Message msg = sender.createMessage(requestMessage);
                    msg.setJMSCorrelationID(clientBookingRequest.getId());
                    msg.setIntProperty("aggregationID",counter);
                    sender.SendMessage(msg);
                    nrOfRequest++;
                }
            }
            AgencyRecipientList bankRecipientList = new AgencyRecipientList(nrOfRequest);
            agencyReplyAggregators.put(counter, bankRecipientList);
            counter++;
            logger.info("Request is forwarded to the agency: " + clientBookingRequest + " id:" + clientBookingRequest.getId());

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void loadFromTextFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = getClass().getClassLoader().getResourceAsStream("AgencyQueueAndRules.txt");
                    InputStreamReader inputStreamReader = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(inputStreamReader);

                    String line;
                    while((line = br.readLine()) != null){
                        //process the line
                        String[] results = line.split("\\|");
                        agencySenders.add(new AgencySender(results[0], results[1]));
                        System.out.println(line);
                    }
                    br.close();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
