package gps.repository;



import gps.service.GpsService;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * gps repository
 */
@Repository
public class GpsRepository {

    private final Logger           LOGGER      = LoggerFactory.getLogger(GpsRepository.class);
    private final  List<Attraction> attractions = new ArrayList<>();

    /**
     * load attraction in attractions list
     * @param attractions to put in list
     */
    public void loadAttractions(List<Attraction> attractions) {
        LOGGER.debug("loading Attractions");
        this.attractions.clear();
        this.attractions.addAll(attractions);
    }

    /**
     * get attractions in list
     * @return all attractions in list
     */
    public List<Attraction> getAttractions() {
        LOGGER.debug("getting Attractions");
        return attractions;
    }

    /**
     * add new attraction to the list
     * @param attraction to add
     * @return true if saved
     */
    public boolean addAttraction(Attraction attraction) {
        LOGGER.debug("saving new attraction");
        return attractions.add(attraction);

    }

}
