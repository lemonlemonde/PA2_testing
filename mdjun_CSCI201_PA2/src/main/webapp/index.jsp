<!--  Home page   -->


<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>


<%@ page import="mdjun_CSCI201_PA2.Util.RestaurantDataParser"%>



<%

// check the cookies!


		Cookie[] cookies = null;
		cookies = request.getCookies();
		Cookie nameCookie = null;
		
		String hiUser = null;
		
		if (cookies != null) {
			
			
			System.out.println(" >>>> There is some cookies!!");
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
					
			RestaurantDataParser.Init("aa");
			
		}


%>


    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Home</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Lobster&family=Roboto:wght@300&display=swap"
            rel="stylesheet">
        <link rel="stylesheet" href="index.css">
        <script src="https://kit.fontawesome.com/1507e206c4.js" crossorigin="anonymous"></script>
        
        <!-- // for google login/logout -->
        <meta name="google-signin-client_id" content="877910387394-ufhsr40pe5pq31b6cajqtup30gh9vpji.apps.googleusercontent.com">
    
    </head>

    <body>
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

        
       
        
        <div style="width: auto; background-color: pink; height: auto; justify-content: center; align-content: center">
			<% String er = (String) request.getAttribute("error");
			if (er != null) out.println(er);%>
		</div>
        
        
        <br>
        <br>
        
        <img src="banner.jpeg" style="width: 95%; margin: 20px; border-radius: 10px">
        
        <br>
		<br>
        
       	<% // search bar %>
		<!-- Search bar copy paste for everything else -->
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
        
		
        
    </body>

    </html>