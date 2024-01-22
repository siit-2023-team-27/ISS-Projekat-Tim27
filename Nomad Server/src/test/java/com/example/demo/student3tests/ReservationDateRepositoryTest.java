package com.example.demo.student3tests;

import Repositories.AccommodationRepository;
import Repositories.ReservationDateRepository;
import Repositories.ReservationRepository;
import model.*;
import model.enums.AccommodationStatus;
import model.enums.AccommodationType;
import model.enums.ConfirmationType;
import model.enums.PriceType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase()
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ReservationDateRepositoryTest {
    @Autowired
    ReservationDateRepository reservationDateRepository;

    @Autowired
    AccommodationRepository accommodationRepository;

    Accommodation accommodation;
    ReservationDate reservationDate;
    ReservationDate newResDate;
    Accommodation saved;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    @BeforeEach()
    public void setBefore() throws ParseException {
        accommodation = new Accommodation(0L, null, 2, 3, "accommodation", "description","adress", null, null, null,
                null, null, null, null, null, 200, 4, true);

        saved = accommodationRepository.save(accommodation);
        reservationDate = new ReservationDate(saved, null, 200, formatter.parse("2024-01-01"));
        newResDate = reservationDateRepository.save(reservationDate);
    }
    @Test
    public void checkingSaveOption() {
        List<ReservationDate> before = reservationDateRepository.findAll();
        ReservationDate reservationDate = new ReservationDate(null, null, 200, new Date());
        ReservationDate newResDate = reservationDateRepository.save(reservationDate);
        List<ReservationDate> after = reservationDateRepository.findAll();

        Assertions.assertNotEquals(before.size(), after.size());
        reservationDate.setId(newResDate.getId());
        Assertions.assertEquals(newResDate, reservationDate);
    }

    @Test
    public void FoundOneByAccommodationIdAndDate() {
        ReservationDate found = reservationDateRepository.findOneByAccommodation_IdAndDate(saved.getId(), newResDate.getDate());
        Assertions.assertEquals(newResDate.getId(), found.getId());
    }

    @Test
    public void FoundNull() {
        ReservationDate found = reservationDateRepository.findOneByAccommodation_IdAndDate(5L, newResDate.getDate());
        Assertions.assertNull(found);
    }
    @Test
    public void FoundNull2() throws ParseException {
        ReservationDate found = reservationDateRepository.findOneByAccommodation_IdAndDate(saved.getId(), formatter.parse("2024-01-05"));
        Assertions.assertNull(found);
    }
    @Test
    public void FoundNull3() throws ParseException {
        ReservationDate found = reservationDateRepository.findOneByAccommodation_IdAndDate(5L, formatter.parse("2024-01-01"));
        Assertions.assertNull(found);
    }

}
