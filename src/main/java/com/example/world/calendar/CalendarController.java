package com.example.world.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;


    @GetMapping("/")
    public String calendarPage(Model model) {
        List<Calendar> eventList = calendarService.getAllEvents();
        model.addAttribute("eventList", eventList);
        return "calendar_view";
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addCalendarEvent(@RequestBody Calendar calendar) {
        Calendar savedCalendar = calendarService.saveCalendar(calendar);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("calendar", savedCalendar);
        return response;
    }



}









