package ru.agile.scrum.mst.market.email.services;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.agile.scrum.mst.market.api.ProductDto;
import ru.agile.scrum.mst.market.email.integrations.ProductServiceIntegration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.sender.email}")
    private String senderAddress;


    private final JavaMailSender mailSender;

    private final ProductServiceIntegration productServiceIntegration;

    HashMap<Integer, List<String>> backToStockSubscriberDB = new HashMap<>();

    public void subscribeToBackToStock(int productId, String eAddress){
        if(!backToStockSubscriberDB.containsKey(productId)) {
            ArrayList<String> newEmailsList = new ArrayList<>();
            newEmailsList.add(eAddress);
            backToStockSubscriberDB.put(productId,newEmailsList);
        }else {
            ArrayList<String> updateEmailsList = (ArrayList<String>) backToStockSubscriberDB.get(productId);
            updateEmailsList.add(eAddress);
            backToStockSubscriberDB.put(productId,updateEmailsList);
        }
    }

    public void sendBackToStock(int productId){
        List<String> currentSubcriberEmailList =  backToStockSubscriberDB.get(productId);
        String[] emails = currentSubcriberEmailList.toArray(new String[currentSubcriberEmailList.size()]);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderAddress);
        message.setTo(emails);
        message.setSubject("Товар поступил в продажу");
        ProductDto productDto = productServiceIntegration.findById((long) productId);
        message.setText("Товар "+ productDto.getTitle() + " снова в продаже! Успешных покупок!");
        mailSender.send(message);
    }
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderAddress);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
