package megamek.common;

import megamek.server.Server;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class RatingHandlerTest {

    @Test
    public void testAddPlayer() throws IOException {
        Server newServer = new Server("",2346);
        RatingHandler handler = new RatingHandler(newServer);
        newServer.setGame(new Game());

        IPlayer player = new Player(1, "TestPlayer");
        handler.addPlayer(player);
        assertTrue(handler.getRatings().containsKey("TestPlayer"));
        newServer.getGame().addPlayer(1,player);

        IPlayer fetchedPlayer = newServer.getGame().getPlayer("TestPlayer");

        assertEquals(1500,fetchedPlayer.getEloRating());

    }

    @Test
    public void testUpdateRatings() throws IOException {
        Server newServer = new Server("",2343);
        RatingHandler handler = new RatingHandler(newServer);
        Server server = handler.getServer();
        server.setGame(new Game());
        server.getGame().createVictoryConditions();

        IPlayer player1 = new Player(1, "TestPlayer1");
        player1.setTeam(1);
        IPlayer player2 = new Player(2, "TestPlayer2");
        player2.setAdmitsDefeat(true);
        player2.setTeam(2);
        handler.addPlayer(player1);
        handler.addPlayer(player2);
        server.getGame().addPlayer(1,player1);
        server.getGame().addPlayer(2,player2);



        server.getGame().setVictoryTeam(1);
        server.getGame().setForceVictory(true);
        server.getGame().setVictoryPlayerId(1);
        handler.updateRatings();

        assertEquals(1516, player1.getEloRating());
        assertEquals(1485, player2.getEloRating());
    }

}
