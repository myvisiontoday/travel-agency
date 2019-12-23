package booking.client.model;

public enum Discount {
    STANDARD{
        public int getDiscount(){ return 0;}
    },
    SILVER{
        public int getDiscount(){ return 10;}
    },
    GOLD{
        public int getDiscount(){ return 20;}
    },
    PREMIUM{
        public int getDiscount(){ return 30;}
    }
}
