package com.example.demo.student3tests;

import DTO.ReservationDTO;
import Repositories.AccommodationRepository;
import Repositories.ReservationDateRepository;
import Repositories.ReservationRepository;
import Services.AccommodationService;
import Services.ReservationService;
import Services.UserService;
import jakarta.transaction.Transactional;
import model.*;
import model.enums.ConfirmationType;
import model.enums.ReservationStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@ActiveProfiles("test")
@AutoConfigureTestDatabase()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationControllerTest {
//    createReservation
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;
    @Autowired
    private AccommodationService accommodationService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationDateRepository reservationDateRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    private static String token = "";
    private Guest guest;
    private Host host;
    private Accommodation accommodation;
    private Accommodation accommodation2;
    private Reservation reservation;

    public void login() throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject user = new JSONObject();
        user.put("username", "guest@gmail.com");
        user.put("password",  "pass");

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
        host = new Host();
        host.setUsername("host@gmail.com");
        host.setPassword("hostPassword");
        host.setAddress("Test Street");
        host.setFirstName("Name");
        host.setLastName("Surname");
        host.setPhoneNumber("0694251300");
        host.setNotificationPreferences(new HashMap<>());
        userService.create(host);
        userService.confirmRegistration("host@gmail.com");

        accommodation = new Accommodation();
        accommodation.setHost(host);
        accommodation.setConfirmationType(ConfirmationType.MANUAL);
        accommodation.setMinGuests(2);
        accommodation.setMaxGuests(4);

        accommodation2 = new Accommodation();
        accommodation2.setHost(host);
        accommodation2.setConfirmationType(ConfirmationType.AUTOMATIC);
        accommodation2.setMinGuests(2);
        accommodation2.setMaxGuests(4);

        accommodation = accommodationService.createAccommodation(accommodation);
        accommodation2 = accommodationService.createAccommodation(accommodation2);
    }
    public void register() throws JSONException {
        guest = new Guest();
        guest.setUsername("guest@gmail.com");
        guest.setPassword("pass");
        guest.setAddress("adress");
        guest.setFirstName("jane");
        guest.setLastName("doe");
        guest.setPhoneNumber("089778798");
        guest.setNotificationPreferences(new HashMap<>());
        guest = userService.createUser(guest);
        userService.confirmRegistration("guest@gmail.com");
    }
    @BeforeAll
    public void setupUser() throws JSONException, ParseException {
        register();
        login();
        addAccommodation();
    }

    private static HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }
    private static JSONObject getReservationDTO(Long id, Long guestId,Long accommodationId, String from, String to, int peopleNum, ReservationStatus reservationStatus) throws JSONException {
        JSONObject reservation = new JSONObject();

        reservation.put("id", id);
        reservation.put("user",guestId);
        reservation.put("accommodation",  accommodationId);
        reservation.put("startDate", from);
        reservation.put("finishDate", to);
        reservation.put("numGuests", peopleNum);
        reservation.put("status",  reservationStatus);
        return reservation;
    }

    @Test
    @DisplayName("Should return BAD_REQUEST, beacause guest is null")
    @WithMockUser(roles = {"GUEST"}, username = "guest@gmail.com")
    public void notValidReservation1() throws JSONException, ParseException {
        JSONObject reservationDTO = getReservationDTO(1L, 0L, accommodation.getId(), "2024-01-01", "2024-01-02", 3, ReservationStatus.PENDING);
        HttpEntity<String> request =
                new HttpEntity<String>(reservationDTO.toString(), getHeaders());
        ResponseEntity<ReservationDTO> responseEntity = restTemplate.exchange("/api/reservations",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<ReservationDTO>() {
                });

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should return BAD_REQUEST, because accommodation is null")
    @WithMockUser(roles = {"GUEST"}, username = "guest@gmail.com")
    public void notValidReservation2() throws JSONException, ParseException {
        JSONObject reservationDTO = getReservationDTO(1L, guest.getId(), 0L, "2024-01-01", "2024-01-02", 3, ReservationStatus.PENDING);
        HttpEntity<String> request =
                new HttpEntity<String>(reservationDTO.toString(), getHeaders());
        ResponseEntity<ReservationDTO> responseEntity = restTemplate.exchange("/api/reservations",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<ReservationDTO>() {
                });

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should return BAD_REQUEST, because people num is too small")
    @WithMockUser(roles = {"GUEST"}, username = "guest@gmail.com")
    public void notValidReservation3() throws JSONException, ParseException {
        JSONObject reservationDTO = getReservationDTO(1L, guest.getId(), accommodation.getId(), "2024-01-01","2024-01-02", 1, ReservationStatus.PENDING);
        HttpEntity<String> request =
                new HttpEntity<String>(reservationDTO.toString(), getHeaders());
        ResponseEntity<ReservationDTO> responseEntity = restTemplate.exchange("/api/reservations",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<ReservationDTO>() {
                });

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    @Test
    @DisplayName("Should return BAD_REQUEST, because peopleNum is to large")
    @WithMockUser(roles = {"GUEST"}, username = "guest@gmail.com")
    public void notValidReservation4() throws JSONException, ParseException {
        JSONObject reservationDTO = getReservationDTO(1L, guest.getId(), accommodation.getId(), "2024-01-01", "2024-01-02", 8, ReservationStatus.PENDING);
        HttpEntity<String> request =
                new HttpEntity<String>(reservationDTO.toString(), getHeaders());
        ResponseEntity<ReservationDTO> responseEntity = restTemplate.exchange("/api/reservations",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<ReservationDTO>() {
                });

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    @ParameterizedTest
    @DisplayName("Should return BAD_REQUEST, because dates are invalid")
    @MethodSource("invalidDates")
    @WithMockUser(roles = {"GUEST"}, username = "guest@gmail.com")
    public void notValidReservation5(String from, String to) throws JSONException, ParseException {
        JSONObject reservationDTO = getReservationDTO(1L, guest.getId(), accommodation.getId(), from, to, 3, ReservationStatus.PENDING);
        HttpEntity<String> request =
                new HttpEntity<String>(reservationDTO.toString(), getHeaders());
        ResponseEntity<ReservationDTO> responseEntity = restTemplate.exchange("/api/reservations",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<ReservationDTO>() {
                });

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should return CREATED - manual acception of reservation")
    @WithMockUser(roles = {"GUEST"}, username = "guest@gmail.com")
    public void validReservation() throws JSONException, ParseException {
        JSONObject reservationDTO = getReservationDTO(1L, guest.getId(), accommodation.getId(), "2024-01-01", "2024-01-02", 3, ReservationStatus.PENDING);
        HttpEntity<String> request =
                new HttpEntity<String>(reservationDTO.toString(), getHeaders());
        ResponseEntity<ReservationDTO> responseEntity = restTemplate.exchange("/api/reservations",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<ReservationDTO>() {
                });
        ReservationDTO reservation = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(reservation.getStatus(), ReservationStatus.PENDING);
    }
    @Test
    @DisplayName("Should return CREATED - automatic acception of reservation")
    @WithMockUser(roles = {"GUEST"}, username = "guest@gmail.com")
    public void validReservation2() throws JSONException, ParseException {
        JSONObject reservationDTO = getReservationDTO(1L, guest.getId(), accommodation2.getId(), "2024-01-01", "2024-01-02", 3, ReservationStatus.PENDING);
        HttpEntity<String> request =
                new HttpEntity<String>(reservationDTO.toString(), getHeaders());
        ResponseEntity<ReservationDTO> responseEntity = restTemplate.exchange("/api/reservations",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<ReservationDTO>() {
                });
        ReservationDTO reservation = responseEntity.getBody();
        List<ReservationDate> reservations = reservationDateRepository.findAll();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(reservation.getStatus(), ReservationStatus.ACCEPTED);
    }
    @Test
    @DisplayName("Should return FORBIDDEN because accommodation is not free for entered dates")
    @WithMockUser(roles = {"GUEST"}, username = "guest@gmail.com")
    public void notValidAutomatic() throws JSONException, ParseException {
        JSONObject reservationDTO = getReservationDTO(1L, guest.getId(), accommodation2.getId(), "2024-01-05", "2024-01-08", 3, ReservationStatus.PENDING);
        HttpEntity<String> request =
                new HttpEntity<String>(reservationDTO.toString(), getHeaders());
        ResponseEntity<ReservationDTO> responseEntity = restTemplate.exchange("/api/reservations",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<ReservationDTO>() {
                });

        JSONObject reservationDTO1 = getReservationDTO(1L, guest.getId(), accommodation2.getId(), "2024-01-06", "2024-01-08", 3, ReservationStatus.PENDING);
        HttpEntity<String> request1 =
                new HttpEntity<String>(reservationDTO1.toString(), getHeaders());
        ResponseEntity<ReservationDTO> responseEntity1 = restTemplate.exchange("/api/reservations",
                HttpMethod.POST,
                request1,
                new ParameterizedTypeReference<ReservationDTO>() {
                });
        ReservationDTO reservation = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        ReservationDTO reservation1 = responseEntity1.getBody();
        assertEquals(HttpStatus.FORBIDDEN, responseEntity1.getStatusCode());
    }
    @Test
    @DisplayName("Should return FORBIDDEN because accommodation is not free for entered dates")
    @WithMockUser(roles = {"GUEST"}, username = "guest@gmail.com")
    public void notValidAutomatic2() throws JSONException, ParseException {
        JSONObject reservationDTO = getReservationDTO(1L, guest.getId(), accommodation2.getId(), "2024-01-12", "2024-01-13", 3, ReservationStatus.PENDING);
        HttpEntity<String> request =
                new HttpEntity<String>(reservationDTO.toString(), getHeaders());
        ResponseEntity<ReservationDTO> responseEntity = restTemplate.exchange("/api/reservations",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<ReservationDTO>() {
                });

        JSONObject reservationDTO1 = getReservationDTO(1L, guest.getId(), accommodation2.getId(), "2024-01-10", "2024-01-13", 3, ReservationStatus.PENDING);
        HttpEntity<String> request1 =
                new HttpEntity<String>(reservationDTO1.toString(), getHeaders());
        ResponseEntity<ReservationDTO> responseEntity1 = restTemplate.exchange("/api/reservations",
                HttpMethod.POST,
                request1,
                new ParameterizedTypeReference<ReservationDTO>() {
                });
        ReservationDTO reservation = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        ReservationDTO reservation1 = responseEntity1.getBody();
        assertEquals(HttpStatus.FORBIDDEN, responseEntity1.getStatusCode());
    }
    @Test
    @DisplayName("Should return CREATED ")
    @WithMockUser(roles = {"GUEST"}, username = "guest@gmail.com")
    public void notValidAutomatic3() throws JSONException, ParseException {
        JSONObject reservationDTO = getReservationDTO(1L, guest.getId(), accommodation2.getId(), "2024-01-15", "2024-01-18", 3, ReservationStatus.PENDING);
        HttpEntity<String> request =
                new HttpEntity<String>(reservationDTO.toString(), getHeaders());
        ResponseEntity<ReservationDTO> responseEntity = restTemplate.exchange("/api/reservations",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<ReservationDTO>() {
                });

        JSONObject reservationDTO1 = getReservationDTO(1L, guest.getId(), accommodation2.getId(), "2024-01-18", "2024-01-19", 3, ReservationStatus.PENDING);
        HttpEntity<String> request1 =
                new HttpEntity<String>(reservationDTO1.toString(), getHeaders());
        ResponseEntity<ReservationDTO> responseEntity1 = restTemplate.exchange("/api/reservations",
                HttpMethod.POST,
                request1,
                new ParameterizedTypeReference<ReservationDTO>() {
                });
        ReservationDTO reservation = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        ReservationDTO reservation1 = responseEntity1.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity1.getStatusCode());
    }
    @Test
    @DisplayName("Should return FORBIDDEN because selected dates are taken - manual acception of reservation")
    @WithMockUser(roles = {"GUEST"}, username = "guest@gmail.com")
    public void invalidReservation() throws JSONException, ParseException {
        accommodation.setConfirmationType(ConfirmationType.AUTOMATIC);
        accommodationService.createAccommodation(accommodation);
        JSONObject reservationDTO1 = getReservationDTO(1L, guest.getId(), accommodation.getId(), "2024-02-15", "2024-02-18", 3, ReservationStatus.PENDING);
        HttpEntity<String> request1 =
                new HttpEntity<String>(reservationDTO1.toString(), getHeaders());
        ResponseEntity<ReservationDTO> responseEntity1 = restTemplate.exchange("/api/reservations",
                HttpMethod.POST,
                request1,
                new ParameterizedTypeReference<ReservationDTO>() {
                });
        accommodation.setConfirmationType(ConfirmationType.MANUAL);
        accommodationService.createAccommodation(accommodation);
        JSONObject reservationDTO = getReservationDTO(1L, guest.getId(), accommodation.getId(), "2024-02-14", "2024-02-16", 3, ReservationStatus.PENDING);
        HttpEntity<String> request =
                new HttpEntity<String>(reservationDTO.toString(), getHeaders());
        ResponseEntity<ReservationDTO> responseEntity = restTemplate.exchange("/api/reservations",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<ReservationDTO>() {
                });
        ReservationDTO reservation = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity1.getStatusCode());
        ReservationDTO reservation1 = responseEntity1.getBody();
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }


    private static Stream<Arguments> invalidDates() throws ParseException {
        return Stream.of(
                Arguments.of("2024-01-05", "2024-01-01"),
                Arguments.of("2023-50-10", "2024-01-01"),
                Arguments.of("2024-01-03", "2024-0-0")
        );
    }

}
