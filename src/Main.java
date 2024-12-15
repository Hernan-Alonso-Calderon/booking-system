import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Main {
    // Data structures
    static List<Map<String, Object>> accommodations = new ArrayList<>();
    static List<Map<String, Object>> roomTypes = new ArrayList<>();
    static List<Map<String, String>> users = new ArrayList<>();
    static List<Map<String, Object>> bookings = new ArrayList<>();
    static List<Map<String, Object>> selectedAccomodations = new ArrayList<>();


    public static void getSelectedAccomodations(String city, String type, String startDate, String endDate, int roomQuantity, int adultsQuantity, int childrenQuantity){
        selectedAccomodations = new ArrayList<>();
        for (Map<String, Object> acc : accommodations) {
            if (acc.get("city").equals(city)) {

                if(type.equals("Sunny Day")){
                    if(acc.get("sunnyDay").equals(true)){
                        Map<String, Object> selectedAccomodation = new HashMap<>();
                        selectedAccomodation.put("name", acc.get("name"));
                        selectedAccomodation.put("rating", acc.get("rating"));
                        selectedAccomodation.put("activities", acc.get("activities"));
                        selectedAccomodation.put("includesLunch", acc.get("includesLunch"));
                        selectedAccomodation.put("pricePerson", acc.get("pricePerson"));
                        calculateSunnyDayPrice(selectedAccomodation, startDate, adultsQuantity, childrenQuantity);
                        selectedAccomodations.add(selectedAccomodation);
                    }
                }
                else {
                    if(acc.get("type").equals(type)){
                        if(acc.get("type").equals("Hotel")){
                            if(thereIsRoomAvailability(acc, roomQuantity)){
                                Map<String, Object> selectedAccomodation = new HashMap<>();
                                selectedAccomodation.put("name", acc.get("name"));
                                selectedAccomodation.put("rating", acc.get("rating"));
                                int[] pricePerNight = (int[]) acc.get("pricePerNight");
                                selectedAccomodation.put("pricePerNight", pricePerNight[0]);
                                calculateStayPrice(selectedAccomodation, startDate, endDate, roomQuantity, type);
                                selectedAccomodations.add(selectedAccomodation);
                            }
                        }
                        else {
                            if(acc.containsKey("available") && acc.get("available").equals(true) && roomQuantity == (int) acc.get("roomQuantity") ){
                                Map<String, Object> selectedAccomodation = new HashMap<>();
                                selectedAccomodation.put("name", acc.get("name"));
                                selectedAccomodation.put("rating", acc.get("rating"));
                                selectedAccomodation.put("pricePerNight", acc.get("pricePerNight"));
                                selectedAccomodation.put("features", acc.get("features"));
                                calculateStayPrice(selectedAccomodation, startDate, endDate, roomQuantity, type);
                                selectedAccomodations.add(selectedAccomodation);
                            }
                        }
                    }

                }

            }
        }
    }

    public static boolean thereIsRoomAvailability(Map<String, Object> accommodation, int roomQuantity){
        int[] availableRooms = (int[]) accommodation.get("availableRooms");

        int totalAvailableRooms = 0;
        for (int rooms : availableRooms) {
            totalAvailableRooms += rooms;
        }
        return totalAvailableRooms >= roomQuantity;
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
        hotelAccommodation.put("availableRooms",new int[]{10,8,5,3,1});
        hotelAccommodation.put( "totalRooms", 27);
        accommodations.add(hotelAccommodation);

        accommodations.add(new HashMap<>(Map.of(
                "name", "Hotel Carretero",
                "city", "Manizales",
                "type", "Hotel",
                "rating", 4.8,
                "pricePerNight", new int[]{160000, 230000, 330000, 480000, 950000},
                "sunnyDay", false,
                "availableRooms", new int[]{12, 10, 6, 4, 2},
                "totalRooms", 34
        )));

        HashMap<String, Object> farmAccommodation = new HashMap<>();
        farmAccommodation.put("name", "Paradise Farm");
        farmAccommodation.put("city", "Manizales");
        farmAccommodation.put("type", "Farm");
        farmAccommodation.put("rating", 4.4);
        farmAccommodation.put("pricePerNight", 350000);
        farmAccommodation.put("sunnyDay", true);
        farmAccommodation.put("pricePerson", 110000);
        farmAccommodation.put("peopleQuantity", 40);
        farmAccommodation.put("activities", "Cabalgata, Eco paseo, Piscina");
        farmAccommodation.put("includesLunch", true);
        farmAccommodation.put( "available", true);
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
                "available", true,
                "features", "Sala, Cocina, WiFi gratis, 1 cama doble y 2 camas sencillas",
                "roomQuantity", 3
        )));

        accommodations.add(new HashMap<>(Map.of(
                "name", "Apartamento en Palermo",
                "city", "Manizales",
                "type", "Apartment",
                "rating", 4.7,
                "pricePerNight", 280000,
                "sunnyDay", false,
                "available", true,
                "features", "Cocina, 2 baños, 2 camas dobles",
                "roomQuantity", 2
        )));

        // Initial data for room types
        roomTypes.add(new HashMap<>(Map.of(
                "type", "Standard",
                "features", "Double bed, private bathroom, TV, desk, basic internet"
        )));

        roomTypes.add(new HashMap<>(Map.of(
                "type", "Superior",
                "features", "King size bed, larger bathroom, additional amenities (robe, slippers, minibar), partial view"
        )));

        roomTypes.add(new HashMap<>(Map.of(
                "type", "Deluxe",
                "features", "King size bed, separate living area, luxury bathroom with jacuzzi, panoramic view, premium amenities"
        )));

        roomTypes.add(new HashMap<>(Map.of(
                "type", "Junior Suite",
                "features", "Combination of bedroom and small living area, king size bed, full bathroom, sofa bed"
        )));

        roomTypes.add(new HashMap<>(Map.of(
                "type", "Presidential Suite",
                "features", "spacious living area, dining area, kitchenette, luxury bathroom, personalized services (butler, private chef)"
        )));

        // Initial data for users
        users.add(new HashMap<>(Map.of(
                "firstName", "John",
                "lastName", "Doe",
                "email", "john.doe@gmail.com",
                "nationality", "Colombian",
                "phone", "3001234567",
                "birthDate", "1990-05-20"
        )));

        users.add(new HashMap<>(Map.of(
                "firstName", "Mary",
                "lastName", "Smith",
                "email", "mary.smith@gmail.com",
                "nationality", "Colombian",
                "phone", "3107654321",
                "birthDate", "1985-09-10"
        )));

        // Initial data for bookings
        bookings.add(new HashMap<>(Map.of(
                "userEmail", "john.doe@gmail.com",
                "accommodation", "Hotel Estelar",
                "roomType", "Standard",
                "startDate", "2024-12-20",
                "endDate", "2024-12-25",
                "roomQuantity", 2,
                "totalPrice", 900000
        )));

        getSelectedAccomodations("Manizales","Sunny Day","2024-12-26", "2024-12-27",3,2,2);

        for (Map<String, Object> accommodation : selectedAccomodations) {
            System.out.println("Alojamiento:");
            for (Map.Entry<String, Object> entry : accommodation.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
            System.out.println();
        }
    }
}