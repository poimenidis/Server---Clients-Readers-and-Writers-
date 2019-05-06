import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AirportList {
	/* AirportList is a list of airportEntries. */
	private ArrayList<AirportEntry> airportList;
	/* ReadWriteLock is implemented by ReentrantReadWriteLock Class in java.util.concurrent.locks package.Multiple 
	 * Threads can acquire multiple read Locks, but only a single Thread can acquire mutually-exclusive write Lock .
	 * Other threads requesting readLocks have to wait till the write Lock is released. 
	 * A thread is allowed to degrade from write lock to read lock but not vice-versa.*/
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();  
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();
	
	public AirportList() {
		airportList = new ArrayList<AirportEntry>();
	}
	
	/*Here is where AirportList executes client's requests via server.*/
	public Reply executeRequest(Request request) {
		/* First it gets the opcode, the ip , the condition and the time from the request.
		 * Opcode is client's command.*/
		String opcode = request.getOpcode();
		String ip = request.getIp();
		String condition = request.getCondition();
		String time = request.getTime();
		Reply reply = new Reply();

		/* Depending on opcode it executes the relevant command.
		 * case "READ": Search by IP. Check every AirportEntry item in list and if there you find the same ip return ROK <IP> <condition> <time> .
		 * case "WRITE": Update or Insert an entry. User types new IP. Program finds that entry 
		 *         and if exists it replace it with the new one. Else, if it doesn't exist, the program inserts the new entry.
		 * case "DELETE": DELETE an entry. User types IP. Program search that entry 
		 *         and if exists it deletes it.*/
		switch (opcode) {
		case "READ":
			if(ip!=null) {
				/*If someone writes then wait. However, it allows multiple readers to read.*/
				readLock.lock();
		        try
		        {
		        	boolean found = false;
					for (AirportEntry entry : airportList) {
						if (entry.getIp().equals(ip)) {
							
							/*A delay for reading of 1000ms*/
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							found = true;
							reply.setSomethingToReturn("ROK "+entry.getIp()+ " "+ entry.getCondition()+ " "+ entry.getTime());
							break;
						}
					}
					if(!found)
						reply.setSomethingToReturn("RERR");
		        }
		        finally
		        {
		            readLock.unlock();
		        }
			}
			else 
				reply.setSomethingToReturn("RERR");
			break;
		case "WRITE":
			if(ip!=null&&condition!=null&&time!=null) {
				/*If someone writes too then wait. Also no readers are allowed to read before it finishes*/
				writeLock.lock();
		        try
		        {
		        	int position = -1;
					int count = 0;
					AirportEntry newEntry = new AirportEntry(ip, condition, time);
					for (AirportEntry entry : airportList) {
						if (entry.getIp().equals(ip) || entry.getCondition().equals(condition)) {
							position = count;
						}
						count++;
					}
					if(position != -1) {
						airportList.set(position,newEntry);
					}
					else {
						airportList.add(newEntry);
					}
					
					/*A delay for writing of 10000ms*/
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					reply.setSomethingToReturn("WOK");
		        }
		        finally
		        {
		            writeLock.unlock();
		        }
			}
			else
				reply.setSomethingToReturn("WERR");
			
			break;
		case "DELETE":
			if(ip!=null) {
				/*If someone writes too then wait. Also no readers are allowed to read before it finishes*/
				writeLock.lock();
		        try
		        {
		        	int position = -1;
					int count = 0;
					AirportEntry newEntry = new AirportEntry(ip, condition, time);
					for (AirportEntry entry : airportList) {
						if (entry.getIp().equals(ip) || entry.getCondition().equals(condition)) {
							position = count;
						}
						count++;
					}
					if(position != -1) {
						airportList.remove(position);
					}
					else {
						reply.setSomethingToReturn("DERR");
					}
					
					/*A delay for deleting of 10000ms*/
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					reply.setSomethingToReturn("DOK");
		        }
		        finally
		        {
		        	writeLock.unlock();
		        }
			}
			else 
				reply.setSomethingToReturn("DERR");
			break;
		}
		return reply;
	}
}

/*Here is the AirportEntry which has an ip, a condition and time*/
class AirportEntry {
	private String ip;
	private String condition;
	private String time;
	
	public AirportEntry(String ip, String condition, String time) {
		super();
		this.ip = ip;
		this.condition = condition;
		this.time = time;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getTime() {
		return time;
	}
	
}
