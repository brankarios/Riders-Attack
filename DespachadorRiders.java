import java.util.ArrayList;
import java.util.List;

class DespachadorRiders {
    private Riders[] listOfRiders;
    private int totalClients;
    private int arrivedClients = 0;

    public DespachadorRiders(Riders[] riders, int totalClients) {
        this.listOfRiders = riders;
        this.totalClients = totalClients;
    }

    public synchronized Riders getBestRider(String userApp, String userService, int travelTime) {
        List<Riders> availableRiders = new ArrayList<>();
        for (Riders rider : listOfRiders) {
            if (rider.getService().equals(userService) && rider.isAvailable()) {
                availableRiders.add(rider);
            }
        }
        Riders prospectRider = ((User) Thread.currentThread()).getRider();

        if (!availableRiders.isEmpty()) {
            if(prospectRider == null){
                prospectRider = availableRiders.get(0);
            }
            for (int i = 1; i < availableRiders.size(); i++) {
                if (availableRiders.get(i).getArrivalTime() < prospectRider.getArrivalTime()) {
                    prospectRider.setAvailability(true);
                    prospectRider = availableRiders.get(i);
                } else if (availableRiders.get(i).getArrivalTime() == prospectRider.getArrivalTime()) {
                    if (availableRiders.get(i).getApp().equals(userApp)) {
                        prospectRider = availableRiders.get(i);
                    }
                }
            }
            prospectRider.setAvailability(false);
        }

        return prospectRider;
    }

    public synchronized void clientArrived() {
        arrivedClients++;
        if (arrivedClients == totalClients) {
            System.out.println("Todos los clientes han llegado a su destino.");
            System.exit(0);
        }
    }
}
