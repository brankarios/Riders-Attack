import java.util.Random;

class User extends Thread {

    private String app;
    private String service;
    private int travelTime;
    private Random randomIntGenerator;
    private DespachadorRiders monitor;
    private Riders assignRider;
    private boolean traveling;

    public User(String[] app, String[] service, DespachadorRiders ridersMonitor) {
        int minTravelTime = 1;
        int maxTravelTime = 50;
        this.monitor = ridersMonitor;
        this.randomIntGenerator = new Random();
        this.travelTime = randomIntGenerator.nextInt(maxTravelTime - minTravelTime + 1) + minTravelTime;
        this.app = app[randomIntGenerator.nextInt(app.length)]; 
        this.service = service[randomIntGenerator.nextInt(service.length)];
        this.traveling = false;
    }

    @Override
    public void run() {
        selectRider();
    }

    public String getApp() {
        return this.app;
    }

    public String getService() {
        return this.service;
    }

    public int getTravelTime() {
        return this.travelTime;
    }

    public boolean isTraveling(){
        return this.traveling;
    }

    public void selectRider(){
        if(this.travelTime > 0){
            this.monitor.getAvailableRiders(app, service, travelTime);
        }
    }

    public Riders getRider(){
        return assignRider;
    }

    public void assignRider(Riders rider){
        this.assignRider = rider;
    }

    public void travel() {
        this.traveling = true;
        while(travelTime > 0){
            this.travelTime -= 1;
            try {
                sleep(this.travelTime * 10);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
        }

        System.out.println("El cliente #" + currentThread().threadId() + " ha llegado a su destino.");
        monitor.clientArrived(); 
    }
}
