package ru.agile.scrum.mst.market.email.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.agile.scrum.mst.market.email.services.EmailService;

@RestController
@RequestMapping("/api/v1/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public void sendEmail(@RequestParam String eAddress){
        String testMessageSubject = "testMessageSubject";
        String testMessage = "testMessage";
        emailService.sendEmail(eAddress,testMessageSubject, testMessage);
    }


}
