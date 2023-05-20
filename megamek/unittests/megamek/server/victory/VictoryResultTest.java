package megamek.server.victory;

import megamek.common.IPlayer;
import megamek.common.Player;
import megamek.common.Report;
import org.junit.Test;
import static org.junit.Assert.*;


public class VictoryResultTest {

    @Test
    public void testConstructorsGeneralTest(){
        // test if objects have the right behavior after construction

        // Constructor 1 indicates a win or a loss
        // A game with no players or teams is a draw
        VictoryResult result1 = new VictoryResult(false);
        assertFalse(result1.victory());
        assertEquals(IPlayer.PLAYER_NONE, result1.getWinningPlayer());
        assertEquals(IPlayer.TEAM_NONE, result1.getWinningTeam());
        assertTrue(result1.isDraw());

        VictoryResult result2 = new VictoryResult(true);
        assertTrue(result2.victory());
        assertEquals(IPlayer.PLAYER_NONE, result2.getWinningPlayer());
        assertEquals(IPlayer.TEAM_NONE, result2.getWinningTeam());
        assertTrue(result2.isDraw());


        // Constructor 2
        VictoryResult result3 = new VictoryResult(true, IPlayer.PLAYER_NONE, IPlayer.TEAM_NONE);
        assertTrue(result3.victory());
        assertEquals(IPlayer.PLAYER_NONE, result3.getWinningPlayer());
        assertEquals(IPlayer.TEAM_NONE, result3.getWinningTeam());
        assertTrue(result3.isDraw());

        VictoryResult result4 = new VictoryResult(false, IPlayer.PLAYER_NONE, IPlayer.TEAM_NONE);
        assertFalse(result4.victory());
        assertEquals(IPlayer.PLAYER_NONE, result4.getWinningPlayer());
        assertEquals(IPlayer.TEAM_NONE, result4.getWinningTeam());
        assertTrue(result4.isDraw());

        VictoryResult result5 = new VictoryResult(true, 5, IPlayer.TEAM_NONE);
        assertTrue(result5.victory());
        assertEquals(5, result5.getWinningPlayer());
        assertEquals(IPlayer.TEAM_NONE, result5.getWinningTeam());
        assertFalse(result5.isDraw());

        VictoryResult result6 = new VictoryResult(true, IPlayer.PLAYER_NONE, 6);
        assertTrue(result6.victory());
        assertEquals(IPlayer.PLAYER_NONE, result6.getWinningPlayer());
        assertEquals(6, result6.getWinningTeam());
        assertFalse(result6.isDraw());

        VictoryResult result7 = new VictoryResult(true, 7, 8);
        assertTrue(result7.victory());
        assertEquals(7, result7.getWinningPlayer());
        assertEquals(8, result7.getWinningTeam());
        assertFalse(result7.isDraw());

        // Constructor 3
        VictoryResult result8 = VictoryResult.noResult();
        assertFalse(result8.victory());
        assertTrue(result8.isDraw());

        VictoryResult result9 = VictoryResult.drawResult();
        assertTrue(result9.victory());
        assertTrue(result9.isDraw());
    }

    @Test
    public void testGetWinningPlayer() {
        // Trivial case: no players
        VictoryResult testResult = new VictoryResult(false);
        assertSame(Player.PLAYER_NONE, testResult.getWinningPlayer());

        // Case with two players
        int winningPlayer = 0;
        int losingPlayer = 1;

        testResult.addPlayerScore(winningPlayer, 100);
        testResult.addPlayerScore(losingPlayer, 40);

        assertSame(winningPlayer, testResult.getWinningPlayer());

        // Case with three players and a draw
        int secondWinningPlayer = 2;

        testResult.addPlayerScore(secondWinningPlayer, 100);
        assertNotSame(secondWinningPlayer, testResult.getWinningPlayer());
        assertNotSame(winningPlayer, testResult.getWinningPlayer());
        assertSame(Player.PLAYER_NONE, testResult.getWinningPlayer());

    }

