// Wrapper clas for "Business.java" which is different!
// this is an array of businesses!!!

package mdjun_CSCI201_PA2.Util;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Generated("jsonschema2pojo")
public class Businesses { 
	
	@SerializedName("businesses")
	@Expose
	private List<Business> businesses = null;
	
	

	public List<Business> getBusinesses() {
		return businesses;
	}

	public void setBusinesses(List<Business> businesses) {
		this.businesses = businesses;
	}
}



