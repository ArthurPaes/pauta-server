package com.sicredi.pauta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Component
    static class PortLogger {
        @EventListener
        public void onStartup(ServletWebServerInitializedEvent event) {
            int port = event.getWebServer().getPort();
            System.out.println(">>> Server listening on http://localhost:" + port);
        }
    }
}