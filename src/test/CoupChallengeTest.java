package test;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import main.CoupChallenge;

/**
 * This is the test class for {@link CoupChallenge}
 * 
 * @author rclakmal
 *
 */
public class CoupChallengeTest {

	private CoupChallenge challenge;

	@Test
	public void testEmptyInput() {
		challenge = new CoupChallenge("");
		Assert.assertEquals(true, challenge.getMinNoOfFleetEngineers().has("error"));

	}

	@Test
	public void testNullInput() {
		challenge = new CoupChallenge(null);
		Assert.assertEquals(true, challenge.getMinNoOfFleetEngineers().has("error"));

	}

	@Test
	public void testUncompleteInputWithOneKeyMissing() {
		challenge = new CoupChallenge("{ C: 9, P: 5 }");
		Assert.assertEquals(true, challenge.getMinNoOfFleetEngineers().has("error"));
		challenge = new CoupChallenge("{ scooters: [11, 15, 13], P: 5 }");
		Assert.assertEquals(true, challenge.getMinNoOfFleetEngineers().has("error"));
		challenge = new CoupChallenge("{ scooters: [11, 15, 13], C: 9}");
		Assert.assertEquals(true, challenge.getMinNoOfFleetEngineers().has("error"));
	}

	@Test
	public void testRandomInput() {
		challenge = new CoupChallenge("This is a random String");
		Assert.assertEquals(true, challenge.getMinNoOfFleetEngineers().has("error"));
	}

	@Test
	public void testCorrectInputEmptyArray() {
		challenge = new CoupChallenge("{ scooters: [], C: 9, P: 5 }");
		Assert.assertEquals(true, challenge.getMinNoOfFleetEngineers().has("error"));
	}

	@Test
	public void testCorrectKeysWrongValues() {
		challenge = new CoupChallenge("{ scooters: 100, C: 9, P: 5 }");
		Assert.assertEquals(true, challenge.getMinNoOfFleetEngineers().has("error"));
	}

	@Test
	public void testZeroValues() {
		challenge = new CoupChallenge("{ scooters: [11, 15, 13], C: 0, P: 0 }");
		Assert.assertEquals(true, challenge.getMinNoOfFleetEngineers().has("error"));
	}

	@Test
	public void testCorrectRandomString() {
		challenge = new CoupChallenge("This is a random String");
		Assert.assertEquals(true, challenge.getMinNoOfFleetEngineers().has("error"));
	}

	@Test
	public void testCorrectInput1() throws JSONException {
		challenge = new CoupChallenge("{ scooters: [11, 15, 13], C: 9, P: 5 }");
		Assert.assertEquals(7, challenge.getMinNoOfFleetEngineers().getInt("fleet_engineers"));
	}

	@Test
	public void testCorrectInput2() throws JSONException {
		challenge = new CoupChallenge("{ scooters: [15, 10], C: 12, P: 5 }");
		Assert.assertEquals(3, challenge.getMinNoOfFleetEngineers().getInt("fleet_engineers"));
	}
}
