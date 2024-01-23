package com.example.demo;

import Repositories.ReservationDateRepository;
import Repositories.ReservationRepository;
import Services.AccommodationService;
import Services.ReservationService;
import model.*;
import model.enums.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase()
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ReservationServiceTest {

    @Autowired
    @InjectMocks
    private ReservationService reservationService;

    @MockBean
    private ReservationRepository reservationRepository;

    @MockBean
    private ReservationDateRepository reservationDateRepository;

    @MockBean
    private AccommodationService accommodationService;

    @BeforeEach
    public void setUp() {
        Guest guest = new Guest();

    }

    static Stream<Reservation> provideInvalidReservationsForVerifying() {
        return Stream.of(
                null,
                new Reservation(null, null, null, 0, ReservationStatus.REJECTED),
                new Reservation(null, null, null, 0, ReservationStatus.CANCELED)
        );
    }
    @ParameterizedTest
    @MethodSource("provideInvalidReservationsForVerifying")
    public void testVerifyReservationWhenInvalidReservation(Reservation reservation) {
        boolean result = reservationService.verify(reservation);
        assertFalse(result);
    }

    @Test
    public void testVerifyCancelsOverlappingReservations() {
        Accommodation accommodation = new Accommodation(1L, new Host(), 1, 2, " ", " "," ", null, null, null,
                null, AccommodationStatus.APPROVED, ConfirmationType.MANUAL, AccommodationType.HOUSE, PriceType.FOR_GUEST, 100, 12, true);

        Reservation reservation = new Reservation(1L, new Guest(), accommodation, new DateRange("2024-02-03", "2024-02-10"), 1, ReservationStatus.PENDING);
        Reservation overlappingReservation1 = new Reservation(2L, new Guest(), accommodation, new DateRange("2024-01-30", "2024-02-10"), 1, ReservationStatus.PENDING);
        Reservation overlappingReservation2 = new Reservation(3L, new Guest(), accommodation, new DateRange("2024-01-29", "2024-02-03"), 1, ReservationStatus.PENDING);
        Reservation overlappingReservation3 = new Reservation(4L, new Guest(), accommodation, new DateRange("2024-01-29", "2024-02-11"), 1, ReservationStatus.PENDING);
        Reservation overlappingReservation4 = new Reservation(5L, new Guest(), accommodation, new DateRange("2024-02-6", "2024-02-13"), 1, ReservationStatus.PENDING);
        Reservation overlappingReservation5 = new Reservation(6L, new Guest(), accommodation, new DateRange("2024-02-10", "2024-02-13"), 1, ReservationStatus.PENDING);

        when(reservationRepository.findAllByAccommodation_id(anyLong())).thenReturn(
                List.of(overlappingReservation1, overlappingReservation2, overlappingReservation3, overlappingReservation4, overlappingReservation5)
        );

        when(reservationDateRepository.save(any(ReservationDate.class))).thenReturn(new ReservationDate());

        when(reservationRepository.save(overlappingReservation1)).thenReturn(overlappingReservation1);
        when(reservationRepository.save(overlappingReservation2)).thenReturn(overlappingReservation2);
        when(reservationRepository.save(overlappingReservation3)).thenReturn(overlappingReservation3);
        when(reservationRepository.save(overlappingReservation4)).thenReturn(overlappingReservation4);
        when(reservationRepository.save(overlappingReservation5)).thenReturn(overlappingReservation5);

        boolean result = reservationService.verify(reservation);
        assertTrue(result);

        assertEquals(ReservationStatus.REJECTED, overlappingReservation1.getStatus());
        assertEquals(ReservationStatus.REJECTED, overlappingReservation2.getStatus());
        assertEquals(ReservationStatus.REJECTED, overlappingReservation3.getStatus());
        assertEquals(ReservationStatus.REJECTED, overlappingReservation4.getStatus());
        assertEquals(ReservationStatus.REJECTED, overlappingReservation5.getStatus());


        verify(reservationRepository, times(6)).save(any(Reservation.class));
    }

    public void testVerifyDoesNotCancelOverlappingReservations() {

    }

    @Test
    public void testReserveAutomatically() {
        Accommodation accommodation = new Accommodation();
        accommodation.setConfirmationType(ConfirmationType.AUTOMATIC);
        Reservation reservation = new Reservation();
        reservation.setAccommodation(accommodation);
        reservation.setDateRange(new DateRange("2024-01-15", "2024-01-25"));
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(reservationDateRepository.save(any(ReservationDate.class))).thenReturn(new ReservationDate());
        when(accommodationService.isAvailable(eq(accommodation.getId()), any(Date.class))).thenReturn(true);

        reservationService.reserveAutomatically(reservation);

        assertEquals(ReservationStatus.ACCEPTED, reservation.getStatus());
    }

    @Test
    public void testReserveManually() {
        Accommodation accommodation = new Accommodation();
        accommodation.setConfirmationType(ConfirmationType.MANUAL);
        Reservation reservation = new Reservation();
        reservation.setAccommodation(accommodation);
        reservation.setDateRange(new DateRange("2024-01-15", "2024-01-25"));
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(reservationDateRepository.save(any(ReservationDate.class))).thenReturn(new ReservationDate());
        when(accommodationService.isAvailable(eq(accommodation.getId()), any(Date.class))).thenReturn(true);

        reservationService.reserveManually(reservation);

        assertEquals(ReservationStatus.PENDING, reservation.getStatus());
    }

    private static Stream<Reservation> provideNotPendingReservations() {
        return Stream.of(
                new Reservation(null, null, null, 0, ReservationStatus.CANCELED),
                new Reservation(null, null, null, 0, ReservationStatus.ACCEPTED)
        );
    }
    @ParameterizedTest
    @MethodSource("provideNotPendingReservations")
    public void testRejectingOnInvalidReservations(Reservation reservation) {
        reservationService.decline(reservation);
        assertFalse(ReservationStatus.REJECTED == reservation.getStatus());
        verify(reservationRepository, times(0)).save(any(Reservation.class));
    }

    @Test
    public void testRejectingReservationsOnValidReservations() {
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatus.PENDING);
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        reservationService.decline(reservation);
        assertTrue(ReservationStatus.REJECTED == reservation.getStatus());
        verify(reservationRepository, times(1)).save(reservation);
    }

    static Stream<Reservation> provideReservationsInPast() {
        String startDate1;
        String startDate2;
        String startDate3;

        String endDate;

        startDate1 = addDays(-1);
        startDate2 = addDays(-2);
        startDate3 = addDays(0);
        endDate = addDays(3);
        Reservation reservation1 = new Reservation(null, null, new DateRange(startDate1, endDate), 0, ReservationStatus.REJECTED);
        Reservation reservation2 = new Reservation(null, null, new DateRange(startDate2, endDate), 0, ReservationStatus.REJECTED);
        Reservation reservation3 = new Reservation(null, null, new DateRange(startDate3, endDate), 0, ReservationStatus.REJECTED);
        return Stream.of(
                reservation1, reservation2, reservation3
        );
    }

    @ParameterizedTest
    @MethodSource("provideReservationsInPast")
    public void testVerifyReservationWhenReservationsInPast(Reservation reservation) {
        boolean result = reservationService.verify(reservation);
        assertFalse(result);
    }

    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static String addDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        Date currentDatePlusDays = calendar.getTime();
        String strDate = dateFormat.format(currentDatePlusDays);
        return strDate;
    }

}
