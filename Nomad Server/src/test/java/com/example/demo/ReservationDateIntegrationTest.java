package com.example.demo;

import Controllers.AccommodationController;
import DTO.AccommodationDTO;
import Services.AccommodationService;
import Services.UserService;
import com.beust.ah.A;
import model.Accommodation;
import model.Host;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@ActiveProfiles("test")
@AutoConfigureTestDatabase()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationDateIntegrationTest {
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;
    @Autowired
    private AccommodationService accommodationService;
    private static String token = "";
    public void login() throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject user = new JSONObject();
        user.put("username", "host@gmail.com");
        user.put("password",  "hostPassword");

        HttpEntity<String> request =
                new HttpEntity<String>(user.toString(), headers);
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/auth/login",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        token = responseEntity2.getBody().split("\"")[3];
        System.out.println(responseEntity2.getStatusCode());
    }
    public void addAccommodation(){
        Accommodation accommodation = new Accommodation();
        Host host = new Host();
        host.setId(1L);
        accommodation.setHost(host);

        Accommodation accommodation2 = new Accommodation();
        accommodation2.setHost(host);

        accommodationService.createAccommodation(accommodation);
        accommodationService.createAccommodation(accommodation2);
    }
    public void register() throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject user = new JSONObject();
        user.put("username", "host@gmail.com");
        user.put("password",  "hostPassword");
        user.put("passwordConfirmation",  "hostPassword");
        user.put("roles",  List.of(1));
        user.put("address",  "Baker Street");
        user.put("firstName",  "Name");
        user.put("lastName",  "Surname");
        user.put("phoneNumber",  "0694251300");
        System.out.println(user.toString().replace("\"[1]\"", "[1]"));

        HttpEntity<String> request =
                new HttpEntity<String>(user.toString().replace("\"[1]\"", "[1]"), headers);
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/auth/signup",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        System.out.println(responseEntity2.getStatusCode());
        userService.confirmRegistration("host@gmail.com");
    }
    @BeforeAll
    public void setupUser() throws JSONException {
        register();
        login();
        addAccommodation();
    }
    private static JSONObject getDateRange(String start, String end) throws JSONException {

        JSONObject dateRange = new JSONObject();
        dateRange.put("startDate", start);
        dateRange.put("finishDate",  end);
        return dateRange;
    }
    private static HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }
    private static String getDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEE MMM dd yyyy");
        return sdf2.format(sdf.parse(date));
    }
    @Test
    @DisplayName("Should return empty list of dates")
    public void shouldReturnEmptyDates() {
        ResponseEntity<List<Date>> responseEntity = restTemplate.exchange("/api/accommodations/taken-dates/3",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Date>>() {
                });

        List<Date> dates = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, dates.size());
    }

    @Test
    @DisplayName("Should add reservation date")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void shouldAddReservationDate() throws JSONException, ParseException {

        HttpEntity<String> request =
                new HttpEntity<String>(getDateRange("2025-01-01", "2025-01-03").toString(), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/unavailable/1",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        ResponseEntity<List<Date>> responseEntity = restTemplate.exchange("/api/accommodations/taken-dates/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Date>>() {
                });

        List<Date> dates = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
        assertEquals(3, dates.size());
    }
    @Test
    @DisplayName("Should fail for non-existent accommodation")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void shouldFailForNonExistentAccommodation() throws JSONException, ParseException {

        HttpEntity<String> request =
                new HttpEntity<String>(getDateRange("2025-01-01", "2025-01-03").toString(), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/unavailable/5",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        System.out.println(responseEntity2.getStatusCode());
        ResponseEntity<List<Date>> responseEntity = restTemplate.exchange("/api/accommodations/taken-dates/5",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Date>>() {
                });

        List<Date> dates = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity2.getStatusCode());
        assertEquals(0, dates.size());
    }

    @ParameterizedTest
    @DisplayName("Should fail for invalid date range")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    @MethodSource("provideIllegalDates")
    public void shouldFailForInvalidDateRange(String startDate, String endDate) throws JSONException, ParseException {


        HttpEntity<String> request =
                new HttpEntity<String>(getDateRange(startDate, endDate).toString(), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/unavailable/2",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        System.out.println(responseEntity2.getStatusCode());
        ResponseEntity<List<Date>> responseEntity = restTemplate.exchange("/api/accommodations/taken-dates/2",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Date>>() {
                });

        List<Date> dates = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity2.getStatusCode());
        assertEquals(0, dates.size());
    }
    @Test
    @DisplayName("Should fail for non-existent accommodation")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void shouldFailMakingUnavailableForNonExistentAccommodation() throws JSONException, ParseException {


        HttpEntity<String> request =
                new HttpEntity<String>(getDateRange("2025-01-03", "2025-01-01").toString(), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/unavailable/3",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        System.out.println(responseEntity2.getStatusCode());
        ResponseEntity<List<Date>> responseEntity = restTemplate.exchange("/api/accommodations/taken-dates/3",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Date>>() {
                });

        List<Date> dates = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity2.getStatusCode());
        assertEquals(0, dates.size());
    }
    @Test
    @DisplayName("Should add price")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void shouldAddPrice() throws JSONException, ParseException {

        JSONObject resDate = getDateRange("2025-01-01", "2025-01-03");
        resDate.put("price", "50.0");
        HttpEntity<String> request =
                new HttpEntity<String>(resDate.toString(), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/price/1",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        ResponseEntity<Double> responseEntity = restTemplate.exchange("/api/accommodations/price/1/"+getDate("2025-01-02"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Double>() {
                });

        Double price = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
        assertEquals(price, 50.0);
    }
    @Test
    @DisplayName("Should should overwrite existing price")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void shouldOverwriteExistingPrice() throws JSONException, ParseException {

        JSONObject resDate = getDateRange("2025-01-01", "2025-01-03");
        resDate.put("price", "50.0");
        HttpEntity<String> request =
                new HttpEntity<String>(resDate.toString(), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/price/1",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });

        resDate.put("price", "500.0");
        request =
                new HttpEntity<String>(resDate.toString(), getHeaders());
        ResponseEntity<String> responseEntity3 = restTemplate.exchange("/api/accommodations/price/1",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        ResponseEntity<Double> responseEntity = restTemplate.exchange("/api/accommodations/price/1/"+getDate("2025-01-02"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Double>() {
                });

        Double price = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
        assertEquals(HttpStatus.OK, responseEntity3.getStatusCode());
        assertEquals(500.0, price);
    }
    @ParameterizedTest
    @MethodSource("provideIllegalDates")
    @DisplayName("Should fail for illegal date range")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void shouldFailForIllegalDateRange(String startDate, String endDate, String checkDate) throws JSONException, ParseException {

        JSONObject resDate = getDateRange(startDate, endDate);
        resDate.put("price", "50.0");
        HttpEntity<String> request =
                new HttpEntity<String>(resDate.toString(), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/price/2",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        ResponseEntity<Double> responseEntity = restTemplate.exchange("/api/accommodations/price/2/"+getDate(checkDate),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Double>() {
                });

        Double price = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity2.getStatusCode());
        assertEquals(price, 0.0);
    }

    @Test
    @DisplayName("Should fail setting price for non-existent accommodation")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void shouldFailPriceForNonExistentAccommodation() throws JSONException, ParseException {

        JSONObject resDate = getDateRange("2025-01-01", "2025-01-03");
        resDate.put("price", "50.0");
        HttpEntity<String> request =
                new HttpEntity<String>(resDate.toString(), getHeaders());
        ResponseEntity<String> responseEntity2 = restTemplate.exchange("/api/accommodations/price/3",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<String>() {
                });
        ResponseEntity<Double> responseEntity = restTemplate.exchange("/api/accommodations/price/3/"+getDate("2025-01-02"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Double>() {
                });

        Double price = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity2.getStatusCode());
        assertNull(price);
    }

    public Stream<Arguments> provideIllegalDates() {
        return Stream.of(
                Arguments.of("2025-01-03", "2025-01-01", "2025-01-02"),
                Arguments.of("2019-01-03", "2019-01-01", "2019-01-02")
        );
    }
}