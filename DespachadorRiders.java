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
        while (availableRiders.isEmpty()) {
            try {
                System.out.println("Thread " + Thread.currentThread().threadId() + " waiting.");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

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

        prospectRider.travel(travelTime);
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
        } else {
            System.out.println("No hay riders disponibles en este momento.");
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
