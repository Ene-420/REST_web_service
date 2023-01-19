/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BookingServices;

import Booking.webservice.TripBookingResource;
import TripBookingInformation.TripBookingInfo;
import BookingServices.Weather;
import BookingServices.Database;
import TripBookingInformation.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
///import javax.json.Json;

/**
 *
 * @author Bigse
 */
public class BookingServices {
    
    
    Database database = new Database();
    
   public List<TripBookingInfo> getAllTrips(){
       return database.getAllUsers();
   }
 
   // Gets random number in json format from given URL
    public String getIdOnline() throws IOException, IOException{

        String mainURL = "https://csrng.net/csrng/csrng.php?min=0&max=1000";
        String response = "";
         try {
            URL url = new URL(mainURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            if(con.getResponseCode() != 200){
                throw new IOException(con.getResponseMessage());
            }
            
        BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
      
        String line = in.readLine();
        
        while(line != null){
            
            response += line + "\r\n";
            line = in.readLine();
        }

        con.disconnect();
        } catch (IOException ex) {
            Logger.getLogger(BookingServices.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return response;
    }
    
    //Gets weather details for specified values in json format
    public String getWeatherforLocation(TripBookingInfo trip) throws IOException{
        String mainURL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"+trip.getLocation()+"/"+trip.getStartDate()+"/"+trip.getEndDate() +"?unitGroup=uk&include=current&key=";
        String apiKey = "BNAX2GBUEDF9XCU6E74SU4T36";
        String contentType = "&contentType=json";
        String Response = "";
        
        
            try{
            URL url = new URL(mainURL + apiKey + contentType);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            
            if(con.getResponseCode() != 200){
                throw new IOException(con.getResponseMessage());
            }
             
            BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
            
            String line = in.readLine();
            while(line != null){
                
                Response += line + "\r\n";
                line = in.readLine();
            }
            con.disconnect();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }        
        return Response;            
    } 
    
    //Parses weather values from json format to desired variables and appends to list 
    public List<Weather> getWeatherContent(TripBookingInfo tripInfo) throws IOException{
        
        List<Weather> weatherConditions = new ArrayList<>();
        String contentBody = getWeatherforLocation(tripInfo);
  
        if(contentBody != null){ 
            LocalDate start = LocalDate.parse(tripInfo.getStartDate()); 
            LocalDate end = LocalDate.parse(tripInfo.getEndDate());

            int daysBetween = (int) ChronoUnit.DAYS.between(start, end);
            for(int i = 0; i <= daysBetween; i++){


                String weatherDate = JsonToStr(contentBody,"datetime", "days",i);
                double weatherTempMax = Double.parseDouble(JsonToStr(contentBody,"tempmax", "days",i));
                double weatherTempMin = Double.parseDouble(JsonToStr(contentBody,"tempmin", "days",i));
                double weatherTemp = Double.parseDouble(JsonToStr(contentBody,"temp", "days",i));
                String weatherCondition = JsonToStr(contentBody,"conditions", "days",i);
                String weatherDescription = JsonToStr(contentBody,"description", "days",i);

                Weather weatherDetails = new Weather(weatherTempMax,weatherTempMin,weatherTemp, weatherDescription, weatherCondition,weatherDate);
                weatherConditions.add(weatherDetails);

                
        }
    }

        return weatherConditions;
        
        
    }
    
    //Converts json values to String
    public String JsonToStr(String content, String obj, String thirdValue, int count){
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        if(thirdValue == null){
        JsonArray array = gson.fromJson(content, JsonArray.class);
        JsonObject object = array.get(count).getAsJsonObject();
        String value = object.get(obj).getAsString();
        
        return value;
        }else {
            JsonObject jsonObject = gson.fromJson(content, JsonObject.class);
            JsonArray jsonObjectArray = jsonObject.get(thirdValue).getAsJsonArray();
            JsonObject jsonValue = jsonObjectArray.get(count).getAsJsonObject();
            String contentBody = jsonValue.get(obj).getAsString();
            
            return contentBody;
            
        }
    }
    
    //Creates new User if valid values are presented
    public int createNewUser(User user) throws IOException{
        
        String content = getIdOnline();
        int userCount = 0;

        int UserID = Integer.valueOf(JsonToStr(content,"random", null,0));

        userCount = database.userCount(user);
        if(userCount >= 1){
 
            return 0;
        }
        else{
        User newUser = new User(user.getFirstName(),user.getLastName(), UserID);
        database.createUser(newUser); 
        return newUser.getUserID();
        }

    }
    
    //Creates new trip if valid values are presented 
    public String createNewTrip(TripBookingInfo trip) throws IOException{
        int tripCount =0;
        String value = getIdOnline();
        int TripID = Integer.valueOf(JsonToStr(value,"random", null, 0));

        tripCount = database.tripCount(trip);
        if(tripCount >= 1){
            
            return "Error";
        }

        else{
            TripBookingInfo newTrip = new TripBookingInfo(TripID,trip.getUserID(),trip.getLocation(), trip.getStartDate(), trip.getEndDate());
            database.createTrip(newTrip);        
            return "Hello User "+ newTrip.getUserID() +" the tripID for your trip to "+ newTrip.getLocation()+ " is "+newTrip.getTripID();
            
        }
        
    }
    
    //Checks user existence in database and if valid logs user in
    public int userLogin(User user){

        int userCount = database.userCount(user);
        if(userCount ==1){
            return userCount;
        }
        return 0;
        
    }
    
    //Gets a list of all user trips through specified user id 
    public List<TripBookingInfo> getUserTrips(int id){
        if(database.getUser(id).isEmpty()){
            return null;
        }
        return database.getUser(id);
    }
    
    //Updates specifc user trip
    public String updateUserTrip(TripBookingInfo trip){
        database.updateTrip(trip);
        return "TripID: "+trip.getTripID() +" has been updated";
    }
    
    //Deletes specific user trip
    public String deleteUserTrip(int trip){
        database.deleteUserTrip(trip);
        return "Trip has been deleted";
    }
    


}
