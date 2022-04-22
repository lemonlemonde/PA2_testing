package mdjun_CSCI201_PA2;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mdjun_CSCI201_PA2.Util.RestaurantDataParser;

import java.io.IOException;
import java.io.Serial;

/**
 * Servlet implementation class SearchDispatcher
 */
@WebServlet("/SearchDispatcher")
public class SearchDispatcher extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public SearchDispatcher() {
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = getServletContext();

    }

    
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO
    	String error = "";
    	
    	String searchBox = request.getParameter("searchBox");
    	System.out.println("searchBox text:" + searchBox);
    	
    	String searchFilter = request.getParameter("searchFilter");
    	System.out.println("searchFilter:" + searchFilter);
    	
    	String searchOrder = request.getParameter("searchOrder");
    	System.out.println("searchOrder:" + searchOrder);
    	
    	
    	if (error == "") {
    		// all successful
			request.getRequestDispatcher("search.jsp").forward(request, response);
    	} else {
    		// not all successful
    		request.setAttribute("error", error);
			request.getRequestDispatcher("search.jsp").include(request, response);
    	}
    	
    	
    	
    }
    
    
    
    

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}