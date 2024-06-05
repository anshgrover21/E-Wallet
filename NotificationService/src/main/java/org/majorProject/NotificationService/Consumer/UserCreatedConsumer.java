package org.majorProject.NotificationService.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import netscape.javascript.JSObject;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.majorProject.TrxService.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service  // isme generally buisness logic hota h to hm ise service me dalte h
public class UserCreatedConsumer {

    @Autowired
    private ObjectMapper objectMapper;
    //group is same as what we have discussed in kafka it is a consumer group whcih contains multiple consume in it

    @Autowired
    private JavaMailSender sender;

    @KafkaListener(topics = CommonConstant.USER_CREATION_TOPIC,groupId = "notification-group")
    public void sendNotification(String msg) throws JsonProcessingException {
        JSONObject jsonObject = new JSONObject();
           jsonObject  = objectMapper.readValue(msg, JSONObject.class);
           String name = (String)jsonObject.get(CommonConstant.USER_CREATION_TOPIC_NAME);
           String email = (String)jsonObject.get(CommonConstant.USER_CREATION_TOPIC_EMAIL);

        SimpleMailMessage message= new SimpleMailMessage();

        message.setFrom("ansh.gvr@gmail.com");
        message.setTo(email);
        message.setText("Hello " + name + " welcome to the Ewallet . Yours Proifle has been created successfully");
        message.setSubject("Welcome To E-wallet");

        sender.send(message);



    }

}
