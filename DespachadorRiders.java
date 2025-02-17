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

    public synchronized void assignRider(String userApp, List<Riders> availableRiders, int travelTime) {

        Riders prospectRider = availableRiders.get(0);
        for (int i = 1; i < availableRiders.size(); i++) {
            if (availableRiders.get(i).getArrivalTime() <= prospectRider.getArrivalTime()) {
                prospectRider = availableRiders.get(i);
            } else if (availableRiders.get(i).getArrivalTime() == prospectRider.getArrivalTime()) {
                if (availableRiders.get(i).getApp().equals(userApp)) {
                    prospectRider = availableRiders.get(i);
                }
            }
        }

        if(prospectRider != null){
            while(prospectRider.getArrivalTime() > 0){
                ((User) Thread.currentThread()).assignRider(prospectRider);
                prospectRider.decrementTimeArrival();
                if (Thread.currentThread() instanceof User) {
                    ((User) Thread.currentThread()).selectRider();
                    if(prospectRider.getID() != ((User) Thread.currentThread()).getRider().getID()){
                        System.out.println("El rider del usuario #" + Thread.currentThread().threadId() + " cambio del #" + prospectRider.getID() + " al #" + ((User) Thread.currentThread()).getRider().getID() + ".");
                    } 
                }
            }

            if (Thread.currentThread() instanceof User) {
                if(!((User) Thread.currentThread()).isTraveling()){
                    ((User) Thread.currentThread()).travel();
                }
            }
            prospectRider.travel(travelTime);
        }

        notifyAll();
    }

    public synchronized void getAvailableRiders(String userApp, String userService, int travelTime) {
        List<Riders> availableRiders = new ArrayList<>();
        for (Riders rider : listOfRiders) {
            if (rider.getService().equals(userService) && rider.isAvailable()) {
                availableRiders.add(rider);
            }
        }

        if (!availableRiders.isEmpty()) {
            assignRider(userApp, availableRiders, travelTime);
        }
    }

    public synchronized void clientArrived() {
        arrivedClients++;
        System.out.println("Clientes que han llegado: " + arrivedClients + "/" + totalClients);
        if (arrivedClients == totalClients) {
            System.out.println("Todos los clientes han llegado a su destino. Terminando el programa...");
            System.exit(0);
        }
    }
}
