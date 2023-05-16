package com.example.neoflex000.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CalculateService {

    BigDecimal calculateHolidayPay(BigDecimal averageEarnings, Integer days);
    BigDecimal calculateHolidayPayWithPeriod(BigDecimal averageEarnings, LocalDate start, LocalDate end);

}
