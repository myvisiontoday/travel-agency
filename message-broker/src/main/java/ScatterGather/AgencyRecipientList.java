package ScatterGather;

import booking.agency.model.AgencyReply;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgencyRecipientList {
    private int nrOfRequest;
    private String bestReplyId;
    private List<AgencyReply> replyList;
    // map with agency replyId and the reply id of the agency request
    private Map<String, String > agencyReplyMap = new HashMap<>();

    public AgencyRecipientList(int nrOfRequest) {
        this.nrOfRequest = nrOfRequest;
        replyList = new ArrayList<>();
    }
    public void addReply(AgencyReply reply, String replyId){
        replyList.add(reply);
        agencyReplyMap.put(reply.getId(), replyId);
    }

    public boolean finish(){
        if (replyList.size() == nrOfRequest)
            return true;
        return false;
    }

    public AgencyReply findBestReply()
    {
        AgencyReply bestAgencyReply = replyList.iterator().next();
        double min_price = bestAgencyReply.getPrice();

        for(AgencyReply agencyReply: replyList){
            if(agencyReply.getPrice() <= min_price){
                min_price = agencyReply.getPrice();
                bestAgencyReply = agencyReply;
                this.bestReplyId = agencyReplyMap.get(agencyReply.getId());
            }
        }
        return bestAgencyReply;
    }

    public String getBestReplyId() {
        return bestReplyId;
    }
}
