package mdjun_CSCI201_PA2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mdjun_CSCI201_PA2.Util.Helper;

import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet implementation class RegisterDispatcher
 */
@WebServlet("/RegisterDispatcher")
public class RegisterDispatcher extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String url = "jdbc:mysql://localhost:3306/PA4Users";

    /**
     * Default constructor.
     */
    public RegisterDispatcher() {
    }


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: VERY IMPORTANT
    	// so I keep dropping all data each time I go to index.jsp
    	// or login.jsp or anything
    	// because I call .Init() for RestaurantDataParser
    	// but that will remove all users!!!!
    	
    	
    	String error = "";
    	
    	
    	// is email already registered??
    	// make a query to confirm
    	try {
            Class.forName("com.mysql.jdbc.Driver");
            
            // connect to database
            String db = "jdbc:mysql://localhost:3306/201_Miru_PA2";
            Connection con = DriverManager.getConnection(db, "root", "root"); 
            
            String sql = "SELECT 1 "
            		+ "FROM Users u "
        			+ "WHERE u.email = ?;"
        			;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, request.getParameter("regEmail"));
            
            ResultSet result = ps.executeQuery();
            System.out.println("Completed query for login.");
            
            if (result.next()) {
            	// email already exists!!
            	// return that fact
            	error += "<p>Email already registered! Please try again or login.</p>";
            	
            } else {
            	
            	// nope we're good
            	// nothing to do
            }
            
            con.close();
            
    	} catch (SQLException e) {
    		error += "SQL exception!";
    		System.out.println("Error: SQLException when checking email registration!");
        	System.out.println(e.getMessage());
        	e.getCause();
        	e.printStackTrace();
    	} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
    		error += "ClassNotFound exception!";
    		System.out.println("Error: Class not found when checking email registration!!!!!");
        	System.out.println(e.getMessage());
        	e.getCause();
        	e.printStackTrace();
		} catch (Exception e) {
			error += "Some exception!";
			System.out.println("Error: Some generic error when checking email registration!");
        	e.getCause();
        	e.printStackTrace();
		}
    	
    	// is email valid??
    	// is name valid??
    	Boolean validEmail = Helper.isValidEmail(request.getParameter("regEmail"));
    	Boolean validName = Helper.isValidName(request.getParameter("regName"));
    	// is password and confirm password the same?
    	Boolean samePassword = request.getParameter("regPassword").equals(request.getParameter("regConfirm"));
    	
    	
    	if (validEmail && validName && samePassword) {
    		// all gucci
    		// what do then????
    		
    		// insert into database!!!!!!!!!
			try {
				Class.forName("com.mysql.jdbc.Driver");
				
				
				// connect to database
	            String db = "jdbc:mysql://localhost:3306/201_Miru_PA2";
	            Connection con = DriverManager.getConnection(db, "root", "root"); 
	            
	            Statement state = con.createStatement();
	            
	            String sql = "INSERT INTO Users (email, username, password) VALUES"
	            		+ "("
	            		+ "'" + request.getParameter("regEmail") + "', "
	            		+ "'" + request.getParameter("regName") + "', "
	            		+ "'" + request.getParameter("regPassword") + "'"
	            		+ ");"
	            		;
	            		
	            state.execute(sql);
	            
	            System.out.println("Completed query for login.");
	            
	            
	            con.close();
				
	            
	            // send cookie
	            // a valid successful registration
	            System.out.println("successfully registered!!!!!! probably!!!!");
	            
	            
	            // make a cookie with the username!!!!
        		Cookie username = new Cookie("username", request.getParameter("regName").replaceAll(" ", "="));
        		username.setMaxAge(60 * 60); // 1 hr
        		response.addCookie(username);
        		
        		
	            
	            
			} catch (SQLException e) {
	    		error += "SQL exception!";
	    		System.out.println("Error: SQLException when registering user!");
	        	System.out.println(e.getMessage());
	        	e.getCause();
	        	e.printStackTrace();
	    	} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
	    		error += "ClassNotFound exception!";
	    		System.out.println("Error: Class not found when registering user!!!!!");
	        	System.out.println(e.getMessage());
	        	e.getCause();
	        	e.printStackTrace();
			} catch (Exception e) {
				error += "Some exception!";
				System.out.println("Error: Some generic error when registering user!!");
	        	e.getCause();
	        	e.printStackTrace();
			}
			
			
			
            
            
    		
    		
    	} else {
    		// no bueno!!
    		System.out.println("Email, name, or password is not valid! Please check again.");
    		error += "Email, name, or password is not valid! Please check again.";

    	}
    	
    	
    	
    	if (error == "") {
    		// all successful
    		// should go to home page 
    		response.setHeader("Refresh", "0; URL=" + request.getContextPath() + "/index.jsp");
			request.getRequestDispatcher("index.jsp").forward(request, response);
    		
    	} else {
    		// not all successful
    		request.setAttribute("error", error);
			request.getRequestDispatcher("login.jsp").include(request, response);
    	}
    	
    	
    	
    	
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
