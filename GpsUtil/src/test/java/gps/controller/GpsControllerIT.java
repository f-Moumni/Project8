package gps.controller;

import gps.service.GpsService;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest
@AutoConfigureMockMvc
public class GpsControllerIT {
    @Autowired
    MockMvc mockMvc;



    private VisitedLocation visitedLocation;
    private Location        location;
    private Attraction      attraction;
    @BeforeEach
    void setUp() {

        Location location = new Location(33.817595D, -117.922008D);
        visitedLocation  = new VisitedLocation(UUID.randomUUID(), location, new Date());
        attraction = new Attraction("Disneyland", "Anaheim", "CA",33.817595D, -117.922008D);
        Locale.setDefault(new Locale.Builder().setLanguage("en").setRegion("US").build());
    }
    @Test
    void getAllAttractionsTest() throws Exception {

        mockMvc.perform(get("/gps/attractions")).andExpect(status().isOk());
    }

    @Test
    void getUserLocationTest() throws Exception {

        mockMvc.perform(get("/gps/userLocation").param("userID",String.valueOf(UUID.randomUUID()))).andExpect(status().isOk());
    }
}
