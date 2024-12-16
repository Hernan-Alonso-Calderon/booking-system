import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Main {
    // Data structures
    static List<Map<String, Object>> accommodations = new ArrayList<>();
    static List<Map<String, Object>> roomTypes = new ArrayList<>();
    static List<Map<String, String>> users = new ArrayList<>();
    static List<Map<String, Object>> bookings = new ArrayList<>();
    static List<Map<String, Object>> selectedAccommodations = new ArrayList<>();
    static Map<String, Object> newBooking = new HashMap<>();
    static boolean canReserve = false;
    static boolean canConfirm = false;


    public static void getSelectedAccommodations(String city, String type, String startDate, String endDate, int roomQuantity, int adultsQuantity, int childrenQuantity){
        selectedAccommodations = new ArrayList<>();
        for (Map<String, Object> acc : accommodations) {
            if (acc.get("city").equals(city)) {

                if(type.equals("Sunny Day")){
                    if(acc.get("sunnyDay").equals(true)){
                        Map<String, Object> selectedAccommodation = new HashMap<>();
                        selectedAccommodation.put("name", acc.get("name"));
                        selectedAccommodation.put("rating", acc.get("rating"));
                        selectedAccommodation.put("activities", acc.get("activities"));
                        selectedAccommodation.put("includesLunch", acc.get("includesLunch"));
                        selectedAccommodation.put("pricePerson", acc.get("pricePerson"));
                        calculateSunnyDayPrice(selectedAccommodation, startDate, adultsQuantity, childrenQuantity);
                        selectedAccommodations.add(selectedAccommodation);
                    }
                }
                else {
                    if(acc.get("type").equals(type)){
                        if(acc.get("type").equals("Hotel")){
                            Map<String, Object> selectedAccommodation = new HashMap<>();
                            selectedAccommodation.put("name", acc.get("name"));
                            selectedAccommodation.put("rating", acc.get("rating"));
                            int[] pricePerNight = (int[]) acc.get("pricePerNight");
                            selectedAccommodation.put("pricePerNight", pricePerNight[0]);
                            calculateStayPrice(selectedAccommodation, startDate, endDate, roomQuantity, type);
                            selectedAccommodations.add(selectedAccommodation);

                        }
                        else {
                            if(roomQuantity == (int) acc.get("roomQuantity") && thereIsAccommodationAvailable((String) acc.get("name"), startDate, endDate) ){
                                Map<String, Object> selectedAccommodation = new HashMap<>();
                                selectedAccommodation.put("name", acc.get("name"));
                                selectedAccommodation.put("rating", acc.get("rating"));
                                selectedAccommodation.put("pricePerNight", acc.get("pricePerNight"));
                                selectedAccommodation.put("features", acc.get("features"));
                                calculateStayPrice(selectedAccommodation, startDate, endDate, roomQuantity, type);
                                selectedAccommodations.add(selectedAccommodation);
                            }
                        }
                    }

                }

            }
        }
    }

    public static boolean thereIsAccommodationAvailable(String name , String startDate, String endDate){
        for (Map<String, Object> booking : bookings){
            if(booking.get("accommodation").equals(name)){
                if(dateIntercept(startDate, endDate, (String) booking.get("startDate"), (String) booking.get("endDate"))){
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean dateIntercept(String startDate, String endDate, String bookingStartDate, String bookingEndDate) {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        LocalDate bookingStart = LocalDate.parse(bookingStartDate);
        LocalDate bookingEnd = LocalDate.parse(bookingEndDate);

        return (start.isBefore(bookingEnd) || start.equals(bookingEnd)) &&
                (end.isAfter(bookingStart) || end.equals(bookingStart));
    }

    public static void calculateStayPrice(Map<String, Object> selectedAccomodation, String startDate, String endDate, int roomQuantity, String type) {

        int basePrice = (int) selectedAccomodation.get("pricePerNight");

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        long days = ChronoUnit.DAYS.between(start, end);

        int totalBasePrice = 0;
        if(type.equals("Hotel")){
            totalBasePrice = basePrice * roomQuantity * (int) days;
        }
        else{
            totalBasePrice = basePrice * (int) days;
        }

        double discountOrIncrease = 0.0;
        String adjustmentType = "None";

        if (start.getDayOfMonth() >= 5 && end.getDayOfMonth() <= 10) {
            discountOrIncrease = -0.08 * totalBasePrice;
            adjustmentType = "8% Discount";
        } else if (start.getDayOfMonth() >= 10 && end.getDayOfMonth() <= 15) {
            discountOrIncrease = 0.10 * totalBasePrice;
            adjustmentType = "10% Increase";
        } else if (end.getDayOfMonth() > 25) {
            discountOrIncrease = 0.15 * totalBasePrice;
            adjustmentType = "15% Increase";
        }

        double finalPrice = totalBasePrice + discountOrIncrease;

        selectedAccomodation.put("basePrice", totalBasePrice);
        selectedAccomodation.put("adjustmentType", adjustmentType);
        selectedAccomodation.put("adjustmentValue", discountOrIncrease);
        selectedAccomodation.put("finalPrice", finalPrice);
    }

    public static void calculateSunnyDayPrice(Map<String, Object> selectedAccomodation, String startDate, int adultsQuantity, int childrenQuantity) {

        int pricePerson = (int) selectedAccomodation.get("pricePerson");
        int totalBasePrice = pricePerson * (adultsQuantity + childrenQuantity);

        LocalDate start = LocalDate.parse(startDate);

        double discountOrIncrease = 0.0;
        String adjustmentType = "None";

        if (start.getDayOfMonth() >= 5 && start.getDayOfMonth() <= 10) {
            discountOrIncrease = -0.08 * totalBasePrice;
            adjustmentType = "8% Discount";
        } else if (start.getDayOfMonth() >= 10 && start.getDayOfMonth() <= 15) {
            discountOrIncrease = 0.10 * totalBasePrice;
            adjustmentType = "10% Increase";
        } else if (start.getDayOfMonth() > 25) {
            discountOrIncrease = 0.15 * totalBasePrice;
            adjustmentType = "15% Increase";
        }

        double finalPrice = totalBasePrice + discountOrIncrease;

        selectedAccomodation.put("basePrice", totalBasePrice);
        selectedAccomodation.put("adjustmentType", adjustmentType);
        selectedAccomodation.put("adjustmentValue", discountOrIncrease);
        selectedAccomodation.put("finalPrice", finalPrice);
    }

    public static int[] getAvailableRooms(String name , String startDate, String endDate, int[] rooms){
        int[] availableRooms = rooms.clone();
        for (Map<String, Object> booking : bookings){
            if(booking.get("accommodation").equals(name)){
                if(dateIntercept(startDate, endDate, (String) booking.get("startDate"), (String) booking.get("endDate"))){
                    int[] roomQuantity = (int[]) booking.get("roomQuantity");
                    for (int i = 0; i < availableRooms.length; i++) {
                        availableRooms[i] -= roomQuantity[i];
                        if (availableRooms[i] < 0) {
                            availableRooms[i] = 0;
                        }
                    }
                }
            }
        }
        return availableRooms;
    }

    public static Map<String, Object> confirmRooms(String name, String startDate, String endDate, int adultsQuantity, int childrenQuantity, int roomQuantity){
        Map<String, Object> rooms = new HashMap<>();
        for(Map<String, Object> acc : accommodations){
            if(acc.get("name").equals(name)){
                int[] availableRooms = getAvailableRooms(name, startDate, endDate, (int[]) acc.get("rooms"));
                int[] pricePerNight =( int[]) acc.get("pricePerNight");
                rooms.put("availableRooms", availableRooms);
                rooms.put("pricePerNight", pricePerNight);
                break;
            }
        }
        return rooms;
    }

    public static boolean reserve(String firstName, String lastName, String email, String nationality, String phone, String birthDate, String hour){

        for(Map<String, String> user : users){
            if(user.get("email").equals(email)){
                return false;
            }
        }

        users.add(new HashMap<>(Map.of(
                "firstName", firstName,
                "lastName", lastName,
                "email", email,
                "nationality", nationality,
                "phone", phone,
                "birthDate", birthDate,
                "hour", hour
        )));

        newBooking.put("userEmail", email);
        Map<String, Object> bookingCopy = new HashMap<>();
        for (Map.Entry<String, Object> entry : newBooking.entrySet()) {
            Object valor = entry.getValue();
            bookingCopy.put(entry.getKey(), valor);
        }
        bookings.add(bookingCopy);
        canReserve = false;
        return true;
    }

    public static void main(String[] args) {


        // Initial data for accommodations
        HashMap<String, Object> hotelAccommodation = new HashMap<>();
        hotelAccommodation.put("name", "Hotel Estelar");
        hotelAccommodation.put("city", "Manizales");
        hotelAccommodation.put("type", "Hotel");
        hotelAccommodation.put("rating", 4.6);
        hotelAccommodation.put("pricePerNight", new int[]{180000, 250000, 350000, 500000, 1000000});
        hotelAccommodation.put("sunnyDay", true);
        hotelAccommodation.put("pricePerson", 90000);
        hotelAccommodation.put("peopleQuantity", 30);
        hotelAccommodation.put("activities", "Pool, Spa, Yoga");
        hotelAccommodation.put("includesLunch", true);
        hotelAccommodation.put("rooms",new int[]{6,4,0,1,0});
        accommodations.add(hotelAccommodation);

        accommodations.add(new HashMap<>(Map.of(
                "name", "Hotel Carretero",
                "city", "Manizales",
                "type", "Hotel",
                "rating", 4.8,
                "pricePerNight", new int[]{160000, 230000, 330000, 480000, 950000},
                "sunnyDay", false,
                "rooms", new int[]{10, 0, 6, 3, 1}
        )));

        HashMap<String, Object> farmAccommodation = new HashMap<>();
        farmAccommodation.put("name", "Granja Paraiso");
        farmAccommodation.put("city", "Manizales");
        farmAccommodation.put("type", "Farm");
        farmAccommodation.put("rating", 4.4);
        farmAccommodation.put("pricePerNight", 350000);
        farmAccommodation.put("sunnyDay", true);
        farmAccommodation.put("pricePerson", 110000);
        farmAccommodation.put("peopleQuantity", 40);
        farmAccommodation.put("activities", "Cabalgata, Eco paseo, Piscina");
        farmAccommodation.put("includesLunch", true);
        farmAccommodation.put("features", "Jardin, Piscina y Terraza");
        farmAccommodation.put("roomQuantity", 3);
        accommodations.add(farmAccommodation);

        accommodations.add(new HashMap<>(Map.of(
                "name", "Apartamento en Milan zona G",
                "city", "Manizales",
                "type", "Apartment",
                "rating", 4.5,
                "pricePerNight", 250000,
                "sunnyDay", false,
                "features", "Sala, Cocina, WiFi gratis, 1 cama doble y 1 cama sencilla",
                "roomQuantity", 2
        )));

        accommodations.add(new HashMap<>(Map.of(
                "name", "Apartamento en Palermo",
                "city", "Manizales",
                "type", "Apartment",
                "rating", 4.7,
                "pricePerNight", 280000,
                "sunnyDay", false,
                "features", "Cocina, 2 baños, 2 camas dobles",
                "roomQuantity", 2
        )));

        // Initial data for room types
        roomTypes.add(new HashMap<>(Map.of(
                "type", "Standard",
                "features", "Cama doble, baño privado, TV, escritorio, internet básico"
        )));

        roomTypes.add(new HashMap<>(Map.of(
                "type", "Superior",
                "features", "Cama king size, baño más grande, adicionales (bata, pantuflas, minibar), vista parcial"
        )));

        roomTypes.add(new HashMap<>(Map.of(
                "type", "Deluxe",
                "features", "Cama king size, sala de estar independiente, baño de lujo con jacuzzi, vista panorámica"
        )));

        roomTypes.add(new HashMap<>(Map.of(
                "type", "Junior Suite",
                "features", "Combinación de dormitorio y pequeña sala de estar, cama king size, baño completo, sofá cama"
        )));

        roomTypes.add(new HashMap<>(Map.of(
                "type", "Presidential Suite",
                "features", "Amplia sala, comedor, cocineta, baño de lujo, servicios personalizados (mayordomo, chef privado)"
        )));

        // Initial data for users
        users.add(new HashMap<>(Map.of(
                "firstName", "John",
                "lastName", "Doe",
                "email", "john.doe@gmail.com",
                "nationality", "Colombiano",
                "phone", "3001234567",
                "birthDate", "1990-05-20"
        )));

        // Initial data for bookings
        bookings.add(new HashMap<>(Map.of(
                "userEmail", "john.doe@gmail.com",
                "accommodation", "Hotel Carretero",
                "startDate", "2024-12-20",
                "endDate", "2024-12-25",
                "roomQuantity", new int[]{0, 0, 2, 0, 0},
                "totalPrice", 900000
        )));

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nSistema de Reservas");
            System.out.println("1. Buscar alojamiento");
            System.out.println("2. Confirmar habitaciones (hoteles)");
            System.out.println("3. Realizar reserva");
            System.out.println("4. Actualizar reserva");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> {
                    System.out.print("Ciudad: ");
                    String city = scanner.nextLine();

                    System.out.print("Tipo de alojamiento (Hotel, Apartment, Farm, Sunny Day): ");
                    String type = scanner.nextLine();

                    String startDate;
                    String endDate="";
                    int roomQuantity = 0;
                    if(type.equals("Sunny Day")){
                        System.out.print("Fecha (YYYY-MM-DD): ");
                        startDate = scanner.nextLine();
                    }
                    else{
                        System.out.print("Fecha de inicio (YYYY-MM-DD): ");
                        startDate = scanner.nextLine();

                        System.out.print("Fecha de fin (YYYY-MM-DD): ");
                        endDate = scanner.nextLine();

                        System.out.print("Cantidad de habitaciones: ");
                        roomQuantity = scanner.nextInt();
                    }

                    System.out.print("Cantidad de adultos: ");
                    int adultsQuantity = scanner.nextInt();

                    System.out.print("Cantidad de niños: ");
                    int childrenQuantity = scanner.nextInt();
                    scanner.nextLine(); // Consumir el salto de línea

                    newBooking.put("startDate", startDate);
                    newBooking.put("endDate", endDate);
                    newBooking.put("adultsQuantity", adultsQuantity);
                    newBooking.put("childrenQuantity", childrenQuantity);

                    getSelectedAccommodations(city, type, startDate, endDate, roomQuantity, adultsQuantity, childrenQuantity);

                    if (selectedAccommodations.isEmpty()) {
                        System.out.println("No se encontraron alojamientos disponibles según los criterios ingresados.");
                    } else {
                        System.out.println("Alojamientos encontrados:");
                        int number = 1;
                        for (Map<String, Object> accommodation : selectedAccommodations) {
                            System.out.println(number+". "+ accommodation.get("name"));
                            for (Map.Entry<String, Object> entry : accommodation.entrySet()) {
                                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
                            }
                            System.out.println();
                            number++;
                        }
                        System.out.print("Seleccione una opción: ");
                        int accOption = scanner.nextInt();
                        if(accOption > 0 && accOption <= selectedAccommodations.size()){
                            Map<String, Object> acc= selectedAccommodations.get(accOption-1);
                            newBooking.put("accommodation", acc.get("name"));
                            if(type.equals("Hotel")){
                                canConfirm = true;
                            }
                            else{
                                newBooking.put("totalPrice", acc.get("finalPrice"));
                                canReserve = true;
                            }

                            System.out.println("Alojamiento seleccionado: "+acc.get("name"));
                        }
                        else{
                            newBooking = new HashMap<>();
                            System.out.println("Opción inválida. Intente de nuevo.");
                        }

                    }
                    break;
                }
                case 3 ->{
                    if(canReserve){
                        System.out.print("Nombre: ");
                        String firstName = scanner.nextLine();

                        System.out.print("Apellido: ");
                        String lastName = scanner.nextLine();

                        System.out.print("Email: ");
                        String email = scanner.nextLine();

                        System.out.print("Nacionalidad: ");
                        String nationality = scanner.nextLine();

                        System.out.print("Teléfono: ");
                        String phone = scanner.nextLine();

                        System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
                        String birthDate = scanner.nextLine();

                        System.out.print("Hora aproximada de llegada (HH:mm): ");
                        String hour = scanner.nextLine();

                        boolean reserved = reserve(firstName, lastName, email, nationality, phone, birthDate, hour);

                        if(reserved){
                            System.out.println("Se ha realizado la reserva con éxito.");
                        }
                        else{
                            System.out.println("Ya existe otra reserva con el mismo usuario.");
                        }
                    }
                    else{
                        System.out.println("No es posible reservar.");
                    }
                }
                case 0 -> {
                    System.out.println("Gracias por usar el sistema. Adiós!");
                    exit = true;
                    break;
                }
                default -> System.out.println("Opción inválida. Intente de nuevo.");
            }
        }
        scanner.close();
    }
}