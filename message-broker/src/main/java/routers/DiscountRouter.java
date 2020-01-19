package routers;

import booking.administration.model.ClientProfile;

public class DiscountRouter {

    private double total_price;

    public DiscountRouter() {
    }

    /**
     * returns total price after applying discount depending up on client profile.
     * @param clientProfile
     * @param agencyReplyPrice
     * @param price
     * @return
     */
    public double calculateTotalPrice(ClientProfile clientProfile, double agencyReplyPrice, double price) {

        this.total_price = agencyReplyPrice * price * (1 - (clientProfile.getDiscount() / 100));

        return total_price;
    }
}
