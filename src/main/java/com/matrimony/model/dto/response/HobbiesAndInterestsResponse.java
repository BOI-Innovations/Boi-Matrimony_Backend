package com.matrimony.model.dto.response;

import java.util.Set;

import com.matrimony.model.enums.FoodPreference;
import com.matrimony.model.enums.Hobby;
import com.matrimony.model.enums.MusicPreference;
import com.matrimony.model.enums.Sport;

public class HobbiesAndInterestsResponse {
	private Long id;

	private Set<Hobby> hobbies;
	private String otherHobbies;

	private Set<MusicPreference> favouriteMusic;
	private String otherMusic;

	private Set<Sport> sports;
	private String otherSports;

	private Set<FoodPreference> favouriteFood;
	private String otherFood;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Hobby> getHobbies() {
		return hobbies;
	}

	public void setHobbies(Set<Hobby> hobbies) {
		this.hobbies = hobbies;
	}

	public String getOtherHobbies() {
		return otherHobbies;
	}

	public void setOtherHobbies(String otherHobbies) {
		this.otherHobbies = otherHobbies;
	}

	public Set<MusicPreference> getFavouriteMusic() {
		return favouriteMusic;
	}

	public void setFavouriteMusic(Set<MusicPreference> favouriteMusic) {
		this.favouriteMusic = favouriteMusic;
	}

	public String getOtherMusic() {
		return otherMusic;
	}

	public void setOtherMusic(String otherMusic) {
		this.otherMusic = otherMusic;
	}

	public Set<Sport> getSports() {
		return sports;
	}

	public void setSports(Set<Sport> sports) {
		this.sports = sports;
	}

	public String getOtherSports() {
		return otherSports;
	}

	public void setOtherSports(String otherSports) {
		this.otherSports = otherSports;
	}

	public Set<FoodPreference> getFavouriteFood() {
		return favouriteFood;
	}

	public void setFavouriteFood(Set<FoodPreference> favouriteFood) {
		this.favouriteFood = favouriteFood;
	}

	public String getOtherFood() {
		return otherFood;
	}

	public void setOtherFood(String otherFood) {
		this.otherFood = otherFood;
	}

}
