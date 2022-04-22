package mdjun_CSCI201_PA2.Util;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Coordinates {

	@SerializedName("latitude")
	@Expose
	private Float latitude;
	
	@SerializedName("longitude")
	@Expose
	private Float longitude;
	
	
	
	public Float getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}
	
	public Float getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

}