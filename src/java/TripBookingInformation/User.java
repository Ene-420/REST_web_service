/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TripBookingInformation;

import com.google.gson.annotations.Expose;
//import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Bigse
 */
//@XmlRootElement (name= "user")
public class User {
    
    private String firstName;
    private String lastName;
    private int UserID;

    public User(){
        
    }
    public User(String firstName, String lastName, int UserID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.UserID = UserID;
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public User(String firstName, int UserID){
        this.UserID= UserID;
        this.firstName = firstName;
    }

    
    
    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }
    

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    
}
