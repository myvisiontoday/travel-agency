import applicationGateways.AgencyBrokerGateway;
import applicationGateways.ClientBrokerGateway;
import booking.agency.model.AgencyReply;
import booking.client.model.ClientBookingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrokerMain {
    /**
     * Two Application gateway. AgencyBrokerGateway assists communication between client and broker,
     * ClientBrokerGateway for broker and the travel agency.
     */
    private static AgencyBrokerGateway agencyBrokerGateway;
    private static ClientBrokerGateway clientBrokerGateway;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {
        System.out.println("*************** Message Broker ****************");
        clientBrokerGateway = new ClientBrokerGateway() {
            @Override
            public void clientRequestArrived(ClientBookingRequest clientBookingRequest) {
                // to do forward request to the agency
                agencyBrokerGateway.sendRequestToAgencies(clientBookingRequest);
            }
        };
        agencyBrokerGateway = new AgencyBrokerGateway() {
            @Override
            public void agencyReplyArrived(AgencyReply agencyReply, String bestReplyId) {
                // to do forward request to the client
                clientBrokerGateway.sendReplyToClient(agencyReply, bestReplyId);
            }
        };
    }
}
