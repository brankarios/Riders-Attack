import java.util.Random;

class User extends Thread {

    private int id;
    private String app;
    private String service;
    private int travelTime;
    private Random randomIntGenerator;
    private DespachadorRiders monitor;
    private Riders assignedRider;

    public User(int id, String[] app, String[] service, DespachadorRiders ridersMonitor) {
        int minTravelTime = 1;
        int maxTravelTime = 50;
        this.id = id;
        this.monitor = ridersMonitor;
        this.randomIntGenerator = new Random();
        this.travelTime = randomIntGenerator.nextInt(maxTravelTime - minTravelTime + 1) + minTravelTime;
        this.app = app[randomIntGenerator.nextInt(app.length)]; 
        this.service = service[randomIntGenerator.nextInt(service.length)];
    }

    @Override
    public void run() {
        System.out.println("Usuario #" + this.id + " solicitando servicio de (" + this.service + "-" + this.app + ").");
        while(this.assignedRider == null){
            selectRider();
        }
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

    public void selectRider(){
        Riders newRider;
        this.assignedRider = this.monitor.getBestRider(this.app, this.service, this.travelTime);

        if(this.assignedRider != null){
            int riderArrivalTime = this.assignedRider.getArrivalTime();
            System.out.println("Al usuario #" + this.id + " se le asigno el rider #" + this.assignedRider.getID() + " (" + this.assignedRider.getService() + "-" + this.assignedRider.getApp() + ").");
            while(riderArrivalTime > 0 && this.assignedRider != null){
                riderArrivalTime -= 1;
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    currentThread().interrupt();
                }
                newRider = this.monitor.getBestRider(this.app, this.service, this.travelTime);
                if(this.assignedRider != newRider && newRider != null){
                    System.out.println("Al usuario #" + this.id + " se le cambio el rider #" + this.assignedRider.getID() + " por el rider #" + newRider.getID() + ".");
                    this.assignedRider = newRider;
                    riderArrivalTime = this.assignedRider.getArrivalTime();
                }
            }
            this.travel();
        }
    }

    public Riders getRider(){
        return assignedRider;
    }

    public void assignRider(Riders rider){
        this.assignedRider = rider;
    }

    public void travel() {
        while(travelTime > 0){
            this.travelTime -= 1;
            try {
                sleep(this.travelTime * 1000);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
        }

        System.out.println("El rider #" + this.assignedRider.getID() + " llevo al usuario #" + this.id + ".");
        this.assignedRider.setAvailability(true);
        monitor.clientArrived(); 
    }
}
