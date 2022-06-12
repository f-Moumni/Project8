package Common.DTO;


import Common.model.Provider;
import Common.model.UserReward;
import Common.model.VisitedLocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UserDTO {
	private final UUID userId;
	private final String userName;
	private String phoneNumber;
	private String emailAddress;
	private Date                  latestLocationTimestamp;
	private List<VisitedLocation> visitedLocations = new ArrayList<>();
	private List<UserReward>      userRewards      = new ArrayList<>();
	private UserPreferencesDTO userPreferences = new UserPreferencesDTO();
	private List<Provider>     tripDeals       = new ArrayList<>();
	public UserDTO(UUID userId, String userName, String phoneNumber, String emailAddress) {
		this.userId = userId;
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}

	public UserDTO() {

	}

	public UUID getUserId() {

		return userId;
	}

	public String getUserName() {

		return userName;
	}

	public String getPhoneNumber() {

		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {

		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {

		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {

		this.emailAddress = emailAddress;
	}

	public Date getLatestLocationTimestamp() {

		return latestLocationTimestamp;
	}

	public void setLatestLocationTimestamp(Date latestLocationTimestamp) {

		this.latestLocationTimestamp = latestLocationTimestamp;
	}

	public List<VisitedLocation> getVisitedLocations() {

		return visitedLocations;
	}

	public void setVisitedLocations(List<VisitedLocation> visitedLocations) {

		this.visitedLocations = visitedLocations;
	}

	public List<UserReward> getUserRewards() {

		return userRewards;
	}

	public void setUserRewards(List<UserReward> userRewards) {

		this.userRewards = userRewards;
	}

	public UserPreferencesDTO getUserPreferences() {

		return userPreferences;
	}

	public void setUserPreferences(UserPreferencesDTO userPreferences) {

		this.userPreferences = userPreferences;
	}

	public List<Provider> getTripDeals() {

		return tripDeals;
	}

	public void setTripDeals(List<Provider> tripDeals) {

		this.tripDeals = tripDeals;
	}
}
