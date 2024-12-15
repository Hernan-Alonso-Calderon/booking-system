import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    // Data structures
    static List<Map<String, Object>> accommodations = new ArrayList<>();
    static List<Map<String, Object>> roomTypes = new ArrayList<>();
    static List<Map<String, String>> users = new ArrayList<>();
    static List<Map<String, Object>> bookings = new ArrayList<>();
    static List<Map<String, Object>> selectedAccomodations = new ArrayList<>();

    public static void main(String[] args) {
        // Initial data for accommodations
        accommodations.add(new HashMap<>(Map.of(
                "name", "Hotel Estelar",
                "city", "Manizales",
                "type", "Hotel",
                "rating", 4.6,
                "pricePerNight", new int[]{180000, 250000, 350000, 500000, 1000000},
                "sunnyDay", true,
                "activities", "Pool, Spa, Yoga",
                "includesLunch", true,
                "availableRooms",new int[]{10,8,5,3,1},
                "totalRooms", 27
        )));


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

        accommodations.add(new HashMap<>(Map.of(
                "name", "Paradise Farm",
                "city", "Manizales",
                "type", "Farm",
                "rating", 4.4,
                "pricePerNight", 350000,
                "sunnyDay", true,
                "activities", "Horseback riding, Eco walk, Pool",
                "includesLunch", true,
                "features", "3 rooms, with garden, common pool and terrace"
        )));

        accommodations.add(new HashMap<>(Map.of(
                "name", "Paradise Farm",
                "city", "Manizales",
                "type", "Apartment",
                "rating", 4.5,
                "pricePerNight", 250000,
                "sunnyDay", false,
                "features", "3 Bedrooms offers accommodation in Manizales with free WiFi"
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
    }
}