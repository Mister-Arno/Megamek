package megamek.client;

import junit.framework.TestCase;
import megamek.common.Player;
import megamek.common.net.IConnection;
import megamek.common.net.Packet;
import megamek.server.Server;
import org.mockito.Mockito;

import java.io.IOException;

public class ClientTest extends TestCase {
    public void testClientConstruction() {
        String name = "Client 1";
        String host = "0.0.0.0";
        int port = 1234;

        Client client = new Client(name, host, port);

        TestCase.assertEquals(name, client.getName());
        TestCase.assertEquals(host, client.getHost());
        TestCase.assertEquals(port, client.getPort());
    }

    public void testClientConnect() throws IOException {
        String name = "TestPlayer";
        String host = "localhost";
        int port = 1234;

        Client client = new Client(name, host, port);
        assertFalse(client.connect());
        Server server = new Server("", port);
        // Connection is still pending at this point, because it takes time to handshake
        assertTrue(client.connect());
    }

    public void testClientAddPlayer() {
        String name = "TestPlayer";
        String host = "localhost";
        int port = 2345;
        Client client = new Client(name, host, port);

        // Set up player
        int playerId = 1000;
        Player p = new Player(playerId, name);

        final Object[] data = new Object[2];
        data[0] = playerId;
        data[1] = p;
        Packet playerAddPacket = new Packet(Packet.COMMAND_PLAYER_ADD, data);
        // Local player number update
        Packet lpNumberPacket = new Packet(Packet.COMMAND_LOCAL_PN, new Object[]{playerId});

        client.handlePacket(playerAddPacket);
        client.handlePacket(lpNumberPacket);
        assertEquals(playerId, client.getLocalPlayerNumber());
        assertEquals(playerId, client.getLocalPlayer().getId());
        assertEquals(name, client.getLocalPlayer().getName());
    }
}