package com.matrimony.model.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matrimony.model.enums.FoodPreference;
import com.matrimony.model.enums.Hobby;
import com.matrimony.model.enums.MusicPreference;
import com.matrimony.model.enums.Sport;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "hobbies_interests")
public class HobbiesAndInterests {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id", nullable = false)
	@JsonIgnore
	private Profile profile;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "profile_hobbies", joinColumns = @JoinColumn(name = "hobbies_interests_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "hobby")
	private Set<Hobby> hobbies = new HashSet<>();

	@Column(length = 500)
	private String otherHobbies;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "profile_music", joinColumns = @JoinColumn(name = "hobbies_interests_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "music_type")
	private Set<MusicPreference> favouriteMusic = new HashSet<>();

	@Column(length = 500)
	private String otherMusic;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "profile_sports", joinColumns = @JoinColumn(name = "hobbies_interests_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "sport")
	private Set<Sport> sports = new HashSet<>();

	@Column(length = 500)
	private String otherSports;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "profile_food", joinColumns = @JoinColumn(name = "hobbies_interests_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "food_type")
	private Set<FoodPreference> favouriteFood = new HashSet<>();

	@Column(length = 500)
	private String otherFood;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
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
