package com;

import com.dto.response.LocationResponseDto;
import com.dto.response.WeatherResponseDto;
import com.entity.Location;
import com.exception.InvalidApiRequestException;
import com.exception.WeatherApiException;
import com.service.WeatherApiClientService;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.transaction.Transactional;
import lombok.Setter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@Transactional
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@ActiveProfiles("integration")
public class WeatherApiClientTest {
    private static final Dotenv dotenv = Dotenv.load();
    private static String API_KEY;
    private static String getLocationsUrl;
    private static String getWeatherUrl;
    @Autowired
    WeatherApiClientService weatherApiClientService;
    @Autowired
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    @Value("classpath:mock/mock-weatherApiLocations-response.json")
    private Resource mockLocationsResponse;
    @Value("classpath:mock/mock-WeatherApiWeather-response.json")
    private Resource mockWeatherResponse;

    @BeforeAll
    public static void setUp() {
        API_KEY = dotenv.get("API_KEY");
        getLocationsUrl = UriComponentsBuilder.fromHttpUrl("https://api.openweathermap.org/geo/1.0/direct").queryParam("q", "Moscow").queryParam("limit", 5).queryParam("appid", API_KEY).toUriString();
        getWeatherUrl = UriComponentsBuilder.fromHttpUrl("https://api.openweathermap.org/data/2.5/weather").queryParam("lon", 37.6173).queryParam("lat", 55.7558).queryParam("appid", API_KEY).queryParam("units", "metric").toUriString();
    }

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Nested
    class getLocationsByNameTest {
        @Test
        public void shouldReturnLocations_whenServiceReceivesValidResponse() {
            mockServer.expect(ExpectedCount.once(), requestTo(getLocationsUrl)).andExpect(method(HttpMethod.GET)).andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(mockLocationsResponse));

            List<LocationResponseDto> locations = weatherApiClientService.getLocationsByName("Moscow");

            assertNotNull(locations);
            assertEquals(5, locations.size());
            assertEquals("Moscow", locations.get(0).getName());
        }

        @Test
        public void ShouldThrowInvalidApiRequestException_whenServiceReceivesStatus400() {
            mockServer.expect(ExpectedCount.once(), requestTo(getLocationsUrl)).andExpect(method(HttpMethod.GET)).andRespond(withStatus(HttpStatus.BAD_REQUEST));

            Assertions.assertThrows(InvalidApiRequestException.class, () -> weatherApiClientService.getLocationsByName("Moscow"));
        }

        @Test
        public void ShouldThrowWeatherApiException_whenServiceReceivesStatus500() {
            mockServer.expect(ExpectedCount.once(), requestTo(getLocationsUrl)).andExpect(method(HttpMethod.GET)).andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
            Assertions.assertThrows(WeatherApiException.class, () -> weatherApiClientService.getLocationsByName("Moscow"));
        }
    }

    @Nested
    class getWeatherTest {
        @Setter
        private static Location location;

        @BeforeAll
        public static void setUp() {
            location = new Location();
            location.setName("Moscow");
            location.setLongitude(BigDecimal.valueOf(37.6173));
            location.setLatitude(BigDecimal.valueOf(55.7558));
        }

        @Test
        public void shouldReturnWeatherDto_whenServiceReceivesValidResponse() {
            mockServer.expect(ExpectedCount.once(), requestTo(getWeatherUrl)).andExpect(method(HttpMethod.GET)).andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(mockWeatherResponse));
            WeatherResponseDto weatherResponseDto = weatherApiClientService.getWeatherForLocation(location);

            assertNotNull(weatherResponseDto);
        }

        @Test
        public void ShouldThrowInvalidApiRequestException_whenServiceReceivesStatus400() {
            mockServer.expect(ExpectedCount.once(), requestTo(getWeatherUrl)).andExpect(method(HttpMethod.GET)).andRespond(withStatus(HttpStatus.BAD_REQUEST));

            Assertions.assertThrows(InvalidApiRequestException.class, () -> weatherApiClientService.getWeatherForLocation(location));
        }

        @Test
        public void ShouldThrowWeatherApiException_whenServiceReceivesStatus500() {
            mockServer.expect(ExpectedCount.once(), requestTo(getWeatherUrl)).andExpect(method(HttpMethod.GET)).andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
            Assertions.assertThrows(WeatherApiException.class, () -> weatherApiClientService.getWeatherForLocation(location));
        }
    }
}