    @Test
    public void testGetWinningTeam() {
        // Trivial case: no team
        VictoryResult testResult = new VictoryResult(false);
        assertSame(Player.TEAM_NONE, testResult.getWinningTeam());

        // Case with two teams
        int winningTeam = 1;
        int losingTeam = 2;

        testResult.addTeamScore(winningTeam, 100);
        testResult.addTeamScore(losingTeam, 40);

        assertSame(winningTeam, testResult.getWinningTeam());

        // Case with three teams and a draw
        int secondWinningTeam = 3;

        testResult.addTeamScore(secondWinningTeam, 100);
        assertNotSame(secondWinningTeam, testResult.getWinningTeam());
        assertNotSame(winningTeam, testResult.getWinningTeam());
        assertSame(Player.TEAM_NONE, testResult.getWinningTeam());
    }



    @Test
    public void testGetPlayerScoreNull() {
        VictoryResult victoryResult = new VictoryResult(true);
        assertEquals(0.0, victoryResult.getPlayerScore(1), 0.0);
    }

    @Test
    public void testGetPlayerScore() {
        VictoryResult victoryResult = new VictoryResult(true);
        victoryResult.addPlayerScore(1, 3);
        assertEquals(3.0, victoryResult.getPlayerScore(1), 0.0);

        victoryResult.addPlayerScore(1, 2);
        assertEquals(2.0, victoryResult.getPlayerScore(1), 0.0);

        victoryResult.addPlayerScore(2, 6);
        assertEquals(6.0, victoryResult.getPlayerScore(2), 0.0);
    }

    @Test
    public void testUpdateHiScore_Player() {
        VictoryResult victoryResult = new VictoryResult(false);
        victoryResult.addPlayerScore(1, 1);
        victoryResult.addPlayerScore(2, 2);
        victoryResult.addPlayerScore(3, 1);

        assertTrue(victoryResult.isWinningPlayer(2));
        assertFalse(victoryResult.isWinningPlayer(1));
        assertFalse(victoryResult.isWinningPlayer(3));
    }

    @Test
    public void testUpdateHiScore_Team() {
        VictoryResult victoryResult = new VictoryResult(false);
        victoryResult.addTeamScore(1, 1);
        victoryResult.addTeamScore(2, 2);
        victoryResult.addTeamScore(3, 1);

        assertTrue(victoryResult.isWinningTeam(2));
        assertFalse(victoryResult.isWinningTeam(1));
        assertFalse(victoryResult.isWinningTeam(3));;
    }

    @Test
    public void testVictory(){
        VictoryResult victoryResult = new VictoryResult(true);
        assertTrue(victoryResult.victory());
        victoryResult.setVictory(false);
        assertFalse(victoryResult.victory());

        victoryResult = new VictoryResult(false);
        assertFalse(victoryResult.victory());
        victoryResult.setVictory(true);
        assertTrue(victoryResult.victory());
    }

    @Test
    public void testAddPlayerScore() {
        VictoryResult victoryResult = new VictoryResult(true);
        victoryResult.addPlayerScore(1, 3);

        assertEquals(1, victoryResult.getPlayers().length);
        assertEquals(3.0, victoryResult.getPlayerScore(1), 0.0);
        assertEquals(0.0 , victoryResult.getPlayerScore(2), 0.0);
    }

    @Test
    public void testAddTeamScore() {
        VictoryResult victoryResult = new VictoryResult(true);
        victoryResult.addTeamScore(1, 3);

        assertEquals(1, victoryResult.getTeams().length);
        assertEquals(3.0, victoryResult.getTeamScore(1), 0.0);
        assertEquals(0.0 , victoryResult.getTeamScore(2), 0.0);
    }

    @Test
    public void testGetPlayers() {
        VictoryResult victoryResult = new VictoryResult(true);
        victoryResult.addPlayerScore(1, 3);
        victoryResult.addPlayerScore(2, 3);

        assertEquals(1, victoryResult.getPlayers()[0]);
        assertEquals(2, victoryResult.getPlayers()[1]);
    }

    @Test
    public void testReports(){
        VictoryResult victoryResult = new VictoryResult(true);
        Report report1 = new Report();
        Report report2 = new Report();
        victoryResult.addReport(report1);
        assertEquals(1, victoryResult.getReports().size());
        victoryResult.addReport(report2);
        assertEquals(2, victoryResult.getReports().size());

        String a = victoryResult.toString();
        String b = victoryResult.toString();

    }
}