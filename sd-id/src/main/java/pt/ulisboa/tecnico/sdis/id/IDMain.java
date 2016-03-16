package pt.ulisboa.tecnico.sdis.id;

import javax.xml.ws.Endpoint;

import pt.ulisboa.tecnico.sdis.id.uddi.UDDINaming;


public class IDMain {

    public static void main(String[] args) {
        // Check arguments
        if (args.length < 3) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s url%n", IDMain.class.getName());
            return;
        }
        
        //begin of populate
        IDUsers.getInstance().addUser(new User("root", "rootroot", "root@tecnico.pt"));
        IDUsers.getInstance().addUser(new User("alice", "Aaa1", "alice@tecnico.pt"));
        IDUsers.getInstance().addUser(new User("bruno", "Bbb2", "bruno@tecnico.pt"));
        IDUsers.getInstance().addUser(new User("carla", "Ccc3", "carla@tecnico.pt"));
        IDUsers.getInstance().addUser(new User("duarte", "Ddd4", "duarte@tecnico.pt"));
        IDUsers.getInstance().addUser(new User("eduardo", "Eee5", "eduardo@tecnico.pt"));
        //end of populate

        String uddiURL = args[0];
        String name = args[1];
        String url = args[2];

        Endpoint endpoint = null;
        UDDINaming uddiNaming = null;
        try {
            endpoint = Endpoint.create(new SDIdImpl());

            // publish endpoint
            System.out.printf("Starting %s%n", url);
            endpoint.publish(url);
            
            // publish to UDDI
            System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
            uddiNaming = new UDDINaming(uddiURL);
            uddiNaming.rebind(name, url);

            // wait
            System.out.println("Awaiting connections");
            System.out.println("Press enter to shutdown");
            System.in.read();

        } catch(Exception e) {
            System.out.printf("Caught exception: %s%n", e);
            e.printStackTrace();

        } 

        finally {
            try {
                if (endpoint != null) {
                    // stop endpoint
                    endpoint.stop();
                    System.out.printf("Stopped %s%n", url);
                }
            } catch(Exception e) {
                System.out.printf("Caught exception when stopping: %s%n", e);
            }
            try {
                if (uddiNaming != null) {
                    // delete from UDDI
                    uddiNaming.unbind(name);
                    System.out.printf("Deleted '%s' from UDDI%n", name);
                }
            } catch(Exception e) {
                System.out.printf("Caught exception when deleting: %s%n", e);
            }
        }
}
}