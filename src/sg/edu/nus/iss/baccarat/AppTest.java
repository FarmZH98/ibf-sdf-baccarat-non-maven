package sg.edu.nus.iss.baccarat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import sg.edu.nus.iss.baccarat.server.BacarratEngine;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void FilterCardTest()
    {
        BacarratEngine gamEngine = new BacarratEngine(2);
        int cardValue = gamEngine.filter(12);
        assertEquals( 10, cardValue);
    }

    @Test
    public void playTest()
    {
        BacarratEngine gameEngine = new BacarratEngine(2);
        String gameResult = gameEngine.play("P");
        assertNotNull(gameResult);
        assertNotNull(gameEngine.getCurrentWinner());
    }
}
