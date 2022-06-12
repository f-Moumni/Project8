package Common.model;

import java.util.UUID;

public class Provider {
    private  String name;
    private      double price;
    private    UUID   tripId;

    public Provider(String name, double price, UUID tripId) {

        this.name   = name;
        this.price  = price;
        this.tripId = tripId;
    }

    public Provider() {

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

    public UUID getTripId() {

        return tripId;
    }

    public void setTripId(UUID tripId) {

        this.tripId = tripId;
    }
}
