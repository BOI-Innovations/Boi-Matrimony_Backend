package com.matrimony.util;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator {

	private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
	private static final String DIGITS = "0123456789";
	private static final String SPECIAL = "!@#$%^&*()-_=+[]{}|;:,.<>?";
	private static final String ALL_CHARS = UPPER + LOWER + DIGITS + SPECIAL;

	private final BCryptPasswordEncoder passwordEncoder;
	private final Random random;

	public PasswordGenerator() {
		this.passwordEncoder = new BCryptPasswordEncoder();
		this.random = new SecureRandom();
	}

	public String generateRandomPassword(int length) {
		if (length < 8) {
			length = 8; // Minimum password length
		}

		StringBuilder password = new StringBuilder(length);

		// Ensure at least one character from each category
		password.append(UPPER.charAt(random.nextInt(UPPER.length())));
		password.append(LOWER.charAt(random.nextInt(LOWER.length())));
		password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
		password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

		// Fill the rest with random characters
		for (int i = 4; i < length; i++) {
			password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
		}

		// Shuffle the password characters
		return shuffleString(password.toString());
	}

	public String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	public boolean matches(String rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	public String generateTemporaryPassword() {
		return generateRandomPassword(12);
	}

	public String generateReadablePassword() {
		String[] adjectives = { "Happy", "Sunny", "Bright", "Clever", "Brave", "Gentle" };
		String[] nouns = { "Tiger", "Eagle", "River", "Mountain", "Star", "Ocean" };
		String[] numbers = { "123", "456", "789", "2024", "100", "999" };

		String adjective = adjectives[random.nextInt(adjectives.length)];
		String noun = nouns[random.nextInt(nouns.length)];
		String number = numbers[random.nextInt(numbers.length)];

		return adjective + noun + number + "!";
	}

	private String shuffleString(String input) {
		char[] characters = input.toCharArray();
		for (int i = 0; i < characters.length; i++) {
			int randomIndex = random.nextInt(characters.length);
			char temp = characters[i];
			characters[i] = characters[randomIndex];
			characters[randomIndex] = temp;
		}
		return new String(characters);
	}

	public int getPasswordStrength(String password) {
		int strength = 0;

		if (password.length() >= 8)
			strength += 20;
		if (password.length() >= 12)
			strength += 20;

		if (password.matches(".*[A-Z].*"))
			strength += 20;
		if (password.matches(".*[a-z].*"))
			strength += 20;
		if (password.matches(".*\\d.*"))
			strength += 20;
		if (password.matches(".*[!@#$%^&*()\\-_=+\\[\\]{}|;:,.<>?].*"))
			strength += 20;

		return Math.min(strength, 100);
	}

	public String getPasswordStrengthLevel(String password) {
		int strength = getPasswordStrength(password);

		if (strength >= 80)
			return "Very Strong";
		if (strength >= 60)
			return "Strong";
		if (strength >= 40)
			return "Medium";
		if (strength >= 20)
			return "Weak";
		return "Very Weak";
	}
}