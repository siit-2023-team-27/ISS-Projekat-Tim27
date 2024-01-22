package com.example.demo;

import Services.AccommodationService;
import Services.ReservationService;
import Services.UserService;
import com.example.demo.pages.GuestReservationsPage;
import com.example.demo.pages.HomePage;
import com.example.demo.pages.LoginPage;
import model.*;
import model.enums.AccommodationStatus;
import model.enums.ReservationStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Stream;

import static com.example.demo.TestBase.driver;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GuestReservationsE2ETest extends TestBase{

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    HomePage homePage;
    LoginPage loginPage;
    GuestReservationsPage reservationsPage;

    @Autowired
    UserService userService;

    @Autowired
    ReservationService reservationService;

    @Autowired
    AccommodationService accommodationService;

    Host host;
    Guest guest;
    Accommodation accommodation;

    static final String ROLE = "Developer";
    static final String HOST_USERNAME = "test@host.mail";
    static final String GUEST_USERNAME = "test@guest.mail";
    static final String PASSWORD = "testPassword123";

    private int deadline = 10;

    public void insertTestUsers(){
        host = new Host();
        host.setUsername(HOST_USERNAME);
        host.setPassword(PASSWORD);
        host.setVerified(true);
        host.setSuspended(false);
        host.setAddress("TestAddress");
        host.setFirstName("TestName");
        host.setLastName("TestSurname");
        host.setPhoneNumber("0694251300");
        host.setId(1L);
        userService.create(host);
        userService.confirmRegistration(HOST_USERNAME);

        guest = new Guest();
        guest.setUsername(GUEST_USERNAME);
        guest.setPassword(PASSWORD);
        guest.setVerified(true);
        guest.setSuspended(false);
        guest.setAddress("TestAddress");
        guest.setFirstName("TestName");
        guest.setLastName("TestSurname");
        guest.setPhoneNumber("0694251300");
        guest.setCancellationNumber(0);
        guest.setId(2L);
        userService.create(guest);
        userService.confirmRegistration(GUEST_USERNAME);

    }

    public void insertTestAccommodation(){
        accommodation = new Accommodation();
        accommodation = new Accommodation();
        accommodation.setName("TestName");
        accommodation.setAddress("TestAddress");
        accommodation.setDescription("TestDescription");
        accommodation.setStatus(AccommodationStatus.APPROVED);
        accommodation.setDeadlineForCancellation(deadline);
        accommodation.setDefaultPrice(100);
        accommodation.setMinGuests(1);
        accommodation.setMaxGuests(4);
        accommodation.setHost(host);
        accommodationService.createAccommodation(accommodation);
    }

    public void insertTestReservations() {

        Reservation reservation1 = new Reservation(1L, guest, accommodation, new DateRange("2024-02-1", "2024-02-10"), 1, ReservationStatus.ACCEPTED);
        Reservation reservation2 = new Reservation(2L, guest, accommodation, new DateRange("2024-01-30", "2024-02-10"), 1, ReservationStatus.ACCEPTED);
        Reservation reservation3 = new Reservation(3L, guest, accommodation, new DateRange("2024-01-30", "2024-02-10"), 1, ReservationStatus.CANCELED);
        Reservation reservation4 = new Reservation(4L, guest, accommodation, new DateRange("2024-01-30", "2024-02-10"), 1, ReservationStatus.ACCEPTED);

        reservationService.create(reservation1);
        reservationService.create(reservation2);
        reservationService.create(reservation3);
        reservationService.create(reservation4);
    }

    public void insertValidReservationsForCancel() {
        String startDate;
        String endDate;

        startDate = addDays(deadline+2);
        endDate = addDays(deadline + 5);
        Reservation reservation1 = new Reservation(1L, guest, accommodation, new DateRange(startDate, endDate), 1, ReservationStatus.ACCEPTED);
        reservationService.create(reservation1);

        startDate = addDays(deadline+1);
        endDate = addDays(deadline + 5);
        Reservation reservation2 = new Reservation(2L, guest, accommodation, new DateRange(startDate, endDate), 1, ReservationStatus.ACCEPTED);
        reservationService.create(reservation2);

        startDate = addDays(deadline+10);
        endDate = addDays(deadline + 15);
        Reservation reservation3 = new Reservation(3L, guest, accommodation, new DateRange(startDate, endDate), 1, ReservationStatus.ACCEPTED);
        reservationService.create(reservation3);
    }

    public void insertReservationsWithWrongStatusForCancel() {
        String startDate;
        String endDate;

        startDate = addDays(deadline);
        endDate = addDays(deadline + 5);
        Reservation reservation1 = new Reservation(4L, guest, accommodation, new DateRange(startDate, endDate), 1, ReservationStatus.REJECTED);
        Reservation reservation2 = new Reservation(5L, guest, accommodation, new DateRange("2024-02-1", "2024-02-10"), 1, ReservationStatus.PENDING);
        reservationService.create(reservation1);
        reservationService.create(reservation2);
    }

    public void insertReservationsWithWrongDateRange() {
        String startDate;
        String endDate;

        startDate = addDays(deadline-1);
        endDate = addDays(deadline + 5);
        Reservation reservation1 = new Reservation(6L, guest, accommodation, new DateRange(startDate, endDate), 1, ReservationStatus.ACCEPTED);
        reservationService.create(reservation1);

        startDate = addDays(deadline-5);
        endDate = addDays(deadline + 5);
        Reservation reservation2 = new Reservation(7L, guest, accommodation, new DateRange(startDate, endDate), 1, ReservationStatus.ACCEPTED);
        reservationService.create(reservation2);

        startDate = addDays(deadline);
        endDate = addDays(deadline + 5);
        Reservation reservation3 = new Reservation(8L, guest, accommodation, new DateRange(startDate, endDate), 1, ReservationStatus.ACCEPTED);
        reservationService.create(reservation3);
    }

    @BeforeAll
    public void setUp(){
        insertTestUsers();
        insertTestAccommodation();
        insertValidReservationsForCancel();
        insertReservationsWithWrongStatusForCancel();
        insertReservationsWithWrongDateRange();
    }

    @BeforeEach
    public void loginGuest(){
        loginPage = new LoginPage(driver);
        loginPage.login(GUEST_USERNAME, PASSWORD);

        homePage = new HomePage(driver);
        homePage.clickReservationsGuest();
        reservationsPage = new GuestReservationsPage(driver);
    }

    private static Stream<Arguments> provideValidReservationsForCancel() {
        return Stream.of(
                Arguments.of("1"),
                Arguments.of("2"),
                Arguments.of("3")
        );
    }
    @ParameterizedTest
    @MethodSource("provideValidReservationsForCancel")
    @DisplayName("guest should cancel reservation successfully")
    public void shouldCancelReservation(Long id) {
        reservationsPage.clickCancelButton(id);
        Assertions.assertTrue(reservationsPage.doesStatusMatch(id, "CANCELED"));
    }

    private static Stream<Arguments> provideReservationsWithWrongStatus() {
        return Stream.of(
                Arguments.of("4"),
                Arguments.of("5")
        );
    }
    @ParameterizedTest
    @MethodSource("provideReservationsWithWrongStatus")
    @DisplayName("guest should not cancel reservation because of wrong reservation status")
    public void shouldNotCancelReservation1(Long id) {

        Assertions.assertFalse(reservationsPage.clickCancelButton(id));
    }

    private static Stream<Arguments> provideReservationsWithWrongDateRange() {
        return Stream.of(
                Arguments.of("6"),
                Arguments.of("7"),
                Arguments.of("8")
        );
    }
    @ParameterizedTest
    @MethodSource("provideReservationsWithWrongDateRange")
    @DisplayName("guest should not cancel reservation because of deadline")
    public void shouldNotCancelReservation2(Long id) {
        reservationsPage.clickCancelButton(id);
        Assertions.assertTrue(reservationsPage.isFail());
        Assertions.assertFalse(reservationsPage.doesStatusMatchWithoutWaiting(id, "CANCELED"));

    }

    private String addDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        Date currentDatePlusDays = calendar.getTime();
        String strDate = dateFormat.format(currentDatePlusDays);
        return strDate;
    }
}

