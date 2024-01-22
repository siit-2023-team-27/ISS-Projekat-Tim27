package com.example.demo.student3tests;

import Repositories.ReservationDateRepository;
import Repositories.ReservationRepository;
import Services.AccommodationService;
import Services.ReservationService;
import model.*;
import model.enums.AccommodationType;
import model.enums.ReservationStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Stream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase()
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ReservationServiceTest {
    @MockBean
    AccommodationService accommodationService;
    @MockBean
    ReservationRepository reservationRepository;
    @MockBean
    ReservationDateRepository reservationDateRepository;
    @Autowired
    @InjectMocks
    private ReservationService reservationService;
    Reservation reservation;
    Accommodation accommodation;
    ReservationDate reservationDate;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    @BeforeEach()
    public void setBefore() throws ParseException {
        Date date = formatter.parse("2024-01-01");
        Date date2 = formatter.parse("2024-01-02");
        Date date3 = formatter.parse("2024-01-03");
        Date date4 = formatter.parse("2024-01-04");
        Date date5 = formatter.parse("2024-01-05");
        Date date6 = formatter.parse("2024-01-06");
        Date date7 = formatter.parse("2024-01-07");
        Date date8 = formatter.parse("2024-01-08");
        Date date9 = formatter.parse("2024-01-09");
        Date date10 = formatter.parse("2024-01-10");
        Mockito.when(accommodationService.isAvailable(1L, date))
                .thenReturn(true);
        Mockito.when(accommodationService.isAvailable(1L, date2))
                .thenReturn(true);
        Mockito.when(accommodationService.isAvailable(1L, date3))
                .thenReturn(false);
        Mockito.when(accommodationService.isAvailable(1L, date4))
                .thenReturn(false);
        Mockito.when(accommodationService.isAvailable(1L, date5))
                .thenReturn(false);
        Mockito.when(accommodationService.isAvailable(1L, date6))
                .thenReturn(true);
        Mockito.when(accommodationService.isAvailable(1L, date7))
                .thenReturn(false);
        Mockito.when(accommodationService.isAvailable(1L, date8))
                .thenReturn(false);
        Mockito.when(accommodationService.isAvailable(1L, date9))
                .thenReturn(true);
        Mockito.when(accommodationService.isAvailable(1L, date10))
                .thenReturn(true);
        reservation = new Reservation();
        reservation.setUser(new Guest());
        accommodation = new Accommodation();
        accommodation.setId(1L);
        accommodation.setName("nameForTest");
        accommodation.setAddress("adress2");
        accommodation.setDescription("description");
        accommodation.setAccommodationType(AccommodationType.ROOM);
        accommodation.setDefaultPrice(1000.0);
        accommodation.setHost(new Host());
        accommodation.setMinGuests(3);
        accommodation.setMaxGuests(4);
        reservation.setNumGuests(3);
        reservation.setAccommodation(accommodation);

        reservationDate = new ReservationDate(accommodation, null, 2000, date);

        Mockito.when(reservationDateRepository.findOneByAccommodation_IdAndDate(1L, date))
                .thenReturn(null);
        reservationDate.setDate(date2);
        Mockito.when(reservationDateRepository.findOneByAccommodation_IdAndDate(1L, date2))
                .thenReturn(null);
        reservationDate.setDate(date3);
        Mockito.when(reservationDateRepository.findOneByAccommodation_IdAndDate(1L, date3))
                .thenReturn(reservationDate);
        reservationDate.setDate(null);
        Mockito.when(reservationDateRepository.findOneByAccommodation_IdAndDate(1L, date4))
                .thenReturn(reservationDate);
        reservationDate.setDate(null);
        Mockito.when(reservationDateRepository.findOneByAccommodation_IdAndDate(1L, date5))
                .thenReturn(reservationDate);
        reservationDate.setDate(null);
        Mockito.when(reservationDateRepository.findOneByAccommodation_IdAndDate(1L, date6))
                .thenReturn(reservationDate);
        reservationDate.setDate(null);
        Mockito.when(reservationDateRepository.findOneByAccommodation_IdAndDate(1L, date7))
                .thenReturn(reservationDate);
        reservationDate.setDate(null);
        Mockito.when(reservationDateRepository.findOneByAccommodation_IdAndDate(1L, date8))
                .thenReturn(reservationDate);
        reservationDate.setDate(null);
        Mockito.when(reservationDateRepository.findOneByAccommodation_IdAndDate(1L, date9))
                .thenReturn(reservationDate);
        reservationDate.setDate(null);
        Mockito.when(reservationDateRepository.findOneByAccommodation_IdAndDate(1L, date10))
                .thenReturn(reservationDate);
    }

    @ParameterizedTest
    @MethodSource("invalidDates")
    public void accommodationNotAvailableForDates(String startDate, String endDate) {
        DateRange dateRange = new DateRange(startDate, endDate);
        reservation.setDateRange(dateRange);
        Assertions.assertFalse(reservationService.reserveAutomatically(reservation));
        Assertions.assertFalse(reservationService.reserveManually(reservation));
        verify(accommodationService, times(2)).isAvailable(reservation.getAccommodation().getId(), dateRange.getStartDate());
        verify(accommodationService, times(0)).isAvailable(reservation.getAccommodation().getId(), dateRange.getFinishDate());

    }

    @ParameterizedTest
    @MethodSource("validDates")
    public void accommodationAvailableForDatesReservationManually(String startDate, String endDate) {
        DateRange dateRange = new DateRange(startDate, endDate);
        reservation.setDateRange(dateRange);
        Assertions.assertTrue(reservationService.reserveManually(reservation));
        verify(reservationRepository, times(1)).save(reservation);
    }
    @ParameterizedTest
    @MethodSource("validDatesWithDays")
    public void accommodationAvailableForDatesReservationAutomatically(String startDate, String endDate) {
        DateRange dateRange = new DateRange(startDate, endDate);
        reservation.setDateRange(dateRange);
        Assertions.assertTrue(reservationService.reserveAutomatically(reservation));
        verify(reservationRepository, times(1)).save(reservation);
        Assertions.assertEquals(reservation.getStatus(), ReservationStatus.ACCEPTED);

    }


    private static Stream<Arguments> invalidDates()  {
        return Stream.of(
                Arguments.of("2024-01-01", "2024-01-04"),
                Arguments.of("2024-01-05", "2024-01-06"),
                Arguments.of("2024-01-07", "2024-01-09")
        );
    }
    private static Stream<Arguments> validDates() {
        return Stream.of(
                Arguments.of("2024-01-01", "2024-01-03"),
                Arguments.of("2024-01-06", "2024-01-07"),
                Arguments.of("2024-01-09", "2024-01-10")
        );
    }
    private static Stream<Arguments> validDatesWithDays() {
        return Stream.of(
                Arguments.of("2024-01-01", "2024-01-03"),
                Arguments.of("2024-01-06", "2024-01-07"),
                Arguments.of("2024-01-09", "2024-01-10")
        );
    }

}
