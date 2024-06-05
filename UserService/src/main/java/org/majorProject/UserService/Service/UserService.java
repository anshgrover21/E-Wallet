package org.majorProject.UserService.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.micrometer.common.util.StringUtils;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.lang.StringUtil;
import org.majorProject.TrxService.CommonConstant;
import org.majorProject.UserService.Mapper.UserMapper;
import org.majorProject.UserService.Model.User;
import org.majorProject.UserService.Repository.UserRepo;
import org.majorProject.UserService.Request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.majorProject.TrxService.UserIdentifier;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Value("${user.authority}")
    private String userAuthority ;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired // here this string 1 is topic , and String 2 is msg/data
    private KafkaTemplate<String ,String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User createUser(UserRequest userRequest) throws JsonProcessingException {

               User user =     userMapper.userRequestToUser(userRequest);
               System.out.println(user);
//        User user = userRequest.toUser();
        user.setAuthorities(userAuthority);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        // i have to send notification once user is created using kafka, it is as similar as we connect redis
        //isko json_simple se mvn dependency se import kia h

        JSONObject jsonObject = new JSONObject();

        jsonObject.put(CommonConstant.USER_CREATION_TOPIC_NAME, StringUtils.isEmpty(user.getName())?"User":user.getName());
        jsonObject.put(CommonConstant.USER_CREATION_TOPIC_EMAIL, user.getEmail());
//        jsonObject.put(CommonConstant.USER_CREATION_TOPIC_NAME, StringUtils.isEmpty(user.getName())?"User":user.getName());
        jsonObject.put(CommonConstant.USER_CREATION_TOPIC_ID, user.getId());
        jsonObject.put(CommonConstant.USER_CREATION_TOPIC_USERIDENTIFIER, user.getUserIdentifier());
        jsonObject.put(CommonConstant.USER_CREATION_TOPIC_USERIDENTIFIER_VALUE, user.getUserIdentifierValue());
        jsonObject.put(CommonConstant.USER_CREATION_TOPIC_PHONE_NO, user.getPhoneNo());


        kafkaTemplate.send(CommonConstant.USER_CREATION_TOPIC ,objectMapper.writeValueAsString(jsonObject));

//        kafkaTemplate.send(CommonConstant.USER_CREATION_TOPIC ,objectMapper.writeValueAsString(jsonObject));

        return user;
    }




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByPhoneNo(username);
    }

    public User getUser(String contact) {
        return userRepo.findByPhoneNo(contact);
    }
}
