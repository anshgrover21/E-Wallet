package org.majorProject.TrxService.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.GeneratedValue;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.majorProject.TrxService.CommonConstant;
import org.majorProject.TrxService.Model.Trx;
import org.majorProject.TrxService.Model.TxnStatus;
import org.majorProject.TrxService.Repository.TrxRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TrxService implements UserDetailsService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public UserDetails loadUserByUsername(String contact) throws UsernameNotFoundException {

        // yha pe mujhe verify krna h user ko contact se jo hme authentication ke tym milega contact dalne me
        //isko manage krne ke lie ek trick follow krni hai ki hm get request marenge apnee user service pr

        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth("trx-service","trx-service");
        // basically ham yha kya kr rhe hai ki ham directly enter nhi kr paaenge get method me because vo secured h to hame kuch aisa krna hoga ki hm get method me aajae or fir vha se hm
//ham user ke data base ko access krke name fetch krle data base se isse hame user se password mangne ki need hi ni pdegi direcctly data base access krpaenge get method se , to uske lie hamne already
        // data base me ek trx-service id and trx-service password dal dia or iski auth rkhdi service , so jb bhi service ke through hm get ko hit krenenge ham directly phch jaenge data base pe is process ko khte hai onboarding
        // of service one to service two
        HttpEntity request =new HttpEntity<>(header);

        JSONObject jsonObject = restTemplate.exchange("http://localhost:8080/user/get?contact="+contact, HttpMethod.GET,request,JSONObject.class).getBody();
        System.out.println(jsonObject);
        // ye spring security wala user hai
        List<LinkedHashMap<String ,String>> l =  ( List<LinkedHashMap<String ,String>>)jsonObject.get("authorities");
            List<GrantedAuthority> list =    l.stream().map(x -> x.get("authority")).map(x -> new SimpleGrantedAuthority(x)).collect(Collectors.toList());

        User user = new User((String)jsonObject.get("phoneNo"),(String)jsonObject.get("password"), list); // YE GALAT HAI

        return user;
    }

    @Autowired
    private TrxRepo trxRepo;

    @Autowired
    private KafkaTemplate kafkaTemplate;


    public Trx createTrx(String username, String reciever, double amount, String purpose) throws JsonProcessingException {

        Trx txn = Trx.builder().
                txnId(UUID.randomUUID().toString()).
                senderId(username).
                txnAmount(Double.valueOf(amount)).
                receiverId(reciever).
                purpose(purpose).
                txnStatus(TxnStatus.INITIATED).
                build();

         trxRepo.save(txn);

         //pushing data to topic and sending the details to the wallet so that wallet update the details
        // we cam either do it with kafka or we can do it with htpp post request as we did before

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CommonConstant.TXN_INITIATED_TOPIC_RECEIVER,reciever);
        jsonObject.put(CommonConstant.TXN_INITIATED_TOPIC_AMOUNT,amount);
        jsonObject.put(CommonConstant.TXN_INITIATED_TOPIC_SENDER,username);
        jsonObject.put(CommonConstant.TXN_INITIATED_TOPIC_TXNID,txn.getTxnId());


        kafkaTemplate.send(CommonConstant.TXN_INITIATED_TOPIC,objectMapper.writeValueAsString(jsonObject));


        return txn;


    }


    //trx-group se read kia
    // topic is USER_Update_topic
    @KafkaListener(topics = CommonConstant.TXN_UPDATED_TOPIC, groupId = "trx-group")
    public void createWallet(String msg) throws JsonProcessingException {
            JSONObject jsonObject =new JSONObject();
          jsonObject=  objectMapper.readValue(msg,JSONObject.class);
            String trxid = (String) jsonObject.get(CommonConstant.TXN_UPDATED_TOPIC_TXNID);
            String message = (String) jsonObject.get(CommonConstant.TXN_UPDATED_TOPIC_MESSAGE);
           String trxStatus = (String) jsonObject.get(CommonConstant.TXN_UPDATED_TOPIC_STATUS);

        TxnStatus txnStatus = TxnStatus.valueOf(trxStatus);
        System.out.println(txnStatus);
        trxRepo.updateTrx(trxid,message,txnStatus);
    }


    public List<Trx> getTrx(String username, int page, int size) {

        Pageable pagable = PageRequest.of(page,size);

        Page<Trx> pageData = trxRepo.findBySenderId(username,pagable);
        return pageData.getContent();


    }
}
