/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BookingServices;

//import java.beans.Statement;
import TripBookingInformation.TripBookingInfo;
import TripBookingInformation.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bigse
 */
public class Database {
    private String tableName;
    private String tableFunction;
    
    Connection con =null;

    public Database() {
//        String url = "jdbc:mysql://mydemoserver14.mysql.database.azure.com:3306/tripbookinginformation";
//        String userName = "ene_1400";
//        String password = "Poptropica3005";
        
        String url = "jdbc:mysql://localhost:3306/tripbookinginformation";
        String userName = "root";
        String password = "poptropica3005";
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, userName, password);
        }
        catch(SQLException e ){
                System.out.println(e.getSQLState());
                System.out.println(e.getCause());
            } catch (ClassNotFoundException ex) {
        }

        
    }
    
    //Returns list of all user details on database
    public List<TripBookingInfo> getAllUsers(){ 
        
       List<TripBookingInfo> trips = new ArrayList<>();
        
       String sql = "SELECT u.userID, tripID, firstName, lastName, location, startDate, endDate" + 
                    " FROM Trips t"+
                    " JOIN Users u" + 
                    " ON u.userID = t.userID";
       try{
           Statement st = con.createStatement();
           ResultSet rs = st.executeQuery(sql);
           while(rs.next()){
               TripBookingInfo userTrips = new TripBookingInfo();
               userTrips.setTripID(rs.getInt("tripID"));
               userTrips.setName(new User[]{ new User(rs.getString("firstName"), rs.getString("lastName"), rs.getInt("u.userID"))});
               userTrips.setLocation(rs.getString("location"));
               userTrips.setStartDate(rs.getString("startDate"));
               userTrips.setEndDate(rs.getString("endDate"));
               trips.add(userTrips);
           }
           rs.close();
           st.close();
           con.close();
        }catch(SQLException e ){
                System.out.println(e.getSQLState());
            }
       return trips;
        
    }
    //Returns count of all user trips registered with specified trip ID
    public int userTripCount(int id){
        int count= 0;
        String sql ="SELECT COUNT(userID)"+
                    " FROM Trips"+
                    " WHERE tripID="+ id;
        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
            count = rs.getInt(1);
            }
            rs.close();
            st.close();
        }catch(SQLException e){
            System.out.println(e.getSQLState());
        }
        return count;
    }
    
    //Gets list of all user trips through specified user id
    public List<TripBookingInfo> getUser(int id){
        
       List<TripBookingInfo> trips = new ArrayList<>();
        
       String sql = "SELECT *" + 
                    " FROM Trips "+ 
                    " WHERE userID ="+ id;
       try{
           Statement st = con.createStatement();
           ResultSet rs = st.executeQuery(sql);
           while(rs.next()){
               TripBookingInfo userTrips = new TripBookingInfo();
               userTrips.setTripID(rs.getInt(1));
               userTrips.setUserID(rs.getInt(2));
               userTrips.setLocation(rs.getString(3));
               userTrips.setStartDate(rs.getString(4));
               userTrips.setEndDate(rs.getString(5));
               
               trips.add(userTrips);
           }
           rs.close();
           st.close();
        }catch(SQLException e ){
                System.out.println(e.getSQLState());
            }
       if(trips.isEmpty()){
           return null;
       }
       return trips;
        
        //return trips;
    }
    
    //Gets list of all users present in database
    public List<User> getAllUserNames(){
        
        List<User> userNameList = new ArrayList<>();
        String sql = "SELECT firstName, LastName, userID"
                    +" FROM Users";
        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                User userName = new User();
                userName.setFirstName(rs.getString("firstName"));
                userName.setLastName(rs.getString("LastName"));
                userName.setUserID(rs.getInt("userID"));
                
                userNameList.add(userName);
            }
            rs.close();
            st.close();
        }catch(SQLException sqlEx){
            System.out.println(sqlEx.getSQLState());
        }
        return userNameList;
    }
    
    //Checks of occurrence of user in database
    public int userCount(User user){
       int count = 0;

       if(user.getLastName() != null){
        String sql ="SELECT COUNT(*)"+
                    " FROM Users"+
                    " WHERE firstName=? AND lastName =?";
        try{
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, user.getFirstName());
            st.setString(2, user.getLastName());
            ResultSet rs = st.executeQuery();
            if(rs.next()){
            count = rs.getInt(1);
            }
            rs.close();
            st.close();
        }catch(SQLException e){
            System.out.println(e.getSQLState());
            
        }
        
        return count; 
       }
       else{
        String sql ="SELECT COUNT(*)"+
                    " FROM Users"+
                    " WHERE firstName=? AND userID =?";
        try{
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, user.getFirstName());
            st.setInt(2, user.getUserID());
            ResultSet rs = st.executeQuery();
            if(rs.next()){
            count = rs.getInt(1);
            }
            rs.close();
            st.close();
        }catch(SQLException e){
            System.out.println(e.getSQLState());
            
        }
        return count;
           
       }
       //return 0;
    }
    
    //Checks occurence of trip in database based on specified input
    public int tripCount(TripBookingInfo trip){
       int count = 0;

        String sql ="SELECT COUNT(*)"+
                    " FROM Trips"+
                    " WHERE (userID = ? AND location = ? AND startDate = ?) OR (userID = ? AND location = ? AND startDate = ? AND endDate =?)";
        try{
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, trip.getUserID());
            st.setString(2, trip.getLocation());
            st.setString(3, trip.getStartDate());
            st.setInt(4, trip.getUserID());
            st.setString(5, trip.getLocation());
            st.setString(6, trip.getStartDate());
            st.setString(7, trip.getEndDate());
            
                       
            ResultSet rs = st.executeQuery();
            if(rs.next()){
            count = rs.getInt(1);
            }
            rs.close();
            st.close();
        }catch(SQLException e){
            System.out.println(e.getSQLState());
            
        }
        
        return count; 

       //return 0;
    }

    //Creates user trip in sql database with specified details
    public void createTrip(TripBookingInfo trip){
        String sql = "INSERT INTO Trips Values(?, ?, ?, ?, ?)";
        
        try { 
            PreparedStatement st = con.prepareStatement(sql);            
            st.setInt(1, trip.getTripID());
            st.setInt(2, trip.getUserID());
            st.setString(3, trip.getLocation());
            st.setString(4, trip.getStartDate());
            st.setString(5, trip.getEndDate());
            st.executeUpdate();
            
            st.close();
        }
        
        catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    //Checks for appearance of trip in database 
    public int tripExist(int id){
        int count= 0;
        String sql ="SELECT COUNT(*)"+
                    " FROM Trips"+
                    " WHERE tripID=" + id;
        try{
            PreparedStatement st = con.prepareStatement(sql);            
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                count = rs.getInt(1);
            }
            rs.close();
            st.close();
        }catch(SQLException e){
            System.out.println(e.getSQLState());
            
        }
        return count;
    }
    

    //Creates new user based on specifed user details
    public void createUser(User user){
        String sql = "INSERT INTO Users Values(?, ?, ?)";
        
        try { 
            PreparedStatement st = con.prepareStatement(sql); 
            st.setInt(1, user.getUserID());
            st.setString(2, user.getFirstName());
            st.setString(3, user.getLastName());
            st.executeUpdate();
            
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    //Updates specified user trip with current details provided 
    public void updateTrip(TripBookingInfo trip){
        String sql = "UPDATE Trips"
                    +" SET location = ?,"
                    +"     startDate = ?,"
                    +"     endDate = ? "
                    +" WHERE tripID ="+ trip.getTripID();
        if(trip.getLocation() != null && trip.getStartDate() == null && trip.getEndDate() == null){
            sql = "UPDATE Trips"
                +" SET location = ?"
                +" WHERE tripID ="+ trip.getTripID();
            try{
                PreparedStatement st = con.prepareStatement(sql);
                st.setString(1, trip.getLocation());
                st.executeUpdate();
                
                st.close();
            }catch(SQLException ex){
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(trip.getLocation() != null && trip.getStartDate() != null && trip.getEndDate() == null){
            sql = "UPDATE Trips"
                +" SET location = ?,"
                +"     startDate = ?"
                +" WHERE tripID ="+ trip.getTripID();
            try{
                PreparedStatement st = con.prepareStatement(sql);
                st.setString(1, trip.getLocation());
                st.setString(2, trip.getStartDate());
                st.executeUpdate();
                
                st.close();
            }catch(SQLException ex){
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(trip.getLocation() == null && trip.getStartDate() != null && trip.getEndDate() != null){
            sql = "UPDATE Trips"
                +" SET startDate = ?,"
                +"     endDate = ?"
                +" WHERE tripID ="+ trip.getTripID();
            try{
                PreparedStatement st = con.prepareStatement(sql);
                st.setString(1, trip.getStartDate());
                st.setString(2, trip.getEndDate());
                st.executeUpdate();
                
                st.close();
            }catch(SQLException ex){
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        else if(trip.getLocation() != null && trip.getStartDate() != null && trip.getEndDate() != null){

            try{
                PreparedStatement st = con.prepareStatement(sql);
                st.setString(1, trip.getLocation());
                st.setString(2, trip.getStartDate());
                st.setString(3, trip.getEndDate());
                st.executeUpdate();
                
                st.close();
            }catch(SQLException ex){
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //Deletes specified user trips
    public void deleteUserTrip(int tripID){
        String sql = "DELETE FROM Trips WHERE tripID = "+ tripID;
        
        try{
            PreparedStatement st = con.prepareStatement(sql);
            st.executeUpdate();
            
            st.close();
        }catch(SQLException ex){
           Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);  
        }
    }
    
}
    
    
    
    

