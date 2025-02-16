import java.util.Random;

class User extends Thread {

    private String app;
    private String service;
    private DecrementingTimer travelTime;
    private DespachadorRiders monitor;

    public User(String[] app, String[] service, DespachadorRiders ridersMonitor) {
        int minTravelTime = 1;
        int maxTravelTime = 50;
        this.monitor = ridersMonitor;
        Random randomIntGenerator = new Random();
        this.travelTime = new DecrementingTimer(randomIntGenerator.nextInt(maxTravelTime - minTravelTime + 1) + minTravelTime);
        this.app = app[randomIntGenerator.nextInt(app.length)]; 
        this.service = service[randomIntGenerator.nextInt(service.length)];
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
        return this.travelTime.getTimeLeft();
    }

    public void selectRider() {
        this.monitor.getAvailableRiders(app, service, travelTime);
    }

    public void travel() {
        this.travelTime.start();
        try {
            Thread.sleep(this.travelTime.getTimeLeft() * 10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        monitor.clientArrived(); 
    }
}
