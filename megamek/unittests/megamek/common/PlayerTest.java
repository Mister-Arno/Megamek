package megamek.common;
import java.util.Enumeration;
import java.util.List;
import megamek.common.weapons.LegAttack;
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
        player = new Player(16, "TestPlayer");

    }

    @Test
    public void testConstructor(){
        // QUESTION
        // Should it be possible to create a player with an empty name / null name / non-unique ID?
        // Who / when / where should this raise an error?

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
        // QUESTION
        // When does being observer (not) imply seeEntireBoard and vice versa?

        // player with no team
        assertFalse(player.isObserver());
        assertFalse(player.canSeeAll());
        player.setObserver(true);
        assertTrue(player.isObserver());
        player.setObserver(false);
        assertFalse(player.isObserver());

        // player with team
        Game g = new Game();
        Player player2 = new Player(17, "testplayer2");
        Player player3 = new Player(18, "testplayer3");

        player.setGame(g);
        player2.setGame(g);
        player3.setGame(g);

        g.addPlayer(player.getId(), player);
        g.addPlayer(player2.getId(), player2);
        g.addPlayer(player3.getId(), player3);

        player.setTeam(3);
        player2.setTeam(3);
        player3.setTeam(4);

        g.setupTeams();
        assertFalse(g.getTeamForPlayer(player).isObserverTeam());
        assertFalse(g.getTeamForPlayer(player2).isObserverTeam());
        assertFalse(g.getTeamForPlayer(player3).isObserverTeam());

        // Everyone in team of player3 is an observer
        // The other team is not observer teams
        player3.setObserver(true);
        assertTrue(g.getTeamForPlayer(player3).isObserverTeam());
        assertFalse(g.getTeamForPlayer(player).isObserverTeam());
        assertFalse(g.getTeamForPlayer(player2).isObserverTeam());

        // Only a part of the team of player is observer
        // Therefore the team is not an observer team
        player.setObserver(true);
        assertFalse(g.getTeamForPlayer(player).isObserverTeam());
        assertFalse(g.getTeamForPlayer(player2).isObserverTeam());
        assertTrue(g.getTeamForPlayer(player3).isObserverTeam());

        // Everyone in the team of player is an observer
        // Therefore the team is an observer team
        player2.setObserver(true);
        assertTrue(g.getTeamForPlayer(player).isObserverTeam());
        assertTrue(g.getTeamForPlayer(player2).isObserverTeam());
        assertTrue(g.getTeamForPlayer(player3).isObserverTeam());

        // When someone of the team is not an observer (anymore)
        // The team is not an observer team
        player2.setObserver(false);
        assertFalse(g.getTeamForPlayer(player).isObserverTeam());
        assertFalse(g.getTeamForPlayer(player2).isObserverTeam());
        assertTrue(g.getTeamForPlayer(player3).isObserverTeam());
    }

    @Test
    public void testEnemies(){
        // QUESTION
        // What is the desired output when passing NULL to the isEnemyOf method?

        Game g = new Game();
        Player player2 = new Player(17, "testplayer2");
        Player player3 = new Player(18, "testplayer3");

        player.setGame(g);
        player2.setGame(g);
        player3.setGame(g);

        g.addPlayer(player.getId(), player);
        g.addPlayer(player2.getId(), player2);
        g.addPlayer(player3.getId(), player3);

        assertFalse(player.isEnemyOf(player)); // not an enemy of itself
        assertTrue(player.isEnemyOf(player2)); // when at least one player is not in a team, they are enemies
        assertTrue(player2.isEnemyOf(player));

        player.setTeam(3);
        g.setupTeams();

        assertFalse(player.isEnemyOf(player)); // not an enemy of itself
        assertTrue(player.isEnemyOf(player2)); // when at least one player is not in a team, they are enemies
        assertTrue(player2.isEnemyOf(player));

        player2.setTeam(4);
        g.setupTeams();
        assertFalse(player.isEnemyOf(player)); // not an enemy of itself
        assertTrue(player.isEnemyOf(player2)); // different teams are enemies
        assertTrue(player2.isEnemyOf(player));

        player3.setTeam(3);
        g.setupTeams();
        assertFalse(player.isEnemyOf(player)); // not an enemy of itself
        assertFalse(player.isEnemyOf(player3)); // same team is not an enemy
        assertFalse(player3.isEnemyOf(player)); // same team is not an enemy
        assertTrue(player.isEnemyOf(player2)); // different teams are enemies
        assertTrue(player2.isEnemyOf(player));


    }

    @Test
    public void testEqualsAndHashCode(){
        //QUESTION
        //see constructor: same IDs allowed? Are they equal?

        Game g = new Game();
        assertNotEquals(player, g); // different types are never equal
        assertNotEquals(null, player); // null is never equal
        assertEquals(player, player); // same object is equal

        Player player2 = new Player(player.getId() +1, "testplayer2");
        Player player3 = new Player(player.getId(), "testplayer3");

        assertNotEquals(player, player2); // different id
        assertNotEquals(player.hashCode(), player2.hashCode()); // assuming inequality implies different hashcode

        assertEquals(player, player3); // !!! assuming same id means same player !!!
        assertEquals(player.hashCode(), player3.hashCode()); // assuming equality implies same hashcode
    }
    public Vector<Entity> addEntitiesForTest(){
        Vector<Entity> entities = new Vector<>();
        entities.add(new Aero());
        entities.add(new ConvFighter());
        entities.add(new FixedWingSupport());
        entities.add(new FighterSquadron());
        entities.add(new Jumpship());
        entities.add(new Warship());
        entities.add(new SpaceStation());
        entities.add(new SmallCraft());
        entities.add(new EscapePods());
        entities.add(new Dropship());
        entities.add(new TeleMissile());
        entities.add(new GunEmplacement());
        entities.add(new Infantry());
        entities.add(new BattleArmor());
        entities.add(new BipedMech());
        entities.add(new ArmlessMech());
        entities.add(new QuadMech());
        entities.add(new QuadVee());
        entities.add(new TripodMech());
        entities.add(new MechWarrior());
        entities.add(new Protomech());
        entities.add(new Tank());
        entities.add(new SuperHeavyTank());
        entities.add(new GunEmplacement());
        entities.add(new SupportTank());
        entities.add(new LargeSupportTank());
        entities.add(new TeleMissile());
        return entities;
    }

    @Test
    public void testBattleValue(){
        Vector<Entity> playerEntities = addEntitiesForTest();
        Vector<Entity> player2Entities = addEntitiesForTest();

        Game g = new Game();
        player.setGame(g);
        g.addPlayer(player.getId(), player);



        // add all entities to the game
        // and calculate the upsum of their battle values
        float upsum = 0;
        for (Entity e : playerEntities) {
            upsum += e.calculateBattleValue();
            e.setOwner(player);
            g.addEntity(e);

        }

        // Entities get captured
        for (Entity e : playerEntities) {
            e.setRemovalCondition(IEntityRemovalConditions.REMOVE_CAPTURED);
        }
        // Check battle value of removed entities
        g.setOutOfGameEntitiesVector(playerEntities);
        assertEquals((int) upsum, player.getFledBV());

        // Entities of another player do not influence the BV
        Player player2 = new Player(17, "testplayer2");
        for (Entity e : player2Entities) {
            e.setOwner(player2);
            g.addEntity(e);
            assertEquals((int) upsum, player.getBV()); // upsum not influenced
        }


        // Check initial BV
        int original_BV = player.getInitialBV();
        player.increaseInitialBV(28);
        assertEquals(original_BV + 28, player.getInitialBV());

        player.setInitialBV();
        assertEquals((int) upsum, player.getInitialBV());
        player.increaseInitialBV(68);
        assertEquals((int) upsum + 68, player.getInitialBV());
    }

    @Test
    public void testBonuses(){
        // Entities and thus a game are needed to have a bonus
        assertEquals(0, player.getTurnInitBonus());
        Game g = new Game();
        player.setGame(g);
        g.addPlayer(player.getId(), player);
        assertEquals(0, player.getTurnInitBonus());


        // Add entities to the game
        Vector<Entity> entities = addEntitiesForTest();

        // Calculate the upsum of their battle values
        float upsumInitBonus = 0;
        float upsumCommandBonus = 0;
        for (Entity e : entities) {
            upsumInitBonus += e.getQuirkIniBonus();
            upsumCommandBonus += e.getCrew().getCommandBonus();
            e.setOwner(player);
            g.addEntity(e);
        }
        assertEquals((int) upsumInitBonus, player.getTurnInitBonus());
        assertEquals((int) upsumCommandBonus, player.getCommandBonus());
    }

    @Test
    public void testAirborne(){
        // QUESTION
        // Should this crash when game is null?
        // assertEquals(new Vector<Integer>(), player.getAirborneVTOL());

        Game g = new Game();
        player.setGame(g);
        g.addPlayer(player.getId(), player);
        Vector<Entity> entities = addEntitiesForTest();
        for (Entity e : entities) {
            e.setOwner(player);
            g.addEntity(e);
        }
        assertEquals(0, player.getAirborneVTOL().size()); // there are no airborne VTOLs

        // Add VTOL
        VTOL v1 = new VTOL();
        v1.setOwner(player);
        g.addEntity(v1);;
        assertEquals(0, player.getAirborneVTOL().size()); // not airborne yet

        v1.setElevation(248);
        assertEquals(1, player.getAirborneVTOL().size()); // airborne now

        // Add another VTOL
        VTOL v2 = new VTOL();
        v2.setOwner(player);
        g.addEntity(v2);
        assertEquals(1, player.getAirborneVTOL().size()); // only one airborne
        v2.setElevation(222);
        assertEquals(2, player.getAirborneVTOL().size()); // two airborne
    }




















}
