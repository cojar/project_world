package com.example.world.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;


    @GetMapping("/")
    public String calendar(){
        return "calendar_view";
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCalendar(@RequestBody Calendar calendar) {

        if (calendar.getTitle() == null || calendar.getStart_date() == null || calendar.getEnd_date() == null) {
            return ResponseEntity.badRequest().body("Invalid calendar data");
        }

        LocalDateTime startDateTime = LocalDateTime.parse(calendar.getStart_date() + "T00:00:00");
        LocalDateTime endDateTime = LocalDateTime.parse(calendar.getEnd_date() + "T00:00:00");
        calendar.setStart(startDateTime);
        calendar.setEnd(endDateTime);

        Calendar savedCalendar = calendarService.saveCalendar(calendar);
        if (savedCalendar != null) {
            return ResponseEntity.ok("Calendar data saved successfully");
        } else {
            System.out.println("gd");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving calendar data");
        }

    }








}
