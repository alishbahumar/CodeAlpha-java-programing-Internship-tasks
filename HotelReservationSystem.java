import java.io.*;
import java.util.*;

public class HotelReservationSystem {

    // ---------- Room class ----------
    static class Room {
        int number;
        String category;   // Standard, Deluxe, Suite
        double pricePerNight;
     boolean booked;

        Room(int number, String category, double pricePerNight) {
            this.number = number;
            this.category = category;
            this.pricePerNight = pricePerNight;
            this.booked = false;
        }
    }

    // ---------- Reservation class ----------
    static class Reservation {
        int id;
        String guestName;
        int roomNumber;
        int nights;
        double totalAmount;
        boolean paymentDone;

        Reservation(int id, String guestName, int roomNumber, int nights, double totalAmount, boolean paymentDone) {
            this.id = id;
            this.guestName = guestName;
            this.roomNumber = roomNumber;
            this.nights = nights;
            this.totalAmount = totalAmount;
            this.paymentDone = paymentDone;
        }

        @Override
        public String toString() {
            return "Reservation #" + id + " | Guest: " + guestName + " | Room: " + roomNumber
                    + " | Nights: " + nights + " | Total: " + String.format("%.2f", totalAmount)
                    + " | Paid: " + (paymentDone ? "Yes" : "No");
        }
    }

    // ---------- Hotel class ----------
    static class Hotel {
        List<Room> rooms = new ArrayList<>();
        List<Reservation> reservations = new ArrayList<>();
        int nextReservationId = 1;

        Hotel() {
            // Pre-populate rooms: 101-105 Standard, 201-203 Deluxe, 301-302 Suite
            for (int i = 101; i <= 105; i++) rooms.add(new Room(i, "Standard", 50.0));
            for (int i = 201; i <= 203; i++) rooms.add(new Room(i, "Deluxe", 90.0));
            for (int i = 301; i <= 302; i++) rooms.add(new Room(i, "Suite", 150.0));
        }

        List<Room> searchByCategory(String category) {
            List<Room> result = new ArrayList<>();
            for (Room r : rooms) {
                if (r.category.equalsIgnoreCase(category) && !r.booked) {
                    result.add(r);
                }
            }
            return result;
        }

        Room findRoom(int number) {
            for (Room r : rooms) if (r.number == number) return r;
            return null;
        }

        Reservation bookRoom(String guestName, int roomNumber, int nights) {
            Room room = findRoom(roomNumber);
            if (room == null || room.booked) return null;

            double total = room.pricePerNight * nights;
            Reservation res = new Reservation(nextReservationId++, guestName, roomNumber, nights, total, false);
            room.booked = true;
            reservations.add(res);
            return res;
        }

        boolean cancelReservation(int reservationId) {
            Reservation toRemove = null;
            for (Reservation r : reservations) {
                if (r.id == reservationId) {
                    toRemove = r;
                    break;
                }
            }
            if (toRemove == null) return false;

            Room room = findRoom(toRemove.roomNumber);
            if (room != null) room.booked = false;
            reservations.remove(toRemove);
            return true;
        }

        boolean makePayment(int reservationId) {
            for (Reservation r : reservations) {
                if (r.id == reservationId) {
                    r.paymentDone = true;
                    return true;
                }
            }
            return false;
        }
    }

    static Scanner sc = new Scanner(System.in);
    static final String SAVE_FILE = "reservations.txt";

    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        loadReservations(hotel);

