package booking.client.gui;

import booking.client.model.*;

import java.time.format.DateTimeFormatter;

class ClientListViewLine {

    private ClientBookingRequest request;
    private ClientBookingReply reply;

    public ClientListViewLine(ClientBookingRequest request, ClientBookingReply reply) {
        this.request = request;
        this.reply = reply;
    }

    public ClientListViewLine(ClientBookingRequest request) {
        this.request = request;
        this.reply = null;
    }

    public ClientBookingRequest getRequest() {
        return request;
    }

    private void setRequest(ClientBookingRequest request) {
        this.request = request;
    }

    public ClientBookingReply getReply() {
        return reply;
    }

    public void setReply(ClientBookingReply reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        final String delimiter = "-";
        final String requestString = request.getOriginAirport() +delimiter
                              + request.getDestinationAirport() + delimiter
                              + request.getNumberOfTravellers() + delimiter
                              + request.getDate().format(formatters) + delimiter
                              + request.getClientID();
        final String replyString;
        if (reply == null){
            replyString = "waiting...";
        } else {
            replyString =  reply.getTotalPrice() + delimiter + reply.getAgencyName();
        }
        return requestString + "  --->  " + replyString;
    }

}
