import java.util.ArrayList;
import java.util.Scanner;

class Bus {
    int id;
    String name;
    String source;
    String destination;
    String time;
    int fare;
    boolean[] seats;

    Bus(int id, String name, String source, String destination,
        String time, int fare, int totalSeats) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.time = time;
        this.fare = fare;
        this.seats = new boolean[totalSeats + 1];
    }

    int availableSeats() {
        int count = 0;
        for (int i = 1; i < seats.length; i++) {
            if (!seats[i]) {
                count++;
            }
        }
        return count;
    }

    void display() {
        System.out.println(id + " | " + name + " | " + source + " -> "
            + destination + " | " + time + " | Rs." + fare
            + " | Seats left: " + availableSeats());
    }
}

class Ticket {
    int ticketId;
    int busId;
    String passenger;
    int seatNo;
    int amount;

    Ticket(int ticketId, int busId, String passenger, int seatNo, int amount) {
        this.ticketId = ticketId;
        this.busId = busId;
        this.passenger = passenger;
        this.seatNo = seatNo;
        this.amount = amount;
    }

    void display() {
        System.out.println("Ticket " + ticketId + " | Bus " + busId + " | "
            + passenger + " | Seat " + seatNo + " | Rs." + amount);
    }
}

public class BusTicketBookingSystem {

    static ArrayList<Bus> buses = new ArrayList<>();
    static ArrayList<Ticket> tickets = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static int nextTicketId = 1001;

    // Reads an integer safely; keeps asking until valid input is entered.
    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    static Bus findBus(int id) {
        for (Bus b : buses) {
            if (b.id == id) {
                return b;
            }
        }
        return null;
    }

    static void viewBuses() {
        System.out.println("ID | Bus | Route | Time | Fare | Availability");
        for (Bus b : buses) {
            b.display();
        }
    }

    static void searchBus() {
        String src = readString("Enter Source: ");
        String dest = readString("Enter Destination: ");

        boolean found = false;
        for (Bus b : buses) {
            if (b.source.equalsIgnoreCase(src)
                    && b.destination.equalsIgnoreCase(dest)) {
                b.display();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No buses found for this route.");
        }
    }

    static void bookTicket() {
        int id = readInt("Enter Bus ID: ");
        Bus b = findBus(id);
        if (b == null) {
            System.out.println("Bus not found.");
            return;
        }

        int seat = readInt("Enter Seat Number (1-" + (b.seats.length - 1) + "): ");
        if (seat < 1 || seat >= b.seats.length) {
            System.out.println("Invalid seat number.");
            return;
        }
        if (b.seats[seat]) {
            System.out.println("Seat is already booked.");
            return;
        }

        String name = readString("Enter Passenger Name: ");
        if (name.isEmpty()) {
            System.out.println("Passenger name cannot be empty.");
            return;
        }

        b.seats[seat] = true;
        Ticket t = new Ticket(nextTicketId++, id, name, seat, b.fare);
        tickets.add(t);
        System.out.println("Ticket booked successfully. Ticket ID: " + t.ticketId);
    }

    static void cancelTicket() {
        int tid = readInt("Enter Ticket ID to cancel: ");

        for (Ticket t : tickets) {
            if (t.ticketId == tid) {
                findBus(t.busId).seats[t.seatNo] = false;
                tickets.remove(t);
                System.out.println("Ticket cancelled. Refund: Rs." + t.amount);
                return;
            }
        }
        System.out.println("Ticket not found.");
    }

    static void viewTickets() {
        if (tickets.isEmpty()) {
            System.out.println("No tickets booked.");
            return;
        }
        for (Ticket t : tickets) {
            t.display();
        }
    }

    public static void main(String[] args) {
        buses.add(new Bus(1, "SRS Express", "Chennai", "Bangalore", "22:00", 650, 10));
        buses.add(new Bus(2, "KPN Travels", "Chennai", "Madurai", "21:30", 800, 10));
        buses.add(new Bus(3, "Orange Tours", "Coimbatore", "Chennai", "20:45", 750, 10));

        int choice;
        do {
            System.out.println("\n--- BUS TICKET BOOKING SYSTEM ---");
            System.out.println("1. View Buses");
            System.out.println("2. Search Bus");
            System.out.println("3. Book Ticket");
            System.out.println("4. Cancel Ticket");
            System.out.println("5. View Booked Tickets");
            System.out.println("6. Exit");

            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    viewBuses();
                    break;
                case 2:
                    searchBus();
                    break;
                case 3:
                    bookTicket();
                    break;
                case 4:
                    cancelTicket();
                    break;
                case 5:
                    viewTickets();
                    break;
                case 6:
                    System.out.println("Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 6);

        sc.close();
    }
}
