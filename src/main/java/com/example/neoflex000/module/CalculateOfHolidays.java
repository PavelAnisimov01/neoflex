package com.example.neoflex000.module;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

    @Component
    @Data
    public class CalculateOfHolidays {

        private Map<String, List<LocalDate>> mapOfDates;
    }


