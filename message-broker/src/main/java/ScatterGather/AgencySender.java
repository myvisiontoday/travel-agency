package ScatterGather;

import booking.agency.model.AgencyRequest;
import booking.client.model.MessagingSendGateway;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import javax.jms.JMSException;

public class AgencySender {
    private MessagingSendGateway messagingSendGateway;
    private Evaluator evaluator;
    private String rule;
    private String queueName;

    public AgencySender(String queueName, String rule) throws JMSException {
        this.rule = rule;
        this.queueName = queueName;
        this.evaluator = new Evaluator();
        this.messagingSendGateway = new MessagingSendGateway(queueName);
    }

    /**
     * returns true if the AgencyRequest satisfies the rule which is passed through constructor.
     * otherwise returns false.
     * @param agencyRequest
     * @return
     */
    public boolean evaluateRequest(AgencyRequest agencyRequest) {
        boolean check = false;
        evaluator.clearVariables();
        evaluator.putVariable("numberOfTravellers", Integer.toString(agencyRequest.getNrTravellers()));
        evaluator.putVariable("isRegisteredClient", Integer.toString(agencyRequest.isRegisteredClient() ? 1 : 0));
        try {
            check = evaluator.evaluate(rule).equals("1.0");
        } catch (EvaluationException e) {
            e.printStackTrace();
        }
        return check;
    }

    /**
     * returns the MessagingGateway for the agency queue supplied above in the constructor.
     * @return
     */
    public MessagingSendGateway getSender(){
        return messagingSendGateway;
    }

}
