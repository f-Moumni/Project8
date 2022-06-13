package tourGuide.proxies;

import Common.model.Attraction;
import Common.model.VisitedLocation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class GpsServiceProxyIT{

    @Autowired
    GpsServiceProxy gpsServiceProxy;

    @Test
    void Test_getAttractions() {
        //Act
        List<Attraction> attractionList = gpsServiceProxy.getAttractions();
        //Assert
        assertThat(attractionList.size()).isGreaterThan(0);

    }

    @Test
    void Test_getUserLocation() {
        //Act
        VisitedLocation location  = gpsServiceProxy.getUserLocation(UUID.randomUUID());
        //Assert
        assertThat(location).isNotNull();

    }
}
