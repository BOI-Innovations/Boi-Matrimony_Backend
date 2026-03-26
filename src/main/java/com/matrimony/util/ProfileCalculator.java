package com.matrimony.util;

import org.springframework.stereotype.Component;

import com.matrimony.model.entity.EducationOccupationDetails;
import com.matrimony.model.entity.HobbiesAndInterests;
import com.matrimony.model.entity.Location;
import com.matrimony.model.entity.Profile;

@Component
public class ProfileCalculator {

	public int calculateProfileCompletionPercentage(Profile profile) {
		int totalFields = 25; // Total number of profile fields
		int completedFields = 0;

		// Basic information
		if (profile.getFirstName() != null && !profile.getFirstName().isEmpty())
			completedFields++;
		if (profile.getLastName() != null && !profile.getLastName().isEmpty())
			completedFields++;
		if (profile.getGender() != null)
			completedFields++;
		if (profile.getDateOfBirth() != null)
			completedFields++;

		// Religious information
		if (profile.getReligion() != null)
			completedFields++;
		if (profile.getCaste() != null && !profile.getCaste().isEmpty())
			completedFields++;
		if (profile.getSubCaste() != null && !profile.getSubCaste().isEmpty())
			completedFields++;

		// Personal details
		if (profile.getMaritalStatus() != null)
			completedFields++;

		// Education from EducationOccupationDetails
		if (profile.getEducationOccupationDetails() != null) {
			EducationOccupationDetails eduDetails = profile.getEducationOccupationDetails();
			if (eduDetails.getHighestEducation() != null && !eduDetails.getHighestEducation().isEmpty())
				completedFields++;
			if (eduDetails.getOccupation() != null && !eduDetails.getOccupation().isEmpty())
				completedFields++;
			if (eduDetails.getAnnualIncome() != null && !eduDetails.getAnnualIncome().isEmpty())
				completedFields++;
		}

		// Location information
		if (profile.getLocation() != null) {
			Location location = profile.getLocation();
			if (location.getCity() != null && !location.getCity().isEmpty())
				completedFields++;
			if (location.getState() != null && !location.getState().isEmpty())
				completedFields++;
			if (location.getCountry() != null && !location.getCountry().isEmpty())
				completedFields++;
		}

		// Mobile number is in User entity, skip or handle separately
		// Assuming we skip this field since it's not directly accessible
		completedFields++; // Placeholder - adjust logic if User data is available

		// Additional information
		if (profile.getAbout() != null && !profile.getAbout().isEmpty())
			completedFields++;
		if (profile.getProfilePictureUrl() != null && !profile.getProfilePictureUrl().isEmpty())
			completedFields++;

		// Family background from FamilyDetails
		if (profile.getFamilyDetails() != null && profile.getFamilyDetails().getAboutMyFamily() != null
				&& !profile.getFamilyDetails().getAboutMyFamily().isEmpty())
			completedFields++;

		// Hobbies from HobbiesAndInterests
		if (profile.getHobbiesAndInterests() != null) {
			HobbiesAndInterests hobbies = profile.getHobbiesAndInterests();
			if ((hobbies.getHobbies() != null && !hobbies.getHobbies().isEmpty())
					|| (hobbies.getOtherHobbies() != null && !hobbies.getOtherHobbies().isEmpty()))
				completedFields++;
		}

		if (profile.getDietaryHabits() != null)
			completedFields++;

		if (profile.getDrinkingHabits() != null)
			completedFields++;

		if (profile.getSmokingHabits() != null)
			completedFields++;

		// Profile images (at least one image)
		if (profile.getImages() != null && !profile.getImages().isEmpty())
			completedFields++;

		// Family details (counts as a single field)
		if (profile.getFamilyDetails() != null)
			completedFields++;

		return (completedFields * 100) / totalFields;
	}

	public int calculateAge(java.time.LocalDate birthDate) {
		if (birthDate == null) {
			return 0;
		}

		java.time.LocalDate currentDate = java.time.LocalDate.now();
		return java.time.Period.between(birthDate, currentDate).getYears();
	}

	public String getAgeGroup(int age) {
		if (age < 18)
			return "Under 18";
		if (age <= 25)
			return "18-25";
		if (age <= 35)
			return "26-35";
		if (age <= 45)
			return "36-45";
		if (age <= 55)
			return "46-55";
		return "56+";
	}

	public String getIncomeGroup(String annualIncome) {
		if (annualIncome == null || annualIncome.isEmpty())
			return "Not specified";

		try {
			// Parse income string to double
			String numericStr = annualIncome.replaceAll("[^0-9.]", "");
			if (numericStr.isEmpty()) {
				return "Not specified";
			}

			double income = Double.parseDouble(numericStr);

			if (income < 300000)
				return "Less than 3 LPA";
			if (income < 600000)
				return "3-6 LPA";
			if (income < 1000000)
				return "6-10 LPA";
			if (income < 2000000)
				return "10-20 LPA";
			if (income < 5000000)
				return "20-50 LPA";
			return "50+ LPA";
		} catch (NumberFormatException e) {
			return "Not specified";
		}
	}

	public boolean isProfileComplete(Profile profile) {
		return calculateProfileCompletionPercentage(profile) >= 80;
	}

	public String getProfileStrength(Profile profile) {
		int percentage = calculateProfileCompletionPercentage(profile);

		if (percentage >= 90)
			return "Excellent";
		if (percentage >= 75)
			return "Good";
		if (percentage >= 50)
			return "Average";
		return "Poor";
	}
}