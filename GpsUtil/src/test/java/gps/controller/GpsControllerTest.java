package gps.controller;

import gps.service.GpsService;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GpsController.class)
public class GpsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    GpsService gpsService;

    private VisitedLocation visitedLocation;
    private Location        location;
    private Attraction attraction;
    @BeforeEach
    void setUp() {

        Location location = new Location(33.817595D, -117.922008D);
        visitedLocation  = new VisitedLocation(UUID.randomUUID(), location, new Date());
        attraction = new Attraction("Disneyland", "Anaheim", "CA",33.817595D, -117.922008D);
        Locale.setDefault(new Locale.Builder().setLanguage("en").setRegion("US").build());
    }
    @Test
    void getAllAttractionsTest() throws Exception {
        when(gpsService.getAttractions()).thenReturn(List.of(attraction,attraction));
        mockMvc.perform(get("/gps/attractions")).andExpect(status().isOk());
    }

    @Test
    void getUserLocationTest() throws Exception {
        when(gpsService.getUserLocation(any())).thenReturn(visitedLocation);
        mockMvc.perform(get("/gps/userLocation").param("userID",String.valueOf(UUID.randomUUID()))).andExpect(status().isOk());
    }
}
