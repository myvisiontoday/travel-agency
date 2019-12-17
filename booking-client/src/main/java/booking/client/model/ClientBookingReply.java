package booking.client.model;

import java.util.Objects;

/**
 *
 * This class stores all information about a client booking reply.
 */
public class ClientBookingReply {

    private String id;
    private String agencyName;
    private double totalPrice;

    public ClientBookingReply(String id, String agencyName, double totalPrice) {
        this.id = id;
        this.agencyName = agencyName;
        this.totalPrice = totalPrice;
    }

    public ClientBookingReply() {
        this.id = "unknown";
        this.agencyName = "unknown";
        this.totalPrice = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id != null){
            this.id = id;
        }
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String reasonRejected) {
        this.agencyName = reasonRejected;
    }
    
        public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double costs) {
        this.totalPrice = costs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientBookingReply that = (ClientBookingReply) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ClientBookingReply{" +
                "id='" + id + '\'' +
                ", agencyName='" + agencyName + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
