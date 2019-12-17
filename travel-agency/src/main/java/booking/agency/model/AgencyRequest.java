package booking.agency.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * This class stores information about the booking.agency request for a booking.
 */
public class AgencyRequest {

    private String id;
    private String fromAirport; 
    private String toAirport;
    private LocalDate date;
    private int nrTravellers;
    private boolean isRegisteredClient;

    public AgencyRequest() {
        this.id = "unknown";
        this.fromAirport = "unknown";
        this.toAirport = "unknown";
        this.date = LocalDate.now();
        this.nrTravellers = 0;
        this.isRegisteredClient = false;
    }

    public AgencyRequest(String id, String fromAirport, String toAirport, LocalDate date, int nrTravellers, boolean  registeredClient) {
        this.id = id;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.date = date;
        this.nrTravellers = nrTravellers;
        this.isRegisteredClient =  registeredClient;
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

    public String getFromAirport() {
        return fromAirport;
    }

    public void setFromAirport(String fromAirport) {
        this.fromAirport = fromAirport;
    }

    public String getToAirport() {
        return toAirport;
    }

    public void setToAirport(String toAirport) {
        this.toAirport = toAirport;
    }

    public boolean isRegisteredClient() {
        return isRegisteredClient;
    }

    public void setRegisteredClient(boolean registeredClient) {
        isRegisteredClient = registeredClient;
    }

    public int getNrTravellers() {
        return nrTravellers;
    }

    public void setNrTravellers(int nrTravellers) {
        this.nrTravellers = nrTravellers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgencyRequest that = (AgencyRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AgencyRequest{" +
                "id='" + id + '\'' +
                ", fromAirport='" + fromAirport + '\'' +
                ", toAirport='" + toAirport + '\'' +
                ", date=" + date +
                ", nrTravellers=" + nrTravellers +
                ", isRegisteredClient=" + isRegisteredClient +
                '}';
    }
}
