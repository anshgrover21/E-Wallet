package org.majorProject.WalletService.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.majorProject.WalletService.Model.UserWallet;
import org.majorProject.WalletService.Repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    private WalletRepo walletRepo;

    public UserWallet getWallet(String contact) {

            return  walletRepo.findByContact(contact);

//            JSONObject jsonObject = new JSONObject();



    }

}

