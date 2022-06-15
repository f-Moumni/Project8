package tourGuide.Util;

import Common.model.*;

import java.util.Date;
import java.util.UUID;

public class DataTest {


    //Users
    public static User user1 = new User(UUID.randomUUID(), "john", "123445", "john@tourguide.com");
    public static User user2 = new User(UUID.randomUUID(), "thom", "678910", "thom@tourguide.com");

    //users preferences
    public static UserPreferences userPreferences = new UserPreferences(4, 3, 2, 1);

    //attractions
    public static Attraction attraction1 = new Attraction(33.817595D, -117.922008D, "Disneyland", "Anaheim", "CA", UUID.randomUUID());
    public static Attraction attraction2 = new Attraction(43.582767D, -110.821999D, "Jackson Hole", "Jackson Hole", "WY", UUID.randomUUID());

    //locations
    public static Location location1 = new Location(33.817595D, -117.922008D);
    public static Location location2 = new Location(43.582767D, -110.821999D);

    // visited locations
    public static VisitedLocation visitedLocation1 = new VisitedLocation(UUID.randomUUID(), location1, new Date());
    public static VisitedLocation visitedLocation2 = new VisitedLocation(UUID.randomUUID(), location2, new Date());

    //users rewords
    public static UserReward userReward = new UserReward(visitedLocation1, attraction1, 22);

    //providers
    public static Provider provider1 = new Provider("Holiday Travels", 34, UUID.randomUUID());
    public static Provider provider2 = new Provider("Holiday Travels", 34, UUID.randomUUID());
}
