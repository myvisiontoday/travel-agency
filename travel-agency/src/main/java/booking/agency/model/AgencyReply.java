package booking.agency.model;

import java.util.Objects;

/**
 * This class stores information about the booking.agency reply for a booking.
 */
public class AgencyReply {

    private String id;
    private String name;
    private double price;

    public AgencyReply(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public AgencyReply() {
        this.id = "unknown";
        this.name = "unknown";
        this.price = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id != null) {
            this.id = id;
        }
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgencyReply that = (AgencyReply) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AgencyReply{" +
                "id='" + id + '\'' +
                ", nameAgency='" + name + '\'' +
                ", totalPrice=" + price +
                '}';
    }
}
