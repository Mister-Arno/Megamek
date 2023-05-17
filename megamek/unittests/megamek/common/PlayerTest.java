package megamek.common;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Vector;
import megamek.client.ui.swing.util.PlayerColour;


public class PlayerTest {
    private Player player;
    private static final int ELO_RATING = 1500;
    private static final int MINEFIELD_COUNT = 34;

    @Before
    public void setup() {
        player = new Player(1, "TestPlayer");

    }

    @Test
    public void testConstructor(){
        Player p = new Player(34, "TestPlayer");
        assertEquals(34, p.getId());
    }

    @Test
    public void testEloRating(){
        player.setEloRating(ELO_RATING);
        assertEquals(ELO_RATING, player.getEloRating());
    }

    @Test
    public void testAddRemoveGetMinefield(){
        Minefield minefield = Minefield.createMinefield(new Coords(1,1), 1, 1, 1, 1, false, 1);

        //Should be empty at start
        assertEquals(0, player.getMinefields().size());
        assertFalse(player.containsMinefield(minefield));

        // adding a minefield to a player should add it to the player's list of visible minefields
        player.addMinefield(minefield);
        assertTrue(player.getMinefields().contains(minefield));
        assertTrue(player.containsMinefield(minefield));
        assertEquals(1, player.getMinefields().size());

        // removing a minefield from a player should remove it from the player's list of minefields
        player.removeMinefield(minefield);
        assertFalse(player.containsMinefield(minefield));
        assertEquals(0, player.getMinefields().size());
    }

    @Test
    public void testAddRemoveMultipleMinefields(){
        Vector<Minefield> minefields = new Vector<>();
        minefields.add(Minefield.createMinefield(new Coords(1,1), 1, 1, 1, 1, false, 1));
        minefields.add(Minefield.createMinefield(new Coords(2,2), 2, 1, 1, 1, false, 1));
        minefields.add(Minefield.createMinefield(new Coords(3, 3), 3, 1, 1, 1, false, 1));

        //Should be empty at start
        assertEquals(0, player.getMinefields().size());

        // adding multiple minefields to a player should add them to the player's list of visible minefields
        player.addMinefields(minefields);
        assertEquals(minefields.size(), player.getMinefields().size());
        for (int i = 0; i < minefields.size(); i++) {
            assertTrue(player.containsMinefield(minefields.get(i)));
        }

        // removing multiple minefields from a player should remove them from the player's list of minefields
        player.removeMinefields();
        assertEquals(0, player.getMinefields().size());
    }

    @Test
    public void testMFConventional(){
        assertFalse(player.hasMinefields());
        assertEquals(0, player.getNbrMFConventional());
        player.setNbrMFConventional(MINEFIELD_COUNT);
        assertTrue(player.hasMinefields());
        assertEquals(MINEFIELD_COUNT, player.getNbrMFConventional());
        player.setNbrMFConventional(0);
        assertFalse(player.hasMinefields());
        assertEquals(0, player.getNbrMFConventional());
    }

    @Test
    public void testMFCommand(){
        assertFalse(player.hasMinefields());
        assertEquals(0, player.getNbrMFCommand());
        player.setNbrMFCommand(MINEFIELD_COUNT);
        assertTrue(player.hasMinefields());
        assertEquals(MINEFIELD_COUNT, player.getNbrMFCommand());
        player.setNbrMFCommand(0);
        assertFalse(player.hasMinefields());
        assertEquals(0, player.getNbrMFCommand());
    }

    @Test
    public void testMFVibra(){
        assertFalse(player.hasMinefields());
        assertEquals(0, player.getNbrMFVibra());
        player.setNbrMFVibra(MINEFIELD_COUNT);
        assertTrue(player.hasMinefields());
        assertEquals(MINEFIELD_COUNT, player.getNbrMFVibra());
        player.setNbrMFVibra(0);
        assertFalse(player.hasMinefields());
        assertEquals(0, player.getNbrMFVibra());
    }

    @Test
    public void testMFActive(){
        assertFalse(player.hasMinefields());
        assertEquals(0, player.getNbrMFActive());
        player.setNbrMFActive(MINEFIELD_COUNT);
        assertTrue(player.hasMinefields());
        assertEquals(MINEFIELD_COUNT, player.getNbrMFActive());
        player.setNbrMFActive(0);
        assertFalse(player.hasMinefields());
        assertEquals(0, player.getNbrMFActive());
    }

    @Test
    public void testMFInferno(){
        assertFalse(player.hasMinefields());
        assertEquals(0, player.getNbrMFInferno());
        player.setNbrMFInferno(MINEFIELD_COUNT);
        assertTrue(player.hasMinefields());
        assertEquals(MINEFIELD_COUNT, player.getNbrMFInferno());
        player.setNbrMFInferno(0);
        assertFalse(player.hasMinefields());
        assertEquals(0, player.getNbrMFInferno());
    }

    @Test
    public void testCamouflage(){
        assertNotNull(player.getCamouflage());
    }

    @Test
    public void testSimpleGettersSetters(){
        player.setCamoCategory("testcategory");
        assertEquals("testcategory", player.getCamoCategory());

        player.setCamoFileName("testfilename");
        assertEquals("testfilename", player.getCamoFileName());

        player.setGame(new Game());

        player.setName("testname");
        assertEquals("testname", player.getName());

        player.setTeam(34);
        assertEquals(34, player.getTeam());

        player.setDone(true);
        assertTrue(player.isDone());
        player.setDone(false);
        assertFalse(player.isDone());

        player.setGhost(true);
        assertTrue(player.isGhost());
        player.setGhost(false);
        assertFalse(player.isGhost());

        player.setSeeAll(true);
        assertTrue(player.getSeeAll());
        player.setSeeAll(false);
        assertFalse(player.getSeeAll());

        PlayerColour col = PlayerColour.RED;
        player.setColour(col);
        assertEquals(col, player.getColour());

        player.setStartingPos(34);
        assertEquals(34, player.getStartingPos());

        player.setAdmitsDefeat(true);
        assertTrue(player.admitsDefeat());
        player.setAdmitsDefeat(false);
        assertFalse(player.admitsDefeat());

        player.setAllowTeamChange(true);
        assertTrue(player.isAllowingTeamChange());
        player.setAllowTeamChange(false);
        assertFalse(player.isAllowingTeamChange());

        player.setInitCompensationBonus(34);
        assertEquals(34, player.getInitCompensationBonus());

        player.setConstantInitBonus(34);
        assertEquals(34, player.getConstantInitBonus());
    }

    @Test
    public void testArthyAutoHitHexes(){
        Vector<Coords> coords = new Vector<>();
        coords.add(new Coords(1,1));
        coords.add(new Coords(2,2));
        player.setArtyAutoHitHexes(coords);
        assertEquals(coords, player.getArtyAutoHitHexes());

        Coords c2 = new Coords(3,3);
        coords.add(c2);
        player.addArtyAutoHitHex(c2);
        assertEquals(coords, player.getArtyAutoHitHexes());

        player.removeArtyAutoHitHexes();
        assertEquals(0, player.getArtyAutoHitHexes().size());
    }

    @Test
    public void testObserver(){
        player.setObserver(true);
        assertTrue(player.canSeeAll());


    }










}
