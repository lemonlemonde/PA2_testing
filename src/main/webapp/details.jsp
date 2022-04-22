<!--  Details page   -->


<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ page import="mdjun_CSCI201_PA2.Util.RestaurantDataParser"%>
         
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Details</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
            href="https://fonts.googleapis.com/css2?family=Lobster&family=Roboto:wght@300&display=swap"
            rel="stylesheet">
           <script src="https://kit.fontawesome.com/1507e206c4.js" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="index.css">
    
</head>

<%


		Cookie[] cookies = null;
		cookies = request.getCookies();
		Cookie nameCookie = null;
		
		String hiUser = null;
		
		if (cookies != null) {
			
			
			System.out.println(" >>>> There is some cookies!!!!! length: " + cookies.length);
			// that means that we have a user logged in!!!
			// plz don't drop the database!!!!
			
			// okay so
			for (int i = 0; i < cookies.length; i++) {
				// we only have one cookie
				// System.out.println("found cookie!");
				nameCookie = cookies[i];
				System.out.println("cookie value:" + nameCookie.getValue());
				
				// this is the correct cookie
				if (nameCookie.getName().equals("username") && !nameCookie.getValue().equals("")) {
					System.out.println("Setting username cookie....");
					hiUser = "Hi " + nameCookie.getValue().toString().replaceAll("=", " ") + "!";
					
				} else {
					
				}
				
			}
			
		} else {
			RestaurantDataParser dataParser = new RestaurantDataParser();
					
			RestaurantDataParser.Init("um");
			
		}



%>

	<body>
	
	
	<% // header %>
	<!-- Header copy paste for everything else -->
        <div style="display: flex; flex-direction: row">
        	<a href="index.jsp">
		        <button style="font-family: 'Lobster'; font-size: 20pt; margin: 20px; margin-left: 40px; color: red; background-color: white; border: none">SalEats!</button>
        	</a>
        	
        	<div style="margin-top: 25px; font-size: 15pt"><%= (hiUser != null) ? hiUser : "" %></div> 
        	
	        <div style="margin-left: auto; display: flex; flex-direction: row; padding: 20px">
	        
				<a href="index.jsp"><button style="color: DarkGray; background-color: white; border: none; font-family: Roboto">Home</button></a>
				<form action="LoginDispatcher" method="GET">
					<!--  just check in loginDispatcher if username cookie  -->
        			<a href="login.jsp"><button type="submit" style="color: DarkGray; background-color: white; border: none; font-family: Roboto"><%= (hiUser != null) ? "Logout" : "Login / Register" %> </button></a>
				</form>
				
				
	        </div>
	        
        </div>
        <div id="divider" style="background-color: Whitesmoke; height: 1.5px; width: 100%"></div> 
        <!-- Header copy paste for everything else -->
        
        <% // header %>

		<form action="SearchDispatcher" method="GET">
			<div style="display: flex; flex-direction: row; margin-left: 40px; margin-top: 20px; max-width: 1100px">
				<div style="display: flex; flex-direction: row; max-width: 800px; width: 70%; height: 25px">
			       		
					<select id="searchFilter" name="searchFilter">
						<option value="searchName">Name</option>
						<option value="searchCategory">Category</option>	
					</select>
			       		
			       	
			       		
					<input type="text" id="searchBox" name="searchBox" style="width: 100%; background-color: AliceBlue; border: 1px solid Whitesmoke"> 
			        
					<button type="submit" class="fa-solid fa-magnifying-glass" style="color: white; background-color: red; padding: 4px; border: 2px solid red; border-radius:7px; width: 40px; margin-left: 20px; margin-right: 20px">   </button>
					
				</div>
				<div style="display: flex; flex-direction: column; margin-left: 10px; justify-content: flex-start; align-items: flex-start; max-width: 300px">
					<% //TODO: price is default filter %>
					<span style="padding-bottom: 10px">
						
						
						<input type="radio" checked="checked" id="price" name="searchOrder" value="price">
						<label for="price" style="margin-right: 70px">Price</label>
						
						<span>
							<input type="radio" id="reviewCount" name="searchOrder" value="reviewCount">
							<label for="reviewCount">Review Count</label>
						</span>
					</span>
					
					<span>
						<input type="radio" id="rating" name="searchOrder" value="rating">
						<label for="rating">Rating</label>
					</span>
				</div>
			</div>
		</form>
		<!-- Search bar copy paste for everything else -->
		
	
	
	<% // details %>
	<%
	String name = request.getParameter("name");
	String address = request.getParameter("address");
	String phone = request.getParameter("phone");
	String price = request.getParameter("price");
	String rating = request.getParameter("rating");
	String categories = request.getParameter("categories");
	%>
	
	<%
 		String starRating = "";
 		String extra = "<i class=\"fa-solid fa-star\"></i>";
 		String extraHalf = "<i class=\"fa-solid fa-star-half\"></i>";
 		
 		Boolean isFraction = false;
 		
 		Double ratingVal = Double.parseDouble(rating);
 		
 		if (ratingVal % 1.0 > 0) {
 			// if it has a fractional star...
 			isFraction = true;
 			ratingVal -= 1;
 		}
 		for (int i=0; i < ratingVal; i++) {
 			// if fraction, then we floor it here
 			// concatenate string
 			
 			starRating += extra;
 		}
 		if (isFraction) {
 			// add half a star to the starRating
 			starRating += extraHalf;
 		}
 		
	%>
	
	<h1 style="margin-left: 30px; margin-top: 60px">Name of restaurant: <%= name %></h1>
	<div style="display: flex; flex-direction: row; margin-left: 50px; margin-bottom: 10px; margin-top: -25px">
			<img src="banner.jpeg" style="width: 180px; height: 180px; margin: 30px; border-radius: 10px">
			<% //TODO: Link name of restaurant to Yelp %>
			<div style="margin-top: 30px">
				<p>Address: <%= address %></p>
				<p>Phone number: <%= phone %></p>
				<p>Categories: <%= categories %></p>
				<p>Price: <%= price %></p>
				<br>
				<p>Rating: <%= starRating %></p>
			</div>
	</div>
	
	
	
	
	</body>
</html>