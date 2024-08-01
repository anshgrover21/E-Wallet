package org.majorProject.WalletService.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.majorProject.TrxService.CommonConstant;
import org.majorProject.WalletService.Model.UserWallet;
import org.majorProject.WalletService.Repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TrxCreateConsumer {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WalletRepo walletRepository;

    @Autowired
    private KafkaTemplate kafkaTemplate;



    @KafkaListener(topics = CommonConstant.TXN_INITIATED_TOPIC, groupId = "wallet-group")
    public void createWallet(String msg) throws JsonProcessingException {
        JSONObject jsonObject = objectMapper.readValue(msg , JSONObject.class);

       String reciever = (String)jsonObject.get(CommonConstant.TXN_INITIATED_TOPIC_RECEIVER);
        Double amount = (Double) (jsonObject.get(CommonConstant.TXN_INITIATED_TOPIC_AMOUNT));
        String sender =  (String)jsonObject.get(CommonConstant.TXN_INITIATED_TOPIC_SENDER);
        String id =  (String)jsonObject.get(CommonConstant.TXN_INITIATED_TOPIC_TXNID);



      UserWallet userWalletSender = walletRepository.findByContact(sender);
        UserWallet userWalletReciever = walletRepository.findByContact(reciever);
        String message = "Transection is in Initiate state";
        String status = "Pending";

        if(userWalletSender==null){
            message = " Sender Does Not Exits ";
            status = "FAILURE";
        }
        else if(userWalletReciever==null){
            message = " reciever Does Not Exits ";
            status = "FAILURE";

        }
        else if(amount> userWalletSender.getBalance()){
            message=" Insufficient Funds ";
            status="FAILURE";
        }
        else{
            walletRepository.updateWallet(userWalletSender.getContact(),userWalletSender.getBalance() -amount);
            walletRepository.updateWallet(userWalletReciever.getContact(),userWalletReciever.getBalance()+amount);
            message="trx is in Success state ";
            status="SUCCESS";
        }

        JSONObject jsonObject1= new JSONObject();
        jsonObject1.put(CommonConstant.TXN_UPDATED_TOPIC_MESSAGE,message);
        jsonObject1.put(CommonConstant.TXN_UPDATED_TOPIC_STATUS,status);
        jsonObject1.put(CommonConstant.TXN_UPDATED_TOPIC_TXNID,id);


        kafkaTemplate.send(CommonConstant.TXN_UPDATED_TOPIC,jsonObject1);


    }



}
