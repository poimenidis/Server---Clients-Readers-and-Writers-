import java.io.Serializable;

/* implement the Serializable interface because I need to store a copy of the object, 
 * send it to another process which runs on the same system or over the network.
 * It makes storing and sending objects easy. However it has nothing to do with security.*/

public class Request implements Serializable{
	private String opcode;
	private String ip;
	private String condition;
	private String time;
	
	public Request(String opcode, String ip, String name, String time) {
		super();
		this.opcode = opcode;
		this.ip = ip;
		this.condition = name;
		this.time = time;
	}

	public String getOpcode() {
		return opcode;
	}

	public String getIp() {
		return ip;
	}
	
	public String getCondition() {
		return condition;
	}
	
	public String getTime() {
		return time;
	}
	
}
