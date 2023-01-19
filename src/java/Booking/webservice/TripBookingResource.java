/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Booking.webservice;

import BookingServices.BookingServices;
import BookingServices.Database;
import BookingServices.Weather;
import TripBookingInformation.TripBookingInfo;
import TripBookingInformation.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
/**
 * REST Web Service
 *
 * @author Bigse
 */
@Path("TripBooking")
public class TripBookingResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TripBookingResource
     */
    public TripBookingResource() {
    }

    /**
     * Retrieves representation of an instance of Booking.webservice.TripBookingResource
     * @return an instance of java.lang.String
     */
    
    BookingServices bookService = new BookingServices();
    Database database = new Database();

    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TripBookingInfo> getAllUserTrips(){
        return bookService.getAllTrips();
    }
    
    //Verifies User existence 
    @POST
    @Path("/Login/{userID}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginUser(@PathParam("userID") int id, String obj)  throws IOException{
        
        User user = new Gson().fromJson(obj, User.class);
        
        user.setUserID(id);
        String uId = Integer.toString(user.getUserID());
        int existingUser = bookService.userLogin(user);
        if(existingUser  == 1){
            return Response.status(201).entity(uId).build();
            
        }
        
        return Response.status(400).entity("User Doesn't Exist").build();
    }
    
    //Creates User based on details provided
    @POST
    @Path("/Register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(String obj) throws IOException {
        
        User user = new Gson().fromJson(obj, User.class);
        int userCreated = bookService.createNewUser(user);
        String message = "UserID "+ userCreated;
        if(userCreated > 0){
            return Response.status(201).entity(message).build();
        }
        message = "Error";
        return Response.status(400).entity(message).build();
    }
    
    //Creates specified trips for User through details provided
    @POST
    @Path("/{userID}")
    @Consumes(MediaType.APPLICATION_JSON) 
    public Response createNewTrip(@PathParam("userID") int id, String obj) throws IOException{
        TripBookingInfo newTrip = new Gson().fromJson(obj, TripBookingInfo.class);
        String replyClient = bookService.createNewTrip(newTrip);
        
        if(replyClient.contains("Error")){
            return Response.status(400).entity("Trip Already Exists").build();
        }
        return Response.status(201).entity(replyClient).build();
    }

    
    // Gets trips for specified User
    @GET
    @Path("/{userID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrips(@PathParam("userID") int id){
        List<TripBookingInfo> trips = bookService.getUserTrips(id);
        if(trips.isEmpty()){
            return Response.status(400).entity("Error").build();
           
        }
        GenericEntity<List<TripBookingInfo>> list = new GenericEntity<List<TripBookingInfo>>(trips){};
        return Response.status(201).entity(list).build();

    }
    
    //Gets weather information through specified details provided
    @GET 
    @Path("/location")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWeatherInformation(@QueryParam("location") String location, @QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate ) throws IOException{
        TripBookingInfo tripInfo = new TripBookingInfo();
        tripInfo.setLocation(location);
        tripInfo.setStartDate(startDate);
        tripInfo.setEndDate(endDate);
        
        List<Weather>weather =  bookService.getWeatherContent(tripInfo);
        if(weather.isEmpty()){
            return Response.status(400).entity("No Weather Details Available").build();
        }
        
        GenericEntity<List<Weather>> list = new GenericEntity<List<Weather>>(weather){};
        return Response.status(201).entity(list).build();
    }

    /**
     * PUT method for updating or creating an instance of TripBookingResource
     * @param content representation for the resource
     * @param userID
     * @param tripID
     * @return 
     */
    
    //Updates User trip with specified values provided
    @PUT
    @Path("/Update/{userID}/{tripID}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTrip(String content, @PathParam("userID")int userID, @PathParam("tripID")int tripID) {
        
        TripBookingInfo updatedTrip = new Gson().fromJson(content, TripBookingInfo.class);
        
        int tripCount = database.tripExist(tripID);
        if(tripCount == 1){
            String message =bookService.updateUserTrip(updatedTrip);
            return Response.status(201).entity(message).build();
        }
        return Response.status(400).entity("Trip Doesn't Exist").build();
        
    }
    
    //Deletes User trip
    @DELETE
    @Path("/Delete/{userID}/{tripID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTrip(@PathParam("userID")int userID, @PathParam("tripID")int tripID){
        
        int tripCount = database.tripExist(tripID);
        if(tripCount == 1){
            String message =bookService.deleteUserTrip(tripID);
            return Response.status(201).entity(message).build();
        }
        return Response.status(400).entity("Trip Doesn't Exist").build();

    }
}
