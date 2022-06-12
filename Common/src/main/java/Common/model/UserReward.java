package Common.model;

public class UserReward {

	private final VisitedLocation visitedLocation;
	private final Attraction attraction;
	private int rewardPoints;
	public UserReward(VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
		this.visitedLocation = visitedLocation;
		this.attraction = attraction;
		this.rewardPoints = rewardPoints;
	}

	public UserReward() {

	}

	public VisitedLocation getVisitedLocation() {

		return visitedLocation;
	}

	public Attraction getAttraction() {

		return attraction;
	}

	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	
	public int getRewardPoints() {
		return rewardPoints;
	}
	
}
