package com.example.demo.student3tests;

import Repositories.AccommodationRepository;
import Repositories.ReservationDateRepository;
import Repositories.ReservationRepository;
import model.*;
import model.enums.ReservationStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
public class ReservationRepositoryTest {
    @Autowired
    ReservationRepository reservationRepository;
    @Test
    public void checkingSaveOption() {
        List<Reservation> before = reservationRepository.findAll();
        Reservation reservation = new Reservation(null, null, new DateRange("2000-10-10", "2000-20-20"), 4, ReservationStatus.PENDING);
        Reservation newRes = reservationRepository.save(reservation);
        List<Reservation> after = reservationRepository.findAll();

        Assertions.assertNotEquals(before.size(), after.size());
        reservation.setId(newRes.getId());
        Assertions.assertEquals(newRes, reservation);
    }

}
