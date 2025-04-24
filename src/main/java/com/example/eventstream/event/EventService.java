package com.example.eventstream.event;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class EventService {
    private final Sinks.Many<Event> sink =
            Sinks.many().multicast().directBestEffort();

    private final List<Event> store = new CopyOnWriteArrayList<>();

    public Event createEvent(String message) {
        Event e = new Event(UUID.randomUUID().toString(), message, Instant.now());
        store.add(e);
        sink.tryEmitNext(e);
        return e;
    }

    /** flux для підписок */
    public Flux<Event> getEventsFlux() {
        return sink.asFlux();
    }

    /** читальні методи **/
    public List<Event> recentEvents(int limit) {
        return store.stream()
                .sorted(Comparator.comparing(Event::timestamp).reversed())
                .limit(limit)
                .toList();
    }

    public Event findById(String id) {
        return store.stream()
                .filter(ev -> ev.id().equals(id))
                .findFirst()
                .orElse(null);
    }
}
