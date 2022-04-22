<!--  Searcg page   -->


<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
         
<%@ page import="mdjun_CSCI201_PA2.Util.RestaurantDataParser"%>
<%@ page import="mdjun_CSCI201_PA2.Util.Businesses"%>


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



         
<!DOCTYPE html>
<html>
	<head>
	    <meta charset="UTF-8">
	    <title>Search</title>
	    <link rel="preconnect" href="https://fonts.googleapis.com">
	    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	    <link
	            href="https://fonts.googleapis.com/css2?family=Lobster&family=Roboto:wght@300&display=swap"
	            rel="stylesheet">
	    <link rel="stylesheet" href="index.css">
	    <script src="https://kit.fontawesome.com/3204349982.js"
	            crossorigin="anonymous"></script>
	
	
		<script src="https://kit.fontawesome.com/1507e206c4.js" crossorigin="anonymous"></script>
        <link rel="stylesheet"
          href="https://fonts.googleapis.com/css?family=Lobster">
	
	</head>
	<body>
<!-- TODO -->


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
        
        <div style="color:purple">
			<% String er = (String) request.getAttribute("error");
			if (er != null) out.println(er);%>
		</div>
        
		<% // search bar %>
		<!-- Search bar copy paste for everything else -->
		<% // TODO: need to copy paste the form style of the search bar for everything else %>
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
		
		

		<div style="margin: 40px">
			<% //TODO: Put the input and category or name here %>
			
			<%
			String filter = ((request.getParameter("searchFilter") == null) || request.getParameter("searchFilter").equals("searchName")) ? "Name" : "Category";
			%>
			<%
				String searchBox = "";
				if (request.getParameter("searchBox") == null || request.getParameter("searchBox").isEmpty()) {
					searchBox = "all";
					// if they don't have anything
					// they just searching all restaurants by price/reviewCount/rating whatever
					// and this will be used by the "Results for ____" 
				} else {
					searchBox = request.getParameter("searchBox");
				}
			%>
			
			<p style="font-size: 20pt; font-weight: 900; color: DarkGray">Results for <%= searchBox %> in <%= filter %> </p>
			
			<div id="divider" style="background-color: Whitesmoke; height: 1.5px; width: 80%"></div> 
			
			
			
			<%
				Businesses searchedBusinesses = RestaurantDataParser.getBusinesses(request.getParameter("searchBox"), request.getParameter("searchOrder"), request.getParameter("searchFilter"));
			%>
			
			
			
			<% if (searchedBusinesses != null && searchedBusinesses.getBusinesses() != null) { %>
				<% for (mdjun_CSCI201_PA2.Util.Business b : searchedBusinesses.getBusinesses()) {%>
				    <div id="<%= b.getName() %>" style="display: flex; flex-direction: row; margin-left: 50px; margin-bottom: 10px">
				    	<img src=<%= b.getImageUrl() %> style="width: 180px; height: 180px; margin: 30px; border-radius: 10px">
				    	<div style="margin-top: 30px">
				    		<%
							String price = "unavailable";
				    		if (!b.getPrice().equals("null")) {
								price = b.getPrice();
							} else {
								System.out.println("price not avail for: " + b.getName());
							}
				    		%>
				    		
				    		<%
				    		String starRating = "";
				    		String extra = "<i class=\"fa-solid fa-star\"></i>";
				    		String extraHalf = "<i class=\"fa-solid fa-star-half\"></i>";
				    		
				    		Boolean isFraction = false;
				    		
				    		Double ratingVal = b.getRating();
				    		
				    		if (b.getRating() % 1.0 > 0) {
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
				    	
				    	
				    	<%
				    	String strAddress = String.join(", ", b.getLocation().getDisplayAddress());
				    	strAddress = strAddress.replaceAll("\\[", "");
				    	strAddress = strAddress.replaceAll("\\]", "");
				    	strAddress = strAddress.replaceAll(" ", "+");
				    	
				    	String strName = b.getName().replaceAll(" ", "+");
				    	
				    	String phone = b.getDisplayPhone().replaceAll(" ", "+");
				    	
				    	String categories = "";
				    	
				    	for (int i = 0; i < b.getCategories().size()-1; i++) {
				    		categories += b.getCategories().get(i).getTitle() + ",+";
				    	}
				    	
				    	categories += b.getCategories().get(b.getCategories().size()-1).getTitle();
				    	
				    	categories = categories.replaceAll(" ", "+");
				    	
				    	
				    	%>
				    	
				    	
							<a href=<%= "details.jsp?name=" + strName
											+ "&price=" + price
											+ "&reviewCount=" + b.getReviewCount().toString() 
											+ "&rating=" + b.getRating()
											+ "&address=" + strAddress
											+ "&phone=" + phone
											+ "&categories=" + categories
											+ "&url=" + b.getUrl()
											
											%> type="submit" id="name">
											<%= b.getName() %>
							</a>
							<p id="price">Price: <%= price %></p>
							<p id="reviewCount">Review Count: <%= b.getReviewCount()%></p>
							<p id="rating">Rating: <%= starRating %></p>
							<a id="url" href=<%= b.getUrl() %>>Yelp URL</a>
							

						</div>
				    </div>
				    <div id="divider" style="background-color: Whitesmoke; height: 1.5px; width: 80%"></div> 
				<% } %>
			
			<% } %>
			
			
		
			
			
		</div>
		





	</body>
</html>