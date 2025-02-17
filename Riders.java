import java.util.Random;

public class Riders{
    
    private String app;
    private String service;
    private int arrivalTime;
    private boolean isAvailable;
    private Random randomIntGenerator = new Random();
    private int minArrivalTime = 1;
    private int maxArrivalTime = 30;

    public Riders(String[] app, String[] service){
        
        this.arrivalTime = randomIntGenerator.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
        
        this.app = app[randomIntGenerator.nextInt(3)]; 
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

    public boolean isAvailable(){
        return this.isAvailable;
    }

    public void travel(int travelTime) {
        int riderTravelTime = travelTime;
        this.isAvailable = false;
        System.out.println("Rider " + this.app + " - " + this.service + " esta viajando.");
    
        try {
            Thread.sleep(this.arrivalTime * 10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    
        System.out.println("Rider " + this.app + " ha llegado al cliente.");
        
        while(riderTravelTime > 0){
            riderTravelTime -= 1;
            
            try {
                Thread.sleep(riderTravelTime * 10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    
        System.out.println("Cliente llevado exitosamente por " + this.app + " - " + this.service);
        this.isAvailable = true;
        this.arrivalTime = randomIntGenerator.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
    
        synchronized (this) {
            System.out.println("Rider " + this.app + " - " + this.service + " esta ahora disponible.");
            notifyAll();
        }
    }
}

