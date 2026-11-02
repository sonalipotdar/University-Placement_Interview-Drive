package com.placement.portal.web;

import com.placement.portal.service.StatisticsService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportPageController {

    private final StatisticsService statisticsService;

    public ReportPageController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/reports-page")
    public String reports(Model model) {

        model.addAttribute(
                "statistics",
                statisticsService.getStatistics()
        );

        return "reports/statistics";
    }
}