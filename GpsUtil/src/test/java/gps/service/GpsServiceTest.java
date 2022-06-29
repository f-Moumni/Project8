package gps.service;

import gps.repository.GpsRepository;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class GpsServiceTest {

    @MockBean
    private GpsRepository gpsRepository;
    @MockBean
    GpsUtil gpsUtil;

    private GpsService gpsUtilService;


    private VisitedLocation visitedLocation;
    private Location        location;
    private Attraction attraction;

    @BeforeEach
    void setUp() {
        gpsUtilService = new GpsService(gpsRepository);
        location         = new Location(33.817595D, -117.922008D);
        visitedLocation  = new VisitedLocation(UUID.randomUUID(), location, new Date());
        attraction = new Attraction("Disneyland", "Anaheim", "CA",33.817595D, -117.922008D);
        Locale.setDefault(new Locale.Builder().setLanguage("en").setRegion("US").build());
    }

    @Test
    void getAttractionsTest() {
        //Arrange
        when(gpsRepository.getAttractions()).thenReturn(List.of(attraction, attraction));
        //Act
        List<Attraction> result = gpsUtilService.getAttractions();
        //Assert
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void getAttractionsTest_withEmptyList() {
        //Arrange
        when(gpsRepository.getAttractions()).thenReturn(new ArrayList<Attraction>());
        //Act
        List<Attraction> result = gpsUtilService.getAttractions();
        //Assert
        assertThat(result.size()).isZero();
    }

    @Test
    void getUserLocationTest() {
        //Arrange
        UUID userId = UUID.randomUUID();
        when(gpsUtil.getUserLocation(userId)).thenReturn(visitedLocation);
        //Act
        VisitedLocation result = gpsUtilService.getUserLocation(userId);
        //ASSERT
        assertThat(result.userId).isEqualTo(userId);

    }
}
