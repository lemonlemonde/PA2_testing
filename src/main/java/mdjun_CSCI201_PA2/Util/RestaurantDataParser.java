package mdjun_CSCI201_PA2.Util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import com.google.gson.*;

import java.io.*;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// I imported theseeee
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * A class that pretends to be the Yelp API
 */

public class RestaurantDataParser {
    private static Boolean ready = false;
    
    public static Businesses businesses = null;

    /**
     * Initializes the DB with json data
     *
     * @param responseString the Yelp json string
     */
    public static void Init(String responseString) {
        if (ready) {
            return;
        }
        
        
        try {
        	// make sure we correctly have driver
        	DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        	Class.forName("com.mysql.jdbc.Driver");

        	// um so make database here?
        	// connect to local
            String local = "jdbc:mysql://localhost:3306/";
            Connection con = DriverManager.getConnection(local, "root", "root"); 
            
            // remove database if we already have one
            // in case there's a duplicate when I'm testing
            Statement state = con.createStatement();
            
            
            // make database 
            String sql2 = "CREATE DATABASE IF NOT EXISTS 201_Miru_PA2";
        	
            state.addBatch(sql2);
            
            state.executeBatch();
            
            
            
            
            con.close();
            
            String tables = "jdbc:mysql://localhost:3306/201_Miru_PA2";
            Connection con2 = DriverManager.getConnection(tables, "root", "root"); 
            Statement stateTables = con2.createStatement();

            String sqlTables = "DROP TABLE Category DROP TABLE Rating_details DROP TABLE Restaurant DROP TABLE Restaurant_bridge_Category DROP TABLE Restaurant_details;";
            
            stateTables.execute(sqlTables);
            
            con2.close();
            
            
        } catch (SQLException e) {
        	System.out.print("Error: SQLException when registering driver for creating database perhaps?");
        	e.printStackTrace();
        	
    	} catch (ClassNotFoundException e) {
        	System.out.print("Error: Class not found exception!???");
            e.printStackTrace();
        }
        
        
       
        // Let's get this bread
        // I mean let's read in the json file
        
        try {
        	// if no errors, it's gooood
        	// possible errors to check:
        		// file not found
        		// data can't be converted to proper type
        		// missing data parameters
        	
        	// read file using gson
        	Gson gson = new Gson();
//        	String fileName = "test3.json";
        	
        	// regardless of what I did, couldn't find file
        	// so I'm using class context
        	// need to do Businesses.class which is a static wrapper class
        	InputStream stream = Businesses.class.getResourceAsStream("restaurant_data.json");
        	Reader reader = new InputStreamReader(stream, "UTF-8");
        	businesses = gson.fromJson(reader, Businesses.class);
        	
        	
        } catch (IOException e) {
        	e.printStackTrace();
        	System.out.println(e.getCause());
        	System.out.println("Error: The file 'restaurant_data.json' could not be found.\n\n");
        	return;
        } catch (JsonParseException e) {
			// data can't be converted to proper type in business object
			System.out.println(e.getCause());
			System.out.println("Error: Json parsing exception. Data not in proper type(s). Please retry.\n\n");
			return;
		} catch (Exception e) {
			e.printStackTrace();
        	System.out.println(e.getCause());
			System.out.println("Error: Unable to read file. Please retry.\n\n");
			return;
		} finally {
			// nothing much to do here
		}
        
        
        
        try {
        	// the database should have been created above
        	// now connect to it
        	String db = "jdbc:mysql://localhost:3306/201_Miru_PA2";
            Connection con = DriverManager.getConnection(db, "root", "root"); 
            
        	
        	// We ensured that nothing is in this database 
        	// since we deleted any possible old data
        	// and made a new spangled one
        	
        	
            Statement state = con.createStatement();
     
            // truncating urls at 300 for now
            // and address at 100
            // Restaurant_details table            
        	String sql1 = "CREATE TABLE Restaurant_details ("
        			+ "details_id int NOT NULL AUTO_INCREMENT,"
        			+ "image_url VARCHAR(300),"
        			+ "address VARCHAR(100),"
        			+ "phone_no VARCHAR(45),"
        			+ "estimated_price VARCHAR(45),"
        			+ "yelp_url VARCHAR(300),"
        			+ "PRIMARY KEY (details_id)"
        			+ ");"
        			;
        	state.addBatch(sql1);
        	
        	// Rating_details table
        	String sql2 = "CREATE TABLE Rating_details ("
        			+ "rating_id int NOT NULL AUTO_INCREMENT,"
        			+ "review_count int,"
        			+ "rating double,"
        			+ "PRIMARY KEY (rating_id)"
        			+ ");"
        			;
        	state.addBatch(sql2);
        	
        	// Restaurant table
        	String sql3 = "CREATE TABLE Restaurant ("
        			+ "restaurant_id VARCHAR(45) NOT NULL,"
        			+ "restaurant_name VARCHAR(45) NOT NULL,"
        			+ "details_id int,"
        			+ "rating_id int,"
        			+ "PRIMARY KEY (restaurant_id),"
        			+ "FOREIGN KEY (details_id) REFERENCES Restaurant_details(details_id),"
        			+ "FOREIGN KEY (rating_id) REFERENCES Rating_details(rating_id)"
        			+ ");"
        			;
        	state.addBatch(sql3);
        	
        	// Category table
        	String sql4 = "CREATE TABLE Category ("
        			+ "category_id int NOT NULL AUTO_INCREMENT,"
        			+ "category_name VARCHAR(45),"
        			+ "PRIMARY KEY (category_id)"
        			+ ");"
        			;
        	state.addBatch(sql4);

        	
        	// this is the bridge table for allowing multiple categories for every restaurant
        	String sql5 = "CREATE TABLE Restaurant_bridge_Category ("
        			+ "restaurant_id VARCHAR(45),"
        			+ "category_id int"
        			+ ");" 
        			;
        	state.addBatch(sql5);
        		
			state.executeBatch();
			
			
			
			String sqlUsers = "CREATE TABLE Users ("
					+ "email VARCHAR(45),"
					+ "username VARCHAR(45), "
					+ "password VARCHAR(45)"
					+ ");"
					;
			state.execute(sqlUsers);
			
			
			
			
			// now go through the businesses array and put into db
	        // iterate from 0 to size of the list of 'Business" objects
	        for (int i = 0; i < businesses.getBusinesses().size(); i++) {
	        	Business curBus = businesses.getBusinesses().get(i);

	        	// so mysql doesn't like apostrophes
	            // so we need to escape it ofc
	            // because we need to for points ://
	            // so
	            // apostrophes only seem to be in the restaurant names
	        	// going to try double single apostrophes
	            curBus.setName(curBus.getName().replaceAll("'", "''"));
	        	
	        	
	        	
	        	String sql7 = "INSERT INTO Restaurant_details (image_url, address, phone_no, estimated_price, yelp_url) VALUES"
	        			+ "(" 
	        			+ "'" + curBus.getImageUrl() + "', " 
	        			+ "'" + curBus.getLocation().getDisplayAddress() + "', " 
	        			+ "'" + curBus.getDisplayPhone() + "', "
	        			+ "'" + curBus.getPrice() + "', "
	        			+ "'" + curBus.getUrl() + "'"
	        			+ ");"
	        			;
	        	state.execute(sql7);
	        			
	        	
	        	String sql8 = "INSERT INTO Rating_details (review_count, rating) VALUES"
	        			+ "(" 
	        			+ "'" + curBus.getReviewCount() + "', " 
	        			+ "'" + curBus.getRating() + "'"
	        			+ ");"
	        			;
	        	state.execute(sql8);
	        	
	        	// restaurant_id = business id
	        	// restaurant_name = business name
	        	// insert each iteration
	        	
	        	// now also need to link the foreign key values for:
	        	// details_id
	        	// rating_id
	        	// put it into the Restaurant table, since those are the foreign keys
	        	// but! those are auto-incremented, so we can just use i+1, since 1 based
	        	String sql6 = "INSERT INTO Restaurant (restaurant_id, restaurant_name, details_id, rating_id) VALUES"
	        			+ "(" 
	        			+ "'" + curBus.getId() + "', "
	        			+ "'" + curBus.getName() + "', " 
	        			+ "'" + (i+1) + "', "
	        			+ "'" + (i+1) + "'"
	        			+ ");"
	        			;
	        	state.execute(sql6);


	        	// there could be multiple categories for one  business 
	        	// thus another array
	        	for (int j = 0; j < curBus.getCategories().size(); j++) {
	        		String sql9 = "INSERT INTO Category (category_name) VALUES"
		        			+ "(" 
		        			+ "'" + curBus.getCategories().get(j).getTitle() + "'"
		        			+ ");"
		        			;
	        		state.execute(sql9);
	        		
	        		
	        	}
	        	
	        	
	        	// By this time, all the tables have been populated. 
	        	// Now I can get the auto-incremented id values
	        	
	        	
	        	// get the category_id and restaurant_id to populate the bridge table
	        	
	        	for (int j = 0; j < curBus.getCategories().size(); j++) {
	        		// I need to do a select thing for each category
	        		String sql10 = "SELECT c.category_id, r.restaurant_id "
	        				+ "FROM Category c, Restaurant r "
	        				+ "WHERE r.restaurant_name=? "
	        				+ "AND c.category_name=?;";
	        		
	        		PreparedStatement ps = con.prepareStatement(sql10);
	        		// get current restaurant name
	        		// the escape for the apostrophe is getting mseed up again
	        		// reveret back!
	            	ps.setString(1, curBus.getName().replaceAll("''", "'"));
	            	// get all the current restaurant's categories' names
	            	ps.setString(2, curBus.getCategories().get(j).getTitle());
	            	
	            	ResultSet result = ps.executeQuery();
	        		
	            	
	            	result.next();
	            	
	            	// add to the table using the id info we just got
	            	String sql11 = "INSERT INTO Restaurant_bridge_Category (category_id, restaurant_id) VALUES"
	            			+ "("
	            			+ "'" + result.getInt("category_id") + "', "
	            			+ "'" + result.getString("restaurant_id") + "'"
	            			+ ");"
	            			;
	            	state.execute(sql11);
	            	
	        	}
	        			
	        			
	        	
	        	
	        }
	        
	        // close connection
	        con.close();
        	
        } catch (SQLException e) {
        	System.out.println("Error: SQLException when creating tables in the database (or when inserting)!");
        	System.out.println(e.getMessage());
        	e.getCause();
        	e.printStackTrace();
        	return;
        } catch (Exception e) {
        	System.out.println("Error: Some generic error when creating tables in the database (or when inserting)!");
        	e.getCause();
        	e.printStackTrace();
        	return;
        }
        
        
        
        // putting the change of flag here instead of at the very top
        // because even if there's an error in the middle,
		// I want people to be able to retry..?...?????
        ready = true;
        
        
    }

    
    
    
    // below are functions for searching the database

    
    
    
    // this one is when you.....click on smth and you need to show the
    // further details???
    public static Business getBusiness(String id) {
        try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //TODO return business based on id
        return null;
    }

    
    
    
    // this one is for the general serach
    /**
     * @param keyWord    the search keyword
     * @param sort       the sort option (price, review count, rating)
     * @param searchType search in category or name
     * @return the list of business matching the criteria
     */
    public static Businesses getBusinesses(String keyWord, String sort, String searchType) {
        Businesses businesses = new Businesses();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            // connect to database
            String db = "jdbc:mysql://localhost:3306/201_Miru_PA2";
            Connection con = DriverManager.getConnection(db, "root", "root"); 
            
            // make statement
//            Statement state = con.createStatement();
            
            // make query
            // use "keyWord" to search within searchType and select the matching businesses
            // use "sort" to sort those selected
            if (searchType != null && searchType.equals("searchCategory")) {
            	// LIKE 'J%' only looks at STARTING WITH stringsssssss
            	// LIKE '%J%' is contains
            	
//            	String sql = "SELECT c.category_name, r.restaurant_name, d.image_url, d.address, d.phone_no, d.estimated_price, d.yelp_url, rating.review_count, rating.rating "
//            			+ "FROM Category c "
//            			+ "INNER JOIN Restaurant_bridge_Category b ON b.category_id = c.category_id "
//            			+ "INNER JOIN Restaurant r ON b.restaurant_id = r.restaurant_id "
//            			+ "INNER JOIN Restaurant_details d ON d.details_id = r.details_id "
//            			+ "INNER JOIN Rating_details rating ON rating.rating_id = r.rating_id "
//            			+ "WHERE category_name LIKE ?;";
            	
            	
            	// Sort Price and Restaurant Name in Ascending Order
            	// Sort Ratings and Review count in Descending Order
            	
            	// Restaurant Name in Ascending order as secondary order
            	
            	
            	String sql = "SELECT c.category_name, r.restaurant_name, d.image_url, d.address, d.phone_no, d.estimated_price, d.yelp_url, x.review_count, x.rating "
            			+ "FROM Category c, Restaurant r, Restaurant_details d, Restaurant_bridge_Category b, Rating_details x "
            			+ "WHERE category_name LIKE ? "
            			+ "AND b.category_id = c.category_id "
            			+ "AND b.restaurant_id = r.restaurant_id "
            			+ "AND d.details_id = r.details_id "
            			+ "AND x.rating_id = r.rating_id "
            			+ "ORDER BY <sort>;"
            			;
            	
//            	System.out.println("  >>>> THIS IS THHE SORT: " + sort);
            	
            	if (sort.equals("price")) {
            		sql = sql.replaceAll("<sort>", "d.estimated_price, r.restaurant_name");
            		
            	} else if (sort.equals("reviewCount")) {
            		sql = sql.replaceAll("<sort>", "x.review_count DESC, r.restaurant_name");
            		
            	} else if (sort.equals("rating" )) {
            		sql = sql.replaceAll("<sort>", "x.rating DESC, r.restaurant_name");
            		
            	} else {
            		System.out.println("This is the sort: " + sort);
            		System.out.println("ERROR: You done messed up; your sort doesn't fit any of the hard coded stuff");
            	}
            	
            	PreparedStatement ps = con.prepareStatement(sql);
            	ps.setString(1, "%" + keyWord + "%");
            	// TODO: do i have to make all lowercase??
            	
            	// do some magicaly query shit
            	// get the query results
            	ResultSet result = ps.executeQuery();
            	
            	System.out.println("category search query has been executed.");
            	
            	// display the corresponding results
            	String prevName = "";
            	Business prevBusiness = null;
            	
            	List<Business> listBusinesses = new ArrayList<Business>();
            	
            	while (result.next()) {
            		
            		// one restaurant could have multiple categories!!!!!
            		// make sure to check the restaurant_name. 
            		// if same as previous, then 
            		String name = result.getString("restaurant_name");
            		String category = result.getString("category_name");
            		String imageURL = result.getString("image_url");
            		String URL = result.getString("yelp_url");
            		String address = result.getString("address");
            		String phone = result.getString("phone_no");
            		String price = result.getString("estimated_price");
            		Integer reviewCount = result.getInt("review_count");
            		Double rating = result.getDouble("rating");
            		
            		
            		if (name.equals(prevName)) {
            			System.out.println("Same restaurant name!");
            			System.out.println("restaurant: " + name + " has extra category: " + category);
            			// same restaurant!
            			// just different category
            			// edit previous business
            			
            			// make new category
            			Category newCategory = new Category();
            			newCategory.setTitle(category);
            			
            			// make list of categories existing in prev business
            			// and update it by adding category
            			// then set prev business categories to new list
            			List<Category> listCategories = prevBusiness.getCategories();
            			listCategories.add(newCategory);
            			prevBusiness.setCategories(listCategories);
            			
            			
            		} else {
            			// it's a new restaurant
            			
            			Business newBusiness = new Business();
            			
            			// name
            			newBusiness.setName(name);
            			
            			// make new category
            			Category newCategory = new Category();
            			newCategory.setTitle(category);
            			
            			// make list of categories
            			// and set it 
            			List<Category> listCategories = new ArrayList<Category>();
            			listCategories.add(newCategory);
            			newBusiness.setCategories(listCategories);
            			
            			// imageURL
            			newBusiness.setImageUrl(imageURL);
            			
            			// yelpURL
            			newBusiness.setUrl(URL);
            			
            			// address
            			// Make location
            			// and make list of addresses
            			// then set location
            			Location newLocation = new Location();
            			List<String> listAddresses = new ArrayList<String>();
            			listAddresses.add(address);
            			newLocation.setDisplayAddress(listAddresses);
            			newBusiness.setLocation(newLocation);
            			
            			// phone
            			newBusiness.setDisplayPhone(phone);
            			
            			// price
            			newBusiness.setPrice(price);
            			
            			// review count
            			newBusiness.setReviewCount(reviewCount);
            			
            			// rating
            			newBusiness.setRating(rating);
            			
            			listBusinesses.add(newBusiness);

            		}
            		
            		
        			prevName = name;
        			// get last business
        			prevBusiness = listBusinesses.get(listBusinesses.size()-1);
            		
            	}
            	
            	businesses.setBusinesses(listBusinesses);
            	
            	
            	// TODO: warning!!
            	// one restaurant has multiple categories
            	// so when you search by name
            	// it'll list the same restaurant multiple times
            	// with different categories!!!
            	// Make sure that I manually check that shit
            	// when I'm displaying the restaurants
            	// check that the name of the restaurants aren't the same
            	// if same, then merge the categories somehow
            } else if (searchType == null || searchType.equals("searchName")) {
            	String sql = "SELECT c.category_name, r.restaurant_name, d.image_url, d.address, d.phone_no, d.estimated_price, d.yelp_url, x.review_count, x.rating "
            			+ "FROM Category c, Restaurant r, Restaurant_details d, Restaurant_bridge_Category b, Rating_details x "
            			+ "WHERE restaurant_name LIKE ? "
            			+ "AND b.category_id = c.category_id "
            			+ "AND b.restaurant_id = r.restaurant_id "
            			+ "AND d.details_id = r.details_id "
            			+ "AND x.rating_id = r.rating_id "
            			+ "ORDER BY <sort>;"
            			;
            	
            	if (sort.equals("price")) {
            		sql = sql.replaceAll("<sort>", "d.estimated_price, r.restaurant_name");
            		
            	} else if (sort.equals("reviewCount")) {
            		sql = sql.replaceAll("<sort>", "x.review_count DESC, r.restaurant_name");
            		
            	} else if (sort.equals("rating" )) {
            		sql = sql.replaceAll("<sort>", "x.rating DESC, r.restaurant_name");
            		
            	} else {
            		System.out.println("ERROR: You done messed up; your sort doesn't fit any of the hard coded stuff");
            	}
            	
            	PreparedStatement ps = con.prepareStatement(sql);
            	
            	ps.setString(1, "%" + keyWord + "%");
            	// TODO: do i have to make all lowercase??
            	
            	// do some magicaly query shit
            	// get the query results
            	ResultSet result = ps.executeQuery();
            	
            	// somehow display the results
            	// display the corresponding results
            	String prevName = "";
            	Business prevBusiness = null;
            	
            	List<Business> listBusinesses = new ArrayList<Business>();
            	while (result.next()) {
            		
            		// one restaurant could have multiple categories!!!!!
            		// make sure to check the restaurant_name. 
            		// if same as previous, then 
            		String name = result.getString("restaurant_name");
            		String category = result.getString("category_name");
            		String imageURL = result.getString("image_url");
            		String URL = result.getString("yelp_url");
            		String address = result.getString("address");
            		String phone = result.getString("phone_no");
            		String price = result.getString("estimated_price");
            		Integer reviewCount = result.getInt("review_count");
            		Double rating = result.getDouble("rating");
            		
            		
            		if (name.equals(prevName)) {
            			System.out.println("Same restaurant name!");
            			System.out.println("restaurant: " + name + " has extra category: " + category);
            			// same restaurant!
            			// just different category
            			// edit previous business
            			
            			// make new category
            			Category newCategory = new Category();
            			newCategory.setTitle(category);
            			
            			// make list of categories existing in prev business
            			// and update it by adding category
            			// then set prev business categories to new list
            			List<Category> listCategories = prevBusiness.getCategories();
            			listCategories.add(newCategory);
            			prevBusiness.setCategories(listCategories);
            			
            			
            		} else {
            			// it's a new restaurant
            			
            			Business newBusiness = new Business();
            			
            			// name
            			newBusiness.setName(name);
            			
            			
            			// make new category
            			Category newCategory = new Category();
            			newCategory.setTitle(category);
            			
            			// make list of categories
            			// and set it 
            			List<Category> listCategories = new ArrayList<Category>();
            			listCategories.add(newCategory);
            			newBusiness.setCategories(listCategories);
            			
            			// imageURL
            			newBusiness.setImageUrl(imageURL);
            			
            			// yelpURL
            			newBusiness.setUrl(URL);
            			
            			// address
            			// Make location
            			// and make list of addresses
            			// then set location
            			Location newLocation = new Location();
            			List<String> listAddresses = new ArrayList<String>();
            			listAddresses.add(address);
            			newLocation.setDisplayAddress(listAddresses);
            			newBusiness.setLocation(newLocation);
            			
            			// phone
            			newBusiness.setDisplayPhone(phone);
            			
            			// price
            			newBusiness.setPrice(price);
            			
            			// review count
            			newBusiness.setReviewCount(reviewCount);
            			
            			// rating
            			newBusiness.setRating(rating);
            			
            			listBusinesses.add(newBusiness);
            		}
            		
            		
        			prevName = name;
        			// get last business
        			prevBusiness = listBusinesses.get(listBusinesses.size()-1);
            		
            	}
            	
            	
            	businesses.setBusinesses(listBusinesses);
            	
            	
            	
            	
            } else {
            	System.out.println("Wtf??? you messed up somehow on the searchType inputs");
            	return null;
            }
            
            
            // close connection
            con.close();
            
            
            
        } catch (SQLException e) {
        	System.out.println("Error: SQLException when creating tables in the database (or when inserting)!");
        	System.out.println(e.getMessage());
        	e.getCause();
        	e.printStackTrace();
    	} catch (ClassNotFoundException e) {
    		System.out.println("Error: Class not found exception.");
            e.printStackTrace();
        } catch (Exception e) {
        	System.out.println("Error: Some generic error when creating tables in the database (or when inserting)!");
        	e.getCause();
        	e.printStackTrace();
        }
        
        // we're returning the local variable!
        // not the global that we made all the way at the top
        // is okay!!!
        return businesses;

    }
}



