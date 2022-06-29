package pricer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pricer.service.TripPricerService;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static pricer.constants.PricerConstant.TRIP_PRICER_API_KEY;

@ExtendWith(MockitoExtension.class)
public class TripPricerServiceIT {

  @Mock
  TripPricer tripPricer;
    @InjectMocks
    TripPricerService tripPricerService;
    @Test
    void getPricerTest(){
        //Arrange
        UUID    userId   = UUID.randomUUID();

     //Act
        List<Provider> providers = tripPricerService.getPricer( userId, 2, 1, 4, 44);
       // Assert
        assertThat(providers.size()).isGreaterThan(1);
    }
}
