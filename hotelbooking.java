// hotelbooking
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface HotelBooking extends Remote {
    String bookRoom(String guestName) throws RemoteException;
    String cancelBooking(String guestName) throws RemoteException;
    List<String> getBookings() throws RemoteException;
}

// hotelbookingimpl
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class HotelBookingImpl extends UnicastRemoteObject implements HotelBooking {
    private List<String> bookings;

    protected HotelBookingImpl() throws RemoteException {
        super();
        bookings = new ArrayList<>();
    }

    @Override
    public synchronized String bookRoom(String guestName) throws RemoteException {
        if (bookings.contains(guestName)) {
            return "Booking failed: " + guestName + " already has a room.";
        }
        bookings.add(guestName);
        return "Booking successful for " + guestName;
    }

    @Override
    public synchronized String cancelBooking(String guestName) throws RemoteException {
        if (!bookings.contains(guestName)) {
            return "Cancellation failed: No booking found for " + guestName;
        }
        bookings.remove(guestName);
        return "Booking canceled for " + guestName;
    }

    @Override
    public synchronized List<String> getBookings() throws RemoteException {
        return new ArrayList<>(bookings);
    }
}

// Hotelclient

import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;

public class HotelClient {
    public static void main(String[] args) {
        try {
            HotelBooking hotel = (HotelBooking) Naming.lookup("rmi://localhost/HotelService");
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nHotel Booking System");
                System.out.println("1. Book a Room");
                System.out.println("2. Cancel Booking");
                System.out.println("3. View Bookings");
                System.out.println("4. Exit");
                System.out.print("Enter choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter guest name: ");
                        String name = scanner.nextLine();
                        System.out.println(hotel.bookRoom(name));
                        break;
                    case 2:
                        System.out.print("Enter guest name to cancel: ");
                        String cancelName = scanner.nextLine();
                        System.out.println(hotel.cancelBooking(cancelName));
                        break;
                    case 3:
                        List<String> bookings = hotel.getBookings();
                        System.out.println("Current Bookings: " + bookings);
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// hotel server

import java.rmi.Naming;
// (also make sure you have these imports if needed)
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.net.MalformedURLException;

public class HotelServer {
    public static void main(String[] args) {
        try {
            // LocateRegistry.createRegistry(1099);  // this should stay commented
            HotelBookingImpl hotel = new HotelBookingImpl();
            Naming.rebind("rmi://localhost/HotelService", hotel);
            System.out.println("Hotel Booking Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

