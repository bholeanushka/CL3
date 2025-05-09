// client 

import java.rmi.Naming;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            ConcatInterface stub = (ConcatInterface) Naming.lookup("rmi://localhost/ConcatService");

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter first string: ");
            String str1 = sc.nextLine();
            System.out.print("Enter second string: ");
            String str2 = sc.nextLine();

            String result = stub.concatenate(str1, str2);
            System.out.println("Concatenated result: " + result);
        } catch (Exception e) {
            System.out.println("Client error: " + e);
        }
    }
}




//how to run in windows
//1- javac *.java (compile all files)
//2-rmiregistry(Start the RMI Registry in terminal 1)
// 3- java Server (Run the Server (in terminal 2)
//4- java Client  (Run the Client (in terminal 3)



/*for ubuntu same as windows 
 If you get a command not found, install the JDK:
sudo apt update
sudo apt install default-jdk
 */


// concatimpl
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class ConcatImpl extends UnicastRemoteObject implements ConcatInterface {
    public ConcatImpl() throws RemoteException {
        super();
    }

    public String concatenate(String s1, String s2) throws RemoteException {
        return s1 + s2;
    }
}

// concatinterface
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConcatInterface extends Remote {
    String concatenate(String s1, String s2) throws RemoteException;
}

// server

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099); // Start RMI registry on port 1099
            ConcatImpl obj = new ConcatImpl();
            Naming.rebind("ConcatService", obj);
            System.out.println("Server is ready.");
        } catch (Exception e) {
            System.out.println("Server error: " + e);
        }
    }
}
