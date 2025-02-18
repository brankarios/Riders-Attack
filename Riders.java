import java.util.Random;

public class Riders{
    
    private int id;
    private String app;
    private String service;
    private int arrivalTime;
    private boolean isAvailable;
    private Random randomIntGenerator = new Random();
    private int minArrivalTime = 1;
    private int maxArrivalTime = 30;

    public Riders(String app, String[] service, int id){
        
        this.arrivalTime = randomIntGenerator.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
        this.id = id;
        this.app = app; 
        this.service = service[randomIntGenerator.nextInt(2)];
        
        this.isAvailable = true;

    }

    public String getApp(){
        
        return this.app;
    }

    public String getService(){
        
        return this.service;
        
    }

    public int getArrivalTime(){

        return this.arrivalTime;
    
    }

    public int getID(){
        return this.id;
    }

    public boolean isAvailable(){
        return this.isAvailable;
    }

    public void setAvailability(boolean availability){
        this.isAvailable = availability;
    }
}

