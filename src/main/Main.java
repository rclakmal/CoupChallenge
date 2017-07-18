package main;

import java.util.Scanner;

import org.json.JSONException;

public class Main {

	public static void main(String[] args) throws JSONException {
		Scanner inputScanner = new Scanner(System.in);
		String input;
		System.out.println("Welcome to CoupChallenge From Lakmal. To exit, type 'exit'");
		while (true) {
			System.out.println("Enter the input data:");
			input = inputScanner.nextLine();
			if ("exit".equals(input.toLowerCase())) {
				break;
			}
			CoupChallenge challenge = new CoupChallenge(input);
			System.out.println(challenge.getMinNoOfFleetEngineers());
		}

		inputScanner.close();
	}
}
