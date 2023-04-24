package ru.agile.scrum.mst.market.email.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.agile.scrum.mst.market.api.ProductDto;
import ru.agile.scrum.mst.market.api.StringResponse;
import ru.agile.scrum.mst.market.api.UserPersonalAccountResponse;
import ru.agile.scrum.mst.market.email.integrations.ProductServiceIntegration;
import ru.agile.scrum.mst.market.email.integrations.UserServiceIntegration;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.sender.email}")
    private String senderAddress;
    private final JavaMailSender mailSender;
    private final ProductServiceIntegration productServiceIntegration;
    private final UserServiceIntegration userServiceIntegration;
    private final RedisTemplate<String, List<String>> backToStockSubscriberDB;
    private final String prefix = "S";

    public void subscribeToBackToStock(int productId, String nameuser, String tokenSecurity) {
        UserPersonalAccountResponse personalAccount = userServiceIntegration.getPersonalData(nameuser, tokenSecurity);
        String key = prefix + productId;
        if (personalAccount != null) {
            String email = personalAccount.getEmail();
            if (!backToStockSubscriberDB.hasKey(key)) {
                ArrayList<String> newEmailsList = new ArrayList<>();
                newEmailsList.add(email);
                backToStockSubscriberDB.opsForValue().set(key, newEmailsList);
            } else {
                List<String> updateEmailsList = backToStockSubscriberDB.opsForValue().get(key);
                updateEmailsList.add(email);
                backToStockSubscriberDB.opsForValue().set(key, updateEmailsList);
            }
        }
    }

    public StringResponse sendBackToStock(int productId) {
        String key = prefix + productId;
        List<String> currentSubcriberEmailList = backToStockSubscriberDB.opsForValue().get(key);
        if (currentSubcriberEmailList != null) {
            String[] emails = currentSubcriberEmailList.toArray(new String[currentSubcriberEmailList.size()]);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderAddress);
            message.setTo(emails);
            message.setSubject("Товар поступил в продажу");
            ProductDto productDto = productServiceIntegration.findById((long) productId);
            message.setText("Товар " + productDto.getTitle() + " снова в продаже! Успешных покупок!");
            mailSender.send(message);
            backToStockSubscriberDB.delete(key);
            return new StringResponse("Произведена рассылка. Колличество подписчиков: " + emails.length);

        } else {
            return new StringResponse("Нет подписчиков на данный товар");
        }
    }
}
