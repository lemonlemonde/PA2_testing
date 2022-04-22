package mdjun_CSCI201_PA2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet implementation class LoginDispatcher
 */
@WebServlet("/LoginDispatcher")
public class LoginDispatcher extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //TODO
    	
    	String error = "";
    	
    	String loginEmail = request.getParameter("loginEmail");
    	System.out.println("Login email text:" + loginEmail);
    	
    	String loginPassword = request.getParameter("loginPassword");
    	System.out.println("Login password text:" + loginPassword);
    	
    	
    	
    	// check if we have a value for the username cookie
    	// if we do,
    	// that means that we're here to logout
    	// then just make a cookie of the same name
    	// and set the maxlifespan to 0 or smth
    	Cookie[] cookies = null;
		cookies = request.getCookies();
		Cookie nameCookie = null;
		
		if (cookies != null) {
			// that means that we have a user logged in!!!
			// plz don't drop the database!!!!
			
			// okay so
			// just delete all cookies
			
			Boolean foundUser = false;
			
			System.out.println(" >>>> DELETING ALL COOKIE AHHHHHHH");
			for (int i = 0; i < cookies.length; i++) {
				// we only have one cookie
				System.out.println("found cookie!");
				nameCookie = cookies[i];
				System.out.println("cookie value:" + nameCookie.getValue());
				
				
				
				
				// if there's a cookie that is a username
				// REMOVE 
				// that means we here to logout
				Cookie cookie = new Cookie(nameCookie.getName(), "");
				cookie.setMaxAge(0); // 0 to kill it immediately
				System.out.println("Now, deleted cookie has value: " + cookie.getValue());
				response.addCookie(cookie);
				
				if (nameCookie.getName().equals("username")) {
					foundUser = true;
				}
				
				
			}
			if (foundUser) {
				response.setHeader("Refresh", "0; URL=" + request.getContextPath() + "/index.jsp");
				
				request.getRequestDispatcher("index.jsp").forward(request, response);
				return;
				
			}
			
		} else {
			// nothing to do
			// b/c we're not here to logout
		}
    	
		
    	
    	
    	try {
            Class.forName("com.mysql.jdbc.Driver");
            
            // connect to database
            String db = "jdbc:mysql://localhost:3306/201_Miru_PA2";
            Connection con = DriverManager.getConnection(db, "root", "root"); 
            
            String sql = "SELECT u.password, u.username "
            		+ "FROM Users u "
        			+ "WHERE u.email=?;"
        			;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, request.getParameter("loginEmail"));
            
            ResultSet result = ps.executeQuery();
            System.out.println("Completed query for login.");
            
            if (result.next()) {
            	System.out.println("We have a result back");
            	// check that password is correct
            	if (result.getString("password").equals(request.getParameter("loginPassword"))) {
            		// password retrieved for input email == input password
            		// correct!
            		System.out.println("Successfully logged in!");
            		
            		// make a cookie with the username!!!!
            		Cookie username = new Cookie("username", result.getString("username").replaceAll(" ", "="));
            		username.setMaxAge(60 * 60); // 1 hr
            		response.addCookie(username);
            		
            	} else {
            		// bad!!
            		System.out.println("Wrong password!");
                	System.out.println("No matching email or incorrect password. Please try again or register.");
                	error += "<p style=\"padding: 20; height: 20\">Error: no matching email or incorrect password. Please try again or register.</p>";
            		
            	}
            } else {
            	// bad!!
            	System.out.println("No results matching!!!");
            	
            	System.out.println("No matching email or incorrect password. Please try again or register.");
            	error += "<p style=\"padding: 20; height: 20\">Error: no matching email or incorrect password. Please try again or register.</p>";
            }
            
            con.close();
            
    	} catch (SQLException e) {
    		error += "SQL exception!";
    		System.out.println("Error: SQLException when creating tables in the database (or when inserting)!");
        	System.out.println(e.getMessage());
        	e.getCause();
        	e.printStackTrace();
    	} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
    		error += "ClassNotFound exception!";
    		System.out.println("Error: Class not found!!!!");
        	System.out.println(e.getMessage());
        	e.getCause();
        	e.printStackTrace();
			e.printStackTrace();
		} catch (Exception e) {
			error += "Some exception!";
			System.out.println("Error: Some generic error when creating tables in the database (or when inserting)!");
        	e.getCause();
        	e.printStackTrace();
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
        doGet(request, response);
    }
}
