package Common.DTO;


import Common.model.Provider;
import Common.model.UserReward;
import Common.model.VisitedLocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UserDTO {
	private  String userName;
	private String phoneNumber;
	private String emailAddress;


	public UserDTO( String userName, String phoneNumber, String emailAddress) {
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}

	public UserDTO() {

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


}
