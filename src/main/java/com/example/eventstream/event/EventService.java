package com.example.eventstream.event;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Instant;
import java.util.UUID;

@Service
public class EventService {

    private final Sinks.Many<Event> sink = Sinks.many().multicast().directBestEffort();

    public Event createEvent(String message) {
        Event event = new Event(UUID.randomUUID().toString(), message, Instant.now());
        sink.tryEmitNext(event);
        return event;
    }

    public Flux<Event> getEvents() {
        return sink.asFlux();
    }
}