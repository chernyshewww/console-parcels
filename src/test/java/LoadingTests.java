import org.example.Main;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoadingTests {

    @Test
    public void testLoadParcelsIntoTrucks_Strategy1() {
        List<char[][]> parcels = new ArrayList<>();
        parcels.add(new char[][]{
                {'6', '6', '6'},
                {'6', '6', '6'}
        });
        parcels.add(new char[][]{
                {'7', '7', '7'},
                {'7', '7', '7'}
        });

        List<char[][]> trucks = Main.loadParcelsIntoTrucks(parcels, 1);
        assertEquals(1, trucks.size(), "Strategy 1 should use 1 truck");
    }

    @Test
    public void testLoadParcelsIntoTrucks_Strategy2() {
        List<char[][]> parcels = new ArrayList<>();
        parcels.add(new char[][]{
                {'6', '6', '6'},
                {'6', '6', '6'}
        });
        parcels.add(new char[][]{
                {'7', '7', '7'},
                {'7', '7', '7','7'}
        });

        List<char[][]> trucks = Main.loadParcelsIntoTrucks(parcels, 2);
        assertEquals(2, trucks.size(), "Strategy 2 should use 2 trucks");
    }
}
