package lsi.multinet;

/*
 *  
 * @author      Leandro Soares Indrusiak
 * @version 1.0 (York, 29/07/2020) 
 * 
 * Simple representation of a network interface. 
 * 
 * 
 */


public class Network {
	
	double bandwidth;
	int privacy;
	boolean availability=true;
	String name="";
	
	
	public Network(double bandwidth, int privacyGuarantee){
		
		this.bandwidth = bandwidth;
		this.privacy = privacyGuarantee;
		
	}


	/**
	 * @return the bandwidth
	 */
	public double getBandwidth() {
		return bandwidth;
	}


	/**
	 * @param bandwidth the bandwidth to set
	 */
	public void setBandwidth(double bandwidth) {
		this.bandwidth = bandwidth;
	}


	/**
	 * @return the privacy
	 */
	public int getPrivacyGuaranteeLevel() {
		return privacy;
	}


	/**
	 * @param privacy the privacy to set
	 */
	public void setPrivacyGuaranteeLevel(int privacy) {
		this.privacy = privacy;
	}


	/**
	 * @return the availability
	 */
	public boolean isAvailable() {
		return availability;
	}


	/**
	 * @param availability the availability to set
	 */
	public void setAvailability(boolean availability) {
		this.availability = availability;
	}
	
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		
		return name;
	}
	

}
