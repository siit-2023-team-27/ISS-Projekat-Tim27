package com.example.demo.student3tests;

import Repositories.ReservationDateRepository;
import Services.AccommodationService;
import model.*;
import model.enums.AccommodationType;
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

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase()
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class AccommodationServiceTest {
    @MockBean
    ReservationDateRepository reservationDateRepository;
    @Autowired
    @InjectMocks
    private AccommodationService accommodationService;
    Reservation reservation;
    Accommodation accommodation;
    @BeforeEach()
    public void setBefore() {
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
    }

    @ParameterizedTest
    @MethodSource("dates")
    public void isAvailableTrue(Date date) {
        Mockito.when(reservationDateRepository.findByAccommodation_IdAndDate(1L, date))
                .thenReturn(null);
        Assertions.assertTrue(accommodationService.isAvailable(1L, date));
    }

    @ParameterizedTest
    @MethodSource("dates")
    public void isAvailableTrue2(Date date) {
        ReservationDate reservationDate = new ReservationDate(accommodation, null, 2000, date);
        Mockito.when(reservationDateRepository.findByAccommodation_IdAndDate(1L, date))
                .thenReturn(reservationDate);
        Assertions.assertTrue(accommodationService.isAvailable(1L, date));
    }
    @ParameterizedTest
    @MethodSource("dates")
    public void isAvailableFalse(Date date) {
        ReservationDate reservationDate = new ReservationDate(accommodation, reservation, 2000, date);
        Mockito.when(reservationDateRepository.findByAccommodation_IdAndDate(1L, date))
                .thenReturn(reservationDate);
        Assertions.assertFalse(accommodationService.isAvailable(1L, date));
    }

    private static Stream<Arguments> dates() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return Stream.of(
                Arguments.of(formatter.parse("2024-01-01")),
                Arguments.of(formatter.parse("2024-01-02")),
                Arguments.of(formatter.parse("2024-01-03")),
                Arguments.of(formatter.parse("2024-01-04"))

        );
    }
}
