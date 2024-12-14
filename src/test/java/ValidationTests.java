import org.example.Main;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationTests {

    @Test
    public void testIsValidParcel_ValidParcel() {
        List<String> validParcel = List.of(
                "666\n666"
        );
        assertTrue(Main.isValidParcel(validParcel));
    }

    @Test
    public void testIsValidParcel_InvalidParcel() {
        List<String> invalidParcel = List.of(
                "123"
        );
        assertFalse(Main.isValidParcel(invalidParcel));
    }

    @Test
    public void testConvertToMatrix() {
        List<String> parcel = Arrays.asList(
                "666",
                "666"
        );
        char[][] expectedMatrix = {
                {'6', '6', '6'},
                {'6', '6', '6'}
        };
        char[][] result = Main.convertToMatrix(parcel);
        assertArrayEquals(expectedMatrix, result);
    }

    @Test
    public void testPlaceParcelInTruck_Success() {
        char[][] parcel = {
                {'6', '6', '6'},
                {'6', '6', '6'}
        };
        char[][] truck = new char[6][6];
        for (char[] row : truck) {
            Arrays.fill(row, ' ');
        }

        boolean result = Main.placeParcelInTruck(parcel, truck);
        assertTrue(result);
    }

    @Test
    public void testPlaceParcelInTruck_Failure() {
        // Create a parcel that is too large for the truck (3x3)
        char[][] parcel = {
                {'6', '6', '6'},
                {'6', '6', '6'},
                {'6', '6', '6'}
        };

        char[][] truck = new char[2][2];
        for (char[] row : truck) {
            Arrays.fill(row, ' ');
        }

        assertThrowsExactly(ArrayIndexOutOfBoundsException.class, () -> {
            Main.placeParcelInTruck(parcel, truck);
        }, "if the parcel is too large or too small for the truck we will get an arrayIndexOutOfBounds");
    }

    @Test
    public void testReadParcelsFromFile() {
        List<char[][]> parcels = Main.readParcelsFromFile("src/test/resources/test_parcels.txt");
        assertNotNull(parcels);
        assertTrue(parcels.size() > 0);
    }
}
