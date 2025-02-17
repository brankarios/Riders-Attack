class RidersAttack {
    public static void main(String[] args) {
        int numOfUsers = 10;
        int numOfRiders = 5;
        String[] app = {"bipbip", "ridery", "yummy"};
        String[] service = {"motorcycle", "car"};

        Riders[] riders = new Riders[numOfRiders];
        for (int i = 0; i < numOfRiders; i++) {
            riders[i] = new Riders(app, service, i);
        }

        DespachadorRiders monitor = new DespachadorRiders(riders, numOfUsers);

        User[] users = new User[numOfUsers];
        for (int i = 0; i < numOfUsers; i++) {
            users[i] = new User(app, service, monitor);
            users[i].start();
        }

        for (int i = 0; i < numOfUsers; i++) {
            users[i].interrupt();
        }
    }
}
