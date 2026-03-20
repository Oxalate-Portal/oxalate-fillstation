package io.oxalate.fillstation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.oxalate.fillstation.repository.CylinderRepository;
import io.oxalate.fillstation.repository.FillEntryRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FillEntryServiceTest {

    @Mock
    private FillEntryRepository fillEntryRepository;

    @Mock
    private CylinderRepository cylinderRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private FillEntryService fillEntryService;

    private static final BigDecimal VOLUME = new BigDecimal("12.0");
    private static final BigDecimal START_PRESSURE = new BigDecimal("50.0");
    private static final BigDecimal END_PRESSURE = new BigDecimal("200.0");
    private static final BigDecimal START_O2 = new BigDecimal("21.0");
    private static final BigDecimal END_O2 = new BigDecimal("32.0");
    private static final BigDecimal START_HE = new BigDecimal("0.0");
    private static final BigDecimal END_HE = new BigDecimal("25.0");

    @Test
    void calculateGasAdded_idealGasLaw() {
        // gasAdded = volume * (endPressure - startPressure)
        // = 12 * (200 - 50) = 12 * 150 = 1800
        BigDecimal result = fillEntryService.calculateGasAdded(VOLUME, START_PRESSURE, END_PRESSURE);
        assertEquals(new BigDecimal("1800.0000"), result);
    }

    @Test
    void calculateO2Added_correctFormula() {
        // o2Added = volume * (endPressure * endO2% - startPressure * startO2%) / 100
        // = 12 * (200 * 32 - 50 * 21) / 100
        // = 12 * (6400 - 1050) / 100
        // = 12 * 5350 / 100
        // = 642
        BigDecimal result = fillEntryService.calculateO2Added(VOLUME, START_PRESSURE, END_PRESSURE, START_O2, END_O2);
        assertEquals(new BigDecimal("642.0000"), result);
    }

    @Test
    void calculateHeAdded_correctFormula() {
        // heAdded = volume * (endPressure * endHe% - startPressure * startHe%) / 100
        // = 12 * (200 * 25 - 50 * 0) / 100
        // = 12 * 5000 / 100
        // = 600
        BigDecimal result = fillEntryService.calculateHeAdded(VOLUME, START_PRESSURE, END_PRESSURE, START_HE, END_HE);
        assertEquals(new BigDecimal("600.0000"), result);
    }

    @Test
    void calculateGasAdded_whenNoPressureChange_returnsZero() {
        BigDecimal result = fillEntryService.calculateGasAdded(VOLUME, START_PRESSURE, START_PRESSURE);
        assertEquals(new BigDecimal("0.0000"), result);
    }

    @Test
    void calculateO2Added_purePressureAir_standard() {
        // Standard air: 21% O2, filling from 0 to 200 bar in a 10L cylinder
        BigDecimal volume = new BigDecimal("10.0");
        BigDecimal start = BigDecimal.ZERO;
        BigDecimal end = new BigDecimal("200.0");
        BigDecimal o2Pct = new BigDecimal("21.0");

        // = 10 * (200 * 21 - 0 * 21) / 100 = 10 * 4200 / 100 = 420
        BigDecimal result = fillEntryService.calculateO2Added(volume, start, end, o2Pct, o2Pct);
        assertEquals(new BigDecimal("420.0000"), result);
    }
}
