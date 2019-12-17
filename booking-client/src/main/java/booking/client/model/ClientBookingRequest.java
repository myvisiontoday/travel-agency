package booking.client.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * This class stores all information about a client booking request.
 */
public class ClientBookingRequest {

    private String id;
    private String originAirport;
    private String destinationAirport;
    private int numberOfTravellers;
    private LocalDate date;
    private int clientID;

    public ClientBookingRequest(String id, String originAirport, String destinationAirport, int numberOfTravellers, int clientID) {
        this.id = id;
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
        this.numberOfTravellers = numberOfTravellers;
        this.date = LocalDate.now();
        this.clientID = clientID;
    }

    public ClientBookingRequest(String id, String originAirport, String destinationAirport, LocalDate date, int numberOfTravellers) {
        this.id = id;
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
        this.numberOfTravellers = numberOfTravellers;
        this.date = date;
        this.clientID = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id != null) {
            this.id = id;
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getOriginAirport() {
        return originAirport;
    }

    public void setOriginAirport(String originAirport) {
        this.originAirport = originAirport;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public int getNumberOfTravellers() {
        return numberOfTravellers;
    }

    public void setNumberOfTravellers(int numberOfTravellers) {
        this.numberOfTravellers = numberOfTravellers;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientBookingRequest that = (ClientBookingRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ClientBookingRequest{" +
                "id='" + id + '\'' +
                ", originAirport='" + originAirport + '\'' +
                ", destinationAirport='" + destinationAirport + '\'' +
                ", numberOfTravellers=" + numberOfTravellers +
                ", date=" + date +
                ", clientID=" + clientID +
                '}';
    }
}
