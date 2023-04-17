package ru.agile.scrum.mst.market.email.services;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.agile.scrum.mst.market.api.ProductDto;
import ru.agile.scrum.mst.market.api.StringResponse;
import ru.agile.scrum.mst.market.api.UserPersonalAccount;
import ru.agile.scrum.mst.market.email.integrations.ProductServiceIntegration;
import ru.agile.scrum.mst.market.email.integrations.UserServiceIntegration;
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
    private final UserServiceIntegration userServiceIntegration;

    HashMap<Integer, List<String>> backToStockSubscriberDB = new HashMap<>();

    public void subscribeToBackToStock(int productId, String nameuser, String tokenSecurity){
        UserPersonalAccount personalAccount = userServiceIntegration.getPersonalData(nameuser, tokenSecurity);
        if(personalAccount != null){
            String email = personalAccount.getEmail();
            if(!backToStockSubscriberDB.containsKey(productId)) {
                ArrayList<String> newEmailsList = new ArrayList<>();
                newEmailsList.add(email);
                backToStockSubscriberDB.put(productId,newEmailsList);
            }else {
                ArrayList<String> updateEmailsList = (ArrayList<String>) backToStockSubscriberDB.get(productId);
                updateEmailsList.add(email);
                backToStockSubscriberDB.put(productId,updateEmailsList);
            }
        }

    }

    public StringResponse sendBackToStock(int productId){
        List<String> currentSubcriberEmailList =  backToStockSubscriberDB.get(productId);
        if(currentSubcriberEmailList!= null){
            String[] emails = currentSubcriberEmailList.toArray(new String[currentSubcriberEmailList.size()]);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderAddress);
            message.setTo(emails);
            message.setSubject("Товар поступил в продажу");
            ProductDto productDto = productServiceIntegration.findById((long) productId);
            message.setText("Товар "+ productDto.getTitle() + " снова в продаже! Успешных покупок!");
            mailSender.send(message);
            return new StringResponse("Произведена рассылка. Колличество подписчиков: "+ emails.length);
        }else{
            return new StringResponse("Нет подписчиков на данный товар");
        }

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
