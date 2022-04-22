<!--  Login page   -->


<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    
<%@ page import="mdjun_CSCI201_PA2.Util.RestaurantDataParser"%>

<%

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
	<title>Login/Register</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
            href="https://fonts.googleapis.com/css2?family=Lobster&family=Roboto:wght@300&display=swap"
            rel="stylesheet">
           <script src="https://kit.fontawesome.com/1507e206c4.js" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="index.css">
    
    <!-- // for google login/logout -->
    <!-- got my client id -->
    <meta name="google-signin-client_id" content="877910387394-ufhsr40pe5pq31b6cajqtup30gh9vpji.apps.googleusercontent.com">
    <script src="https://apis.google.com/js/platform.js?onload=renderButton" async defer></script>
    
</head>
<body>
	<% // header %>
	<!-- Header copy paste for everything else -->
    <% //TODO: Change to Logout if logged in already %>
    <div style="display: flex; flex-direction: row">
    	<a href="index.jsp">
      <button style="font-family: 'Lobster'; font-size: 20pt; margin: 20px; margin-left: 40px; color: red; background-color: white; border: none">SalEats!</button>
    	</a>
    	
     <div style="margin-left: auto; padding: 20px">
     
		<a href="index.jsp"><button style="color: DarkGray; background-color: white; border: none; font-family: Roboto">Home</button></a>
     	<a href="login.jsp"><button style="color: DarkGray; background-color: white; border: none; font-family: Roboto">Login / Register</button></a>
     	
     </div>
     
    </div>
    <div id="divider" style="background-color: Whitesmoke; height: 1.5px; width: 100%"></div> 
    <!-- Header copy paste for everything else -->
	
	
	
	
	
	<div style="width: auto; background-color: pink; padding: 20; height: auto; justify-content: center; align-content: center">
			<% String er = (String) request.getAttribute("error");
			if (er != null) out.println(er);%>
	</div>
		
		
		
	
	<!-- two columns: login and register -->
	
	<div style="display: flex; flex-direction: row; height: 500px; justify-content: center">
		<!-- Login -->
		
		<div style="display: flex; flex-direction: column; justify-content: center; align-content: flex-end; height: 500px; width: 100%; max-width: 400px; margin-right: 10px">
			<form action="LoginDispatcher" method="GET" style="display: flex; flex-direction: column; justify-content: center; align-content: flex-end; height: 500px; width: 100%; max-width: 400px; margin-right: 10px">
			
				<span style="font-size: 15pt; font-weight: 900; color: black">Login</span> <br>
				<span>Email</span> 
				<input type="text" id="loginEmail" name="loginEmail" style="border: 1px solid Whitesmoke; width: auto" required> <br>
				
				<span>Password</span>
				<input type="text" id="loginPassword" name="loginPassword" style="border: 1px solid Whitesmoke" required> <br>
				
				<button style="background-color: red; color: white; height: 30px; border: none; font-size: light">
					<i class="fa-solid fa-right-to-bracket"></i> Sign In
				</button>
			</form>
			
			<!-- google sign in button -->
			<div style="margin-top: -150px; display: flex; flex-direction: row; justify-content: center; align-content: flex-end; height: 50; width: 100%; max-width: 400px; margin-right: 10px">
				
				<div style="width: auto" class="g-signin2" data-onsuccess="onSignIn" data-theme="dark"></div>
			  	
			  	
			</div>
			
			
			
		</div>
	
	 
		<!-- Register -->
		<form action="RegisterDispatcher" method="GET" style="display: flex; flex-direction: column; justify-content: center; align-content: flex-end; height: 500px; width: 100%; max-width: 400px; margin-right: 10px">
			<div style="display: flex; flex-direction: column; justify-content: center; align-content: flex-start; height: 500px; width: 100%; max-width: 400px; margin-left: 10px">
				<span style="font-size: 15pt; font-weight: 900; color: black">Register</span> <br>
				<span>Email</span>
				<input type="text" id="regEmail" name="regEmail" style="border: 1px solid Whitesmoke; width: auto; max-width: 850px" required> <br>
				
				<span>Name</span>
				<input type="text" id="regName" name="regName" style="border: 1px solid Whitesmoke; width: auto; max-width: 850px" required> <br>
	
				<span>Password</span>
				<input type="text" id="regPassword" name="regPassword" style="border: 1px solid Whitesmoke" required> <br>
	
				<span>Confirm Password</span>
				<input type="text" id="regConfirm" name="regConfirm" style="border: 1px solid Whitesmoke" required> <br>
				
				<span>
					<input type="checkbox" id="terms" name="terms" required> I have read and agree to all terms and conditions of SalEats.
				</span>
				
				<br>
				
				<button type="submit" style="background-color: red; color: white; height: 30px; border: none; font-size: light">
					<i class="fa-solid fa-user-plus"></i> Create Account
				</button>
			
			</div>
		</form>
	
	
	</div>
	
	<script>
	    function onSignIn(googleUser) {
	    	  var profile = googleUser.getBasicProfile();
	    	  console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
	    	  console.log('Name: ' + profile.getName());
	    	  console.log('Image URL: ' + profile.getImageUrl());
	    	  console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
    		
	    	  
	    	  var auth2 = gapi.auth2.getAuthInstance();
	    	  
	    	  document.cookie = "username=" + profile.getName().replaceAll(" ", "=");
	    	  let x = document.cookie;
	    	  console.log(x);
	    	  
	    	  auth2.disconnect();
	    	  
	    	  window.location.replace("index.jsp");
	    
	    }
  	</script>
	
	
</body>
</html>