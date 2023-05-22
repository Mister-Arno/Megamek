package megamek.server.victory;

import static org.mockito.Mockito.*;

import megamek.common.Game;
import megamek.common.IGame;
import megamek.common.options.GameOptions;
import org.junit.Test;
import static org.junit.Assert.*;
import megamek.common.options.OptionsConstants;

import java.util.Map;

public class VictoryTest {

    @Test
    public void testCheckForVictory() {
        // Create a mock IGame object
        IGame mockGame1 = new Game();
        mockGame1.setForceVictory(true);
        mockGame1.setVictoryTeam(1);
        mockGame1.setVictoryPlayerId(1);

        IGame mockGame2 = new Game();


        Map<String,Object> mockContext = mock(Map.class);

        // Set up GameOptions with victory conditions
        GameOptions options = new GameOptions();
        options.getOption(OptionsConstants.VICTORY_CHECK_VICTORY).setValue(true);
        options.getOption(OptionsConstants.VICTORY_USE_BV_DESTROYED).setValue(true);
        options.getOption(OptionsConstants.VICTORY_BV_DESTROYED_PERCENT).setValue(50);

        // Create a Victory object with the GameOptions object
        Victory victory = new Victory(options);

        // Call the checkForVictory method with the mock IGame object and the mock game context
        VictoryResult result1 = victory.checkForVictory(mockGame1, mockContext);
        VictoryResult result2 = victory.checkForVictory(mockGame2, mockContext);



        // Verify the VictoryResult object
        assertNotNull(result1);
        assertNotNull(result2);
        assertTrue(result1.victory()); // In this example, we expect the victory condition to be met
        assertTrue(result2.victory()); // In this example, we expect the victory condition to be met
    }
}