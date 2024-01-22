package com.example.demo;

import Services.AccommodationService;
import Services.ReservationService;
import Services.UserService;
import model.*;
import model.enums.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@ActiveProfiles("test")
@AutoConfigureTestDatabase()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private AccommodationService accommodationService;
    @Autowired
    private UserService userService;

    private Host host;
    private static String token = "";

    private Accommodation accommodation;
    private Guest guest;

    private static HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }


    public void register() throws JSONException {

        host = new Host();
        host.setUsername("host@gmail.com");
        host.setPassword("hostPassword");
        host.setAddress("Test Street");
        host.setFirstName("NameHost");
        host.setLastName("SurnameHost");
        host.setPhoneNumber("0694251300");
        host.setNotificationPreferences(new HashMap<>());
        userService.create(host);
        userService.confirmRegistration("host@gmail.com");
    }

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

    public void setUpAccommodation() {
        accommodation = new Accommodation();
        accommodation.setName("TestName");
        accommodation.setAddress("TestAddress");
        accommodation.setDescription("TestDescription");
        accommodation.setStatus(AccommodationStatus.APPROVED);
        accommodation.setDeadlineForCancellation(10);
        accommodation.setDefaultPrice(100);
        accommodation.setMinGuests(1);
        accommodation.setMaxGuests(4);
        accommodation.setHost(host);
        accommodationService.createAccommodation(accommodation);
    }

    public void setUpGuest() {
        guest = new Guest();
        guest.setUsername("guest@gmail.com");
        guest.setPassword("guestPassword");
        guest.setAddress("Test Street");
        guest.setFirstName("NameGuest");
        guest.setLastName("SurnameGuest");
        guest.setPhoneNumber("0694251300");
        guest.setNotificationPreferences(new HashMap<>());
        userService.create(guest);
    }
    public void addReservations() {
        setUpAccommodation();
        setUpGuest();

        Reservation reservation1 = new Reservation(1L, guest, accommodation, new DateRange("2024-01-30", "2024-02-10"), 1, ReservationStatus.CANCELED);
        Reservation reservation2 = new Reservation(2L, guest, accommodation, new DateRange("2024-01-30", "2024-02-10"), 1, ReservationStatus.ACCEPTED);
        Reservation reservation3 = new Reservation(3L, guest, accommodation, new DateRange("2024-01-30", "2024-02-10"), 1, ReservationStatus.PENDING);
        Reservation reservation4 = new Reservation(4L, guest, accommodation, new DateRange("2024-01-30", "2024-02-10"), 1, ReservationStatus.REJECTED);
        Reservation reservation5 = new Reservation(5L, guest, accommodation, new DateRange("2024-01-30", "2024-02-10"), 1, ReservationStatus.PENDING);

        reservationService.create(reservation1);
        reservationService.create(reservation2);
        reservationService.create(reservation3);
        reservationService.create(reservation4);
        reservationService.create(reservation5);
    }

    @BeforeAll
    public void setupUser() throws JSONException {
        register();
        login();
        addReservations();
    }

    @Test
    @DisplayName("should return not found reservation")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void shouldReturnNotFoundReservation () {
        HttpEntity<String> request =
                new HttpEntity<String>(getHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange("/api/reservations/reject/0",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<String>() {
                });
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    private static Stream<Arguments> provideNotPendingReservations() {
        return Stream.of(
                Arguments.of("1"),
                Arguments.of("2")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNotPendingReservations")
    @DisplayName("should return bad request")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void shouldReturnBadRequest (Long id) {
        HttpEntity<String> request =
                new HttpEntity<String>(getHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange("/api/reservations/reject/" + id,
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<String>() {
                });
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("rejecting reservations should return status OK")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void testRejectingValid () {
        HttpEntity<String> request =
                new HttpEntity<String>(getHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange("/api/reservations/reject/3",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<String>() {
                });
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("confirming reservation should return not found reservation")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void confirmingReservationShouldReturnNotFoundReservation () {
        HttpEntity<String> request =
                new HttpEntity<String>(getHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange("/api/reservations/confirm/0",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<String>() {
                });
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    private static Stream<Arguments> provideInvalidReservationsForConfirming() {
        return Stream.of(
                Arguments.of("1"),
                Arguments.of("4")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidReservationsForConfirming")
    @DisplayName("confirming reservation should return bad request")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void confirmingReservationShouldReturnBadRequest (Long id) {
        HttpEntity<String> request =
                new HttpEntity<String>(getHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange("/api/reservations/confirm/" + id,
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<String>() {
                });
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("confirming reservations should return status OK")
    @WithMockUser(roles = {"HOST"}, username = "host@gmail.com")
    public void testConfirmingValid () {
        HttpEntity<String> request =
                new HttpEntity<String>(getHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange("/api/reservations/confirm/5",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<String>() {
                });
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
