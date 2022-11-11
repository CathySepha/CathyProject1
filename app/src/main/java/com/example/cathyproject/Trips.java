package com.example.cathyproject;

public class Trips {


    private String TripId;
    private String Tripname;
    private String Amount;

    public Trips(String TripId, String TripName, String Amount) {
        this.TripId = TripId;
        this.Tripname = TripName;
        this.Amount = Amount;

    }

    public String getTripId() {
        return TripId;
    }

    public void setDestinationId(String DestinationId) {
        this.TripId = DestinationId;
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
                "DesinationId=" + TripId +
                ", Tripname='" + Tripname + '\'' +
                ", Amount='" + Amount + '\'' +
                '}';
    }
}