package com.example.eventstream.graphql;

import com.example.eventstream.event.Event;
import com.example.eventstream.event.EventService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.List;

@Controller
public class EventGraphQLController {

    private final EventService eventService;

    public EventGraphQLController(EventService eventService) {
        this.eventService = eventService;
    }

    @MutationMapping
    public Event createEvent(@Argument String message) {
        return eventService.createEvent(message);
    }

    @SubscriptionMapping
    public Flux<Event> eventCreated() {
        return eventService.getEventsFlux();
    }

    @QueryMapping
    public List<Event> recentEvents(@Argument int limit) {
        return eventService.recentEvents(limit);
    }

    @QueryMapping
    public Event event(@Argument String id) {
        return eventService.findById(id);
    }
}
