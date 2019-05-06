import java.io.Serializable;

/* implement the Serializable interface because I need to store a copy of the object, 
 * send it to another process which runs on the same system or over the network.
 * It makes storing and sending objects easy. However it has nothing to do with security.*/

public class Reply implements Serializable {
	private String somethingToReturn;
	
	public Reply() {
		somethingToReturn = null;
	}

	public String getSomethingToReturn() {
		return somethingToReturn;
	}

	public void setSomethingToReturn(String somethingToReturn) {
		this.somethingToReturn = somethingToReturn;
	}
	
	
}
