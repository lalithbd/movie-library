package com.mcueen.movie.library.controller;

import com.mcueen.movie.library.service.impl.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private KafkaService kafkaService;

    @GetMapping("/send")
    public void sendMessage() {
        kafkaService.sendMessage("my-topic", "Test message");
    }
}
