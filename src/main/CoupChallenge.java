package main;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class represent a single coup challenge.
 * 
 * @author rclakmal
 *
 */
public class CoupChallenge {

	private JSONArray scooters;
	private Integer maxScootersForFM;
	private Integer maxScootersForFE;
	private String errorMessage;

	public CoupChallenge(String input) {
		parseInput(input);
	}

	/**
	 * This method is used to parse the input string into individual values.
	 * 
	 * @throws JSONException
	 */
	private void parseInput(String input) {
		JSONObject obj;
		try {
			if (input != null) {
				obj = new JSONObject(input);
				maxScootersForFE = obj.getInt("P");
				maxScootersForFM = obj.getInt("C");
				scooters = obj.getJSONArray("scooters");
			}
		} catch (JSONException e) {
			errorMessage = e.getMessage() + ". Invalid Input";
		}

	}

	private Double getScootersForDistrict(JSONArray scooters, int districtIndex) {
		try {
			return scooters.getDouble(districtIndex);
		} catch (JSONException e) {
			errorMessage = e.getMessage() + ". Invalid Array Value for Scooters";
			return null;
		}
	}

	public JSONObject getMinNoOfFleetEngineers() {

		int minNoOfFleetEngineers = 0;

		// If there is a problem with the parsed data, we immediate return a
		// error,
		// signaling process was aborted. Currently the invalid inputs are
		// defined as
		// * Scooter array cannot be empty
		// * Max scooters for a FE cannot be zero or negative.
		// * Max scooters for a FM cannot be negative. However it could be zero.
		if (scooters == null || scooters.length() == 0 || maxScootersForFE <= 0 || maxScootersForFM < 0) {
			errorMessage = "Invalid input data.";
			return processResult(minNoOfFleetEngineers);
		}

		// This HashMap is used to store number of fleet engineers needed for
		// each district if there was no fleet manager.
		Map<Integer, Integer> districtFleetManagerMap = new HashMap<Integer, Integer>();

		// We are initializing the HashMap with values in the first index of the
		// Scooter array
		double scootersForCurrentDistrict = getScootersForDistrict(scooters, 0);
		districtFleetManagerMap.put(0, getNumberOfFleetEngineersForDistrict(scootersForCurrentDistrict));
		int indexOfDistrictToPutManager = 0;
		int bestReductionOfFleetEngineersInDistrict = getNumberOfFEsSavedIfFleetManagePresent(
				scootersForCurrentDistrict);
		int currentReductionOfFleetEngineers = 0;

		// Afterwards, we iterate through the Scooter array to find out where we
		// can utilize the fleet manager the best. The district we can save most
		// fleet engineers, by placing fleet manager is the ideal place.
		for (int i = 1; i < scooters.length(); i++) {
			scootersForCurrentDistrict = getScootersForDistrict(scooters, i);
			districtFleetManagerMap.put(i, getNumberOfFleetEngineersForDistrict(scootersForCurrentDistrict));
			currentReductionOfFleetEngineers = getNumberOfFEsSavedIfFleetManagePresent(scootersForCurrentDistrict);
			if (currentReductionOfFleetEngineers > bestReductionOfFleetEngineersInDistrict) {
				bestReductionOfFleetEngineersInDistrict = currentReductionOfFleetEngineers;
				indexOfDistrictToPutManager = i;
			}
		}

		// Now that we have the index of the best district to place fleet
		// manager, we simply update the reduced number of fleet engineers for
		// that district.
		int currentValueOfFleetManagerDistrict = districtFleetManagerMap.get(indexOfDistrictToPutManager);
		districtFleetManagerMap.put(indexOfDistrictToPutManager,
				currentValueOfFleetManagerDistrict - bestReductionOfFleetEngineersInDistrict);

		// We then sum up all required fleet engineers for all districts.
		for (int fleetEngForDistrict : districtFleetManagerMap.values()) {
			minNoOfFleetEngineers += fleetEngForDistrict;
		}

		// Finally we output the result in the format we need
		return processResult(minNoOfFleetEngineers);
	}

	private JSONObject processResult(int sumOfFleetEngineers) {
		JSONObject finalResult = new JSONObject();
		try {
			finalResult.put("fleet_engineers", sumOfFleetEngineers);
			if (errorMessage != null && !errorMessage.isEmpty()) {
				finalResult.put("error", errorMessage);
			}
		} catch (JSONException e) {
			return null;
		}

		return finalResult;
	}

	/**
	 * Given the amount of scooters in the district this returns FEs needed.
	 * 
	 * @param districtScooterCount
	 * @return noOfFletEngineers
	 */
	private int getNumberOfFleetEngineersForDistrict(double districtScooterCount) {
		return (int) Math.ceil(districtScooterCount / maxScootersForFE);
	}

	/**
	 * Given the amount of scooters in the district this returns FEs needed if
	 * FM was in the same district.
	 * 
	 * @param districtScooterCount
	 * @return reducedFECount
	 */
	private int getNumberOfFEsSavedIfFleetManagePresent(double districtScooterCount) {
		return (int) ((Math.ceil(districtScooterCount / maxScootersForFE)
				- Math.ceil(districtScooterCount - maxScootersForFM) / maxScootersForFE));
	}

	public JSONArray getScooters() {
		return scooters;
	}

	public int getMaxScootersForFM() {
		return maxScootersForFM;
	}

	public int getMaxScootersForFE() {
		return maxScootersForFE;
	}
}
