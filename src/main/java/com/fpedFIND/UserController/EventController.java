package com.fpedFIND.UserController;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fpedFIND.DTO.CalendarEventsDTO;
import com.fpedFIND.Entity.CalendarEvents;
import com.fpedFIND.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private UserService service;
    

    

    // THIS IS FOR CRUD OPERATION // CREATE, READ, UPDATE, AND DELETE
//	@Cacheable(cacheNames = "allevents")
//    @GetMapping("/event/Allevents")
//    public List<CalendarEventsDTO> getAllEvents() {
//        return service.getAllEvents();
//    }
    
    
    
    // LESSEN PAYLOAD ON FETCHING EVENTS
//	@Cacheable(cacheNames = "allevents")
    @GetMapping("/event/Allevent")
    public ResponseEntity<List<CalendarEventsDTO>> getAllCalendarEvents() {
        List<CalendarEventsDTO> events = service.getAllCalendarEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
    
    @GetMapping("/events/{userId}")
    public ResponseEntity<List<CalendarEventsDTO>> getEventsByUserId(@PathVariable("userId") Integer userId) {
        List<CalendarEventsDTO> events = service.getEventsByUserId(userId);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
    
    @GetMapping("/events/myevent/{chiefId}")
    public ResponseEntity<List<CalendarEventsDTO>> getEventsByChiefId(@PathVariable("chiefId") Integer chiefId) {
        List<CalendarEventsDTO> events = service.getEventsByChiefId(chiefId);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
    

    @PostMapping("/event/addnew")
    public ResponseEntity<String> addEvent(@RequestBody CalendarEvents event) {
        // Validate event and do additional processing if needed
        // ...

        service.addEvent(event);

        return ResponseEntity.status(HttpStatus.CREATED).body("Event added successfully");
    }
    
    
    @PostMapping("/event/addnew/chief")
    public ResponseEntity<String> addEventChief(@RequestBody CalendarEvents event) {
        // Validate event and do additional processing if needed
        // ...
        service.addEventChief(event);

        return ResponseEntity.status(HttpStatus.CREATED).body("Event added successfully");
    }

    
    

    @PatchMapping("/event/{id}/edit")
    public ResponseEntity<String> updateEventFields(@PathVariable("id") Long id, @RequestBody Map<String, Object> fields) {
        // Validate fields and do additional processing if needed
        // ...

        service.updateEventFields(id, fields);

        return ResponseEntity.status(HttpStatus.OK).body("Event updated successfully");
    }

    @DeleteMapping("/event/{id}/delete")
    public ResponseEntity<String> deleteEvent(@PathVariable("id") Long id) {
        service.deleteEvent(id);
        return ResponseEntity.status(HttpStatus.OK).body("Event deleted successfully");
    }
    
    
    
    @GetMapping("/events/today-tomorrow")
    public ResponseEntity<List<CalendarEventsDTO>> getEventsForTodayAndTomorrow() {
        // Call the service method
        List<CalendarEventsDTO> eventsForTodayAndTomorrow = service.getAllEvents();

        return new ResponseEntity<>(eventsForTodayAndTomorrow, HttpStatus.OK);
    }
    
  
    //// END OF CRUD OPERATION ////
}
