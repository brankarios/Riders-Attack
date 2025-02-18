import java.util.Random;

class User extends Thread {

    private String app;
    private String service;
    private int travelTime;
    private Random randomIntGenerator;
    private DespachadorRiders monitor;
    private Riders assignedRider;

    public User(String[] app, String[] service, DespachadorRiders ridersMonitor) {
        int minTravelTime = 1;
        int maxTravelTime = 50;
        this.monitor = ridersMonitor;
        this.randomIntGenerator = new Random();
        this.travelTime = randomIntGenerator.nextInt(maxTravelTime - minTravelTime + 1) + minTravelTime;
        this.app = app[randomIntGenerator.nextInt(app.length)]; 
        this.service = service[randomIntGenerator.nextInt(service.length)];
    }

    @Override
    public void run() {
        System.out.println("Usuario #" + currentThread().threadId() + " solicitando servicio de (" + this.service + "-" + this.app + ").");
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
            System.out.println("Al usuario #" + currentThread().threadId() + " se le asigno el rider #" + this.assignedRider.getID() + " (" + this.assignedRider.getService() + "-" + this.assignedRider.getApp() + ").");
            while(riderArrivalTime > 0 && this.assignedRider != null){
                riderArrivalTime -= 1;
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    currentThread().interrupt();
                }
                newRider = this.monitor.getBestRider(this.app, this.service, this.travelTime);
                if(this.assignedRider != newRider && newRider != null){
                    System.out.println("Al usuario #" + currentThread().threadId() + " se le cambio el rider #" + this.assignedRider.getID() + " por el rider #" + newRider.getID() + ".");
                    this.assignedRider = newRider;
                    riderArrivalTime = this.assignedRider.getArrivalTime();
                }
            }
            this.travel();
        }

        /*if(this.assignedRider != null){
            while(this.assignedRider.getArrivalTime() > 0 && this.assignedRider != null){
                int riderArrivalTime = this.assignedRider.getArrivalTime();
                while (riderArrivalTime > 0){
                    riderArrivalTime -= 1;
                    newRider = this.monitor.getBestRider(this.app, this.service, this.travelTime);
                    if(this.assignedRider != newRider && newRider != null){
                        previousRiderID = this.assignedRider.getID();
                        this.assignedRider = newRider;
                        riderArrivalTime = this.assignedRider.getArrivalTime();
                    }
                }
                this.travel();
            }
        }*/
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
                sleep(this.travelTime * 10);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
        }

        System.out.println("El rider #" + this.assignedRider.getID() + " llevo al usuario #" + currentThread().threadId() + ".");
        this.assignedRider.setAvailability(true);
        monitor.clientArrived(); 
    }
}
