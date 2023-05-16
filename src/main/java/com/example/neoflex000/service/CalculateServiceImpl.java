package com.example.neoflex000.service;

import com.example.neoflex000.module.CalculateOfHolidays;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CalculateServiceImpl implements CalculateService {

  private static final RestTemplate restTemplate = new RestTemplate();
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    @Lazy
    private final CalculateOfHolidays calculateOfHolidays;

    /*
    Метод для рассчета отпускных без точных дат. Метод принимает среднюю зарплату и дни.
     */
    @Override
    public BigDecimal calculateHolidayPay(BigDecimal averageEarnings, Integer days) {
        return averageEarnings.multiply(BigDecimal.valueOf(days));
    }

    /*
    Метод для рассчета отпускных с точными датами. Метод принимает среднюю зарплату и промежуток отпуска
    (начальный и конецчный день отпуска).
    */
    @Override
    public BigDecimal calculateHolidayPayWithPeriod(BigDecimal averageEarnings, LocalDate start, LocalDate end) {
        return averageEarnings.multiply(BigDecimal.valueOf((getPeriodOfHolidays(start, end)
                .size() - countHolidaysInPeriod(start, end))));
    }

    /*
    Метод для валидации запроса. При получение определенных парметров в запросе мы выбираем метод, который нам подходит.
     */
   public BigDecimal validateRequest(BigDecimal averageEarnings, Integer days, String start, String end) {
        if (days != null) {
            return calculateHolidayPay(averageEarnings, days);
        }
        if (start != null && end != null) {
            return calculateHolidayPayWithPeriod(averageEarnings, LocalDate.parse(start), LocalDate.parse(end));
        }
        return null;
    }

    /*
    Метод дляя получения выходных и праздничных дней. Передаем строку со значением года, далее с помощью RestTemplate
    получаем строки с датами, разделяем их и мапим в List<LocalDate>.
     */
    public List<LocalDate> getHolidaysCalendar(String year) {
       if ( calculateOfHolidays.getMapOfDates() == null) {
            String stringHolidays = restTemplate.getForObject("http://xmlcalendar.ru/data/ru/" + year + "/calendar.txt", String.class);
            List<LocalDate> datesOfHoliday = stringHolidays.lines()
                    .map(s -> LocalDate.parse(s, formatter))
                    .collect(Collectors.toList());
            Map<String, List<LocalDate>> mapOfDates = new HashMap<>();
           mapOfDates.put(year, datesOfHoliday);
           calculateOfHolidays.setMapOfDates(mapOfDates);
            return calculateOfHolidays.getMapOfDates().get(year);
       }
        return calculateOfHolidays.getMapOfDates().get(year);
    }

    /*
    Метод для определения промежутка заданных дат отпуска.
     */
    public List<LocalDate> getPeriodOfHolidays(LocalDate start, LocalDate end) {
        return Stream
                .iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .collect(Collectors.toList());
    }

    /*
    Метод для подсчета выходных/праздничных дней в периоде отпуска.
     */
    public Long countHolidaysInPeriod(LocalDate start, LocalDate end) {
        return getHolidaysCalendar(start.toString().substring(0, 4)).stream()
                .filter(getPeriodOfHolidays(start, end)::contains)
               .count();
    }
}

