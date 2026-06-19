package org.example.sigfl_backend.shared.web;

import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public liveness endpoint to confirm the REST stack is wired up.
 */
@RestController
@RequestMapping("/api")
public class PingController {

    public record Pong(String status, Instant timestamp) {
    }

    @GetMapping("/ping")
    public Pong ping() {
        return new Pong("ok", Instant.now());
    }
}
