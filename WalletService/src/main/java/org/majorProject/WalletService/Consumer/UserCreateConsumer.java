package org.majorProject.WalletService.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.majorProject.TrxService.CommonConstant;
import org.majorProject.TrxService.UserIdentifier;
import org.majorProject.WalletService.Model.UserWallet;
import org.majorProject.WalletService.Repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserCreateConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WalletRepo walletRepository;



    //wallet-group se read kia

    @KafkaListener(topics = CommonConstant.USER_CREATION_TOPIC, groupId = "wallet-group")
    public void createWallet(String msg) throws JsonProcessingException {

         JSONObject obj = new JSONObject();
         obj = objectMapper.readValue(msg, JSONObject.class);
        System.out.println(obj);
        String contact = (String) obj.get(CommonConstant.USER_CREATION_TOPIC_PHONE_NO);
        Integer userId = (Integer) obj.get(CommonConstant.USER_CREATION_TOPIC_ID);
        String userIdentifier = (String) obj.get(CommonConstant.USER_CREATION_TOPIC_USERIDENTIFIER);
        String userIdentifierValue = (String) obj.get(CommonConstant.USER_CREATION_TOPIC_USERIDENTIFIER_VALUE);

        UserWallet wallet = UserWallet.builder().
                userId(userId).
                contact(contact).
                userIdentifier(UserIdentifier.valueOf(userIdentifier)).
                userIdentifierValue(userIdentifierValue).
                balance(50.0).
                build();

        walletRepository.save(wallet);
    }


}
