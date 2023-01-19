/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TripBookingInformation;

import BookingServices.Weather;




/**
 *
 * @author Bigse
 */
public class TripBookingInfo {
    private int userID;
    private int tripID;
    private User[] name;
    private String location;
    private String endDate;
    private String startDate;
    private Weather[] weather; 

    public TripBookingInfo() {
    }

    public TripBookingInfo(Weather[] weather) {
        this.weather = weather;
    }
    
  
    

    public TripBookingInfo(int userID, int tripID, User[] name, String location, String startYYMMDD, String endYYMMDD ) {
        this.userID = userID;
        this.tripID = tripID;
        this.location = location;
        this.name = name;
        this.endDate = endYYMMDD;
        this.startDate = startYYMMDD;
        
        
    }

    public TripBookingInfo(int userID, int tripID, String location) {
        this.userID = userID;
        this.tripID = tripID;
        this.location = location;
    }

    public TripBookingInfo(int tripID,int userID, String location, String startYYMMDD, String endYYMMDD) {
        this.userID = userID;
        this.tripID = tripID;
        this.location = location;
        this.endDate = endYYMMDD;
        this.startDate = startYYMMDD;
    }
    

    public User[] getName() {
        return name;
    }

    public void setName(User[] name) {
        this.name = name;
    }

    public int getUserID() {
        return this.userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getTripID() {
        return this.tripID;
    }

    public void setTripID(int tripID) {
        this.tripID = tripID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Weather[] getWeather() {
        return this.weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
   
    
}