        int choice;
        do {
            System.out.println("\n===== HOTEL RESERVATION SYSTEM =====");
            System.out.println("1. Search Rooms by Category");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel a Reservation");
            System.out.println("4. View All Reservations");
            System.out.println("5. Make Payment for a Reservation");
            System.out.println("6. View All Rooms (availability)");
            System.out.println("7. Save & Exit");
            choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> searchRooms(hotel);
                case 2 -> bookRoomFlow(hotel);
                case 3 -> cancelFlow(hotel);
                case 4 -> viewReservations(hotel);
                case 5 -> paymentFlow(hotel);
                case 6 -> viewAllRooms(hotel);
                case 7 -> {
                    saveReservations(hotel);
                    System.out.println("Data saved. Goodbye!");
                }
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 7);
        sc.close();
    }

    static void searchRooms(Hotel hotel) {
        System.out.print("Enter category (Standard/Deluxe/Suite): ");
        String category = sc.nextLine();
        List<Room> available = hotel.searchByCategory(category);
        if (available.isEmpty()) {
            System.out.println("No available rooms in this category.");
            return;
        }
        System.out.println("\nAvailable Rooms:");
        System.out.printf("%-10s %-12s %-10s%n", "Room No", "Category", "Price/Night");
        for (Room r : available) {
            System.out.printf("%-10d %-12s %-10.2f%n", r.number, r.category, r.pricePerNight);
        }
    }

    static void bookRoomFlow(Hotel hotel) {
        System.out.print("Enter guest name: ");
        String name = sc.nextLine();
        int roomNumber = readInt("Enter room number to book: ");
        Room room = hotel.findRoom(roomNumber);
        if (room == null) {
            System.out.println("Room does not exist.");
            return;
        }
        if (room.booked) {
            System.out.println("Room is already booked.");
            return;
        }
        int nights = readInt("Enter number of nights: ");
        Reservation res = hotel.bookRoom(name, roomNumber, nights);
        if (res != null) {
            System.out.println("Booking successful!");
            System.out.println(res);
        } else {
            System.out.println("Booking failed.");
        }
    }

    static void cancelFlow(Hotel hotel) {
        viewReservations(hotel);
        int id = readInt("Enter reservation ID to cancel: ");
        if (hotel.cancelReservation(id)) {
            System.out.println("Reservation #" + id + " cancelled.");
        } else {
            System.out.println("Reservation not found.");
        }
    }

    static void viewReservations(Hotel hotel) {
        System.out.println("\n---------- ALL RESERVATIONS ----------");
        if (hotel.reservations.isEmpty()) {
            System.out.println("No reservations yet.");
            return;
        }
        for (Reservation r : hotel.reservations) {
            System.out.println(r);
        }
    }

    static void paymentFlow(Hotel hotel) {
        viewReservations(hotel);
        int id = readInt("Enter reservation ID to pay for: ");
        if (hotel.makePayment(id)) {
            System.out.println("Payment simulated successfully for Reservation #" + id + ".");
        } else {
            System.out.println("Reservation not found.");
        }
    }

    static void viewAllRooms(Hotel hotel) {
        System.out.println("\n---------- ALL ROOMS ----------");
        System.out.printf("%-10s %-12s %-12s %-10s%n", "Room No", "Category", "Price/Night", "Status");
        for (Room r : hotel.rooms) {
            System.out.printf("%-10d %-12s %-12.2f %-10s%n",
                    r.number, r.category, r.pricePerNight, r.booked ? "Booked" : "Available");
        }
    }

    // ---------- File I/O: Save/Load Reservations ----------
    static void saveReservations(Hotel hotel) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(SAVE_FILE))) {
            for (Reservation r : hotel.reservations) {
                pw.println(r.id + "|" + r.guestName + "|" + r.roomNumber + "|" + r.nights
                        + "|" + r.totalAmount + "|" + r.paymentDone);
            }
        } catch (IOException e) {
            System.out.println("Error saving reservations: " + e.getMessage());
        }
    }

    static void loadReservations(Hotel hotel) {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int maxId = 0;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                int id = Integer.parseInt(p[0]);
                String guestName = p[1];
                int roomNumber = Integer.parseInt(p[2]);
                int nights = Integer.parseInt(p[3]);
                double total = Double.parseDouble(p[4]);
                boolean paid = Boolean.parseBoolean(p[5]);

                Reservation res = new Reservation(id, guestName, roomNumber, nights, total, paid);
                hotel.reservations.add(res);

                Room room = hotel.findRoom(roomNumber);
                if (room != null) room.booked = true;

                if (id > maxId) maxId = id;
            }
            hotel.nextReservationId = maxId + 1;
            System.out.println("Loaded saved reservations from " + SAVE_FILE);
        } catch (IOException e) {
            System.out.println("Error loading reservations: " + e.getMessage());
        }
    }

    static int readInt(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            sc.next();
        }
        int val = sc.nextInt();
        sc.nextLine();
        return val;
    }
}