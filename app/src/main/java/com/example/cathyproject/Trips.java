package com.example.cathyproject;

public class Trips {


    private String DestinationId;
    private String Tripname;
    private String Amount;

    public Trips(String DestinationId, String TripName, String Amount) {
        this.DestinationId = DestinationId;
        this.Tripname = TripName;
        this.Amount = Amount;

    }

    public String getDestinationId() {
        return DestinationId;
    }

    public void setDestinationId(String DestinationId) {
        this.DestinationId = DestinationId;
    }

    public String getTripname() {
        return Tripname;
    }

    public void setTripname(String Tripname) {
        this.Tripname = Tripname;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String Amount) {
        this.Amount = Amount;
    }


    @Override
    public String toString() {
        return "DataPOJO {" +
                "DesinationId=" + DestinationId +
                ", Tripname='" + Tripname + '\'' +
                ", Amount='" + Amount + '\'' +
                '}';
    }
}