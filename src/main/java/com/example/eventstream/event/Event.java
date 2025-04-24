package com.example.eventstream.event;

import java.time.Instant;

public record Event(String id, String message, Instant timestamp) {
}