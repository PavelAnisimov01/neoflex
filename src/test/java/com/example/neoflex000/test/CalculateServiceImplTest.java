package com.example.neoflex000.test;


import com.example.neoflex000.module.CalculateOfHolidays;
import com.example.neoflex000.service.CalculateServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class CalculateServiceImplTest {

    @Autowired
    private CalculateServiceImpl calculateServiceImpl;

    @Autowired
    private CalculateOfHolidays calculateOfHolidays;

    @Test
    void calculateHolidayPay() {
        Assertions.assertEquals(BigDecimal.valueOf(500),
                calculateServiceImpl.calculateHolidayPay(BigDecimal.valueOf(100), 5));
    }

    @Test
    void calculateHolidayPayWithPeriod() {
        Map<String, List<LocalDate>> mapOfDates = new HashMap<>();
        mapOfDates.put("2022", calculateServiceImpl.getHolidaysCalendar("2022"));
        calculateOfHolidays.setMapOfDates(mapOfDates);
        Assertions.assertEquals(BigDecimal.valueOf(500),
                calculateServiceImpl.calculateHolidayPayWithPeriod(BigDecimal.valueOf(100),
                        LocalDate.parse("2022-10-03"), LocalDate.parse("2022-10-09")));
    }
}
