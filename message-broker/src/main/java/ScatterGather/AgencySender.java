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
    public boolean evaluateRequest(AgencyRequest agencyRequest) {
        boolean check = false;
        evaluator.clearVariables();
        evaluator.putVariable("numberOfTravellers", Integer.toString(agencyRequest.getNrTravellers()));
        evaluator.putVariable("isRegisteredClient", String.valueOf(agencyRequest.isRegisteredClient()));
        //check = evaluator.evaluate(rule).equals("1.0");
        check = true;
        return true;
    }

    public MessagingSendGateway getSender(){
        return messagingSendGateway;
    }

}
