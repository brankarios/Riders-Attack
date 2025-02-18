import java.nio.file.*;
import java.io.*;

class RidersAttack {
    public static void main(String[] args) {

        String readLine = "null";
        String[] lecture;
        int numOfUsers = 0, numOfBipbipRiders = 0, numOfRideryRiders = 0, numOfYummyRiders = 0;
        try{
            Path filePath = Paths.get(args[0]);
            InputStream input = new BufferedInputStream(Files.newInputStream(filePath));
            BufferedReader reader = new  BufferedReader(new InputStreamReader(input));
            
            readLine = reader.readLine();
            numOfUsers = Integer.parseInt(readLine);
            readLine = reader.readLine();
            lecture = readLine.split(", ");
            numOfBipbipRiders = Integer.parseInt(lecture[1]);
            readLine = reader.readLine();
            numOfRideryRiders = Integer.parseInt(lecture[1]);
            readLine = reader.readLine();
            numOfYummyRiders = Integer.parseInt(lecture[1]);

        }catch(Exception e){
            System.out.println("ERROR: " + e);
        }

        String[] app = {"bipbip", "ridery", "yummy"};
        String[] service = {"motorcycle", "car"};

        int numOfRiders = numOfBipbipRiders + numOfRideryRiders + numOfYummyRiders;
        Riders[] riders = new Riders[numOfRiders];
        for (int i = 0; i < numOfRiders; i++) {
            if(i < numOfBipbipRiders){
                riders[i] = new Riders("bipbip", service, i);
            }
            else if(i < numOfBipbipRiders + numOfRideryRiders){
                riders[i] = new Riders("ridery", service, i);
            }
            else{
                riders[i] = new Riders("yummy", service, i);
            }
        }

        DespachadorRiders monitor = new DespachadorRiders(riders, numOfUsers);

        User[] users = new User[numOfUsers];
        for (int i = 0; i < numOfUsers; i++) {
            users[i] = new User(i, app, service, monitor);
            users[i].start();
        }

        for (int i = 0; i < numOfUsers; i++) {
            users[i].interrupt();
        }
    }
}
