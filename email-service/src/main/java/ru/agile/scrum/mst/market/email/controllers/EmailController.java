package ru.agile.scrum.mst.market.email.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.agile.scrum.mst.market.api.StringResponse;
import ru.agile.scrum.mst.market.email.services.EmailService;

@RestController
@RequestMapping("/api/v1/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public void sendEmail(@RequestParam String eAddress){
        String testMessageSubject = "Приветствуем вас в нашем магазине!";
        String testMessage = "Приятных покупок!";
        emailService.sendEmail(eAddress,testMessageSubject, testMessage);
    }

    @PostMapping("/subscribeBackToStock")
    public void subscribeToBackToStock(@RequestParam(name = "productId")int productId, @RequestParam(name = "email") String email){
        emailService.subscribeToBackToStock(productId,email);
    }

    @GetMapping("/sendEmailBackToStock/{productId}")
    public StringResponse sendEmailBackToStock(@PathVariable int productId){
       return emailService.sendBackToStock(productId);
    }



}
