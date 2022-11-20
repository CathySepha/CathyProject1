package com.example.cathyproject;


public class Dropoffs {


    private String TripId;
    private String Dropoffstage;


    public Dropoffs (String TripId, String Dropoffstage) {
        this.TripId = TripId;
        this.Dropoffstage = Dropoffstage;


    }

    public String getTripId() {
        return TripId;
    }

    public void setTripId(String TripId) {
        this.TripId = TripId;
    }

    public String getDropoffstage() {
        return Dropoffstage;
    }

    public void setDropoffstage(String Dropoffstage) {
        this.Dropoffstage= Dropoffstage;
    }




    @Override
    public String toString() {
        return "DataPOJO {" +
                "TripId=" + TripId +
                ", Dropoffstage='" + Dropoffstage + '\'' +
                '}';
    }
}
