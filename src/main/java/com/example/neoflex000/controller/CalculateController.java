package com.example.neoflex000.controller;

import com.example.neoflex000.service.CalculateServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController

@RequiredArgsConstructor

public class CalculateController {
    @Autowired
    CalculateServiceImpl calculateService;
    private final CalculateServiceImpl calculateServiceImpl;

    @GetMapping("/calculate")
    public BigDecimal get(@RequestParam("averageEarnings") BigDecimal averageEarnings,
                      @RequestParam(value = "days", required = false) Integer days,
                      @RequestParam(value = "start", required = false) String start,
                      @RequestParam(value = "end", required = false) String end) {
        return calculateServiceImpl.validateRequest(averageEarnings, days, start, end);
    }
}

