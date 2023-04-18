package ru.agile.scrum.mst.market.email.controllers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.agile.scrum.mst.market.api.StringResponse;
import ru.agile.scrum.mst.market.email.services.EmailService;
import java.security.Principal;
@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    @PostMapping("/my")
    @ResponseStatus(HttpStatus.CREATED)
    public void subscribeToBackToStock(@RequestParam(name = "productId")int productId, Principal principal){
        emailService.subscribeToBackToStock(productId, principal.getName(), principal.toString());
    }
    @DeleteMapping("/{product-id}")
    public StringResponse sendEmailBackToStock(@PathVariable(name="product-id") int productId){
       return emailService.sendBackToStock(productId);
    }
}
