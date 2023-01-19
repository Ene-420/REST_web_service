/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TripBookingInformation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bigse
 */
public class TripBookingInfoList {
    protected List<TripBookingInfo> TripBookingInfo;  
    
    
    public List<TripBookingInfo> getTripBookingInfo(){
        if(TripBookingInfo == null){
            TripBookingInfo = new ArrayList<TripBookingInfo>();
        } 
        return TripBookingInfo;
    }
}
