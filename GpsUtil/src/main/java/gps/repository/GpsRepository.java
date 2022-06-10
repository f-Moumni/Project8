package gps.repository;



import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GpsRepository {

    private final List<Attraction> attractions = new ArrayList<>();
    private final GpsUtil          gpsUtil     = new GpsUtil();


    public void loadAttractions(List<Attraction> attractions) {
        this.attractions.clear();
        this.attractions.addAll(gpsUtil.getAttractions());
    }

    public List<Attraction> getAttractions() {

        return attractions;
    }

    public boolean addAttraction(Attraction attraction) {

        return attractions.add(attraction);

    }

}
