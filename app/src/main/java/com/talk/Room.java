package com.talk;

public class Room
{
	private String roomId;
	/*用來存入sessionID*/
	private String Meber1;
	private String Meber2;
	
	/*建構子*/
	public Room(String roomId){
		this.roomId = roomId;
	};
	
	
	public String getRoomId()
	{
		return roomId;
	}

	public void setRoomId(String roomId)
	{
		this.roomId = roomId;
	}

	public String getMeber1()
	{
		return Meber1;
	}

	public void setMeber1(String meber1)
	{
		Meber1 = meber1;
	}

	public String getMeber2()
	{
		return Meber2;
	}

	public void setMeber2(String meber2)
	{
		Meber2 = meber2;
	}
	
	
	public boolean equals(Object obj) {
    	if (this == obj) return true;                     
    	if(obj != null && getClass() == obj.getClass()) { 
    		if(obj instanceof Room) {
    			Room e = (Room)obj;
                if (roomId.equals(e.roomId)) {  
                    return true;
                }
        }
    	}	    	

    	return false;
    }
	
	public int hashCode(){
 	  return this.roomId.hashCode();  
  	
  }
	
}
