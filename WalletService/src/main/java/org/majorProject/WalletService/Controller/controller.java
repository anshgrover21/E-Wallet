package org.majorProject.WalletService.Controller;

import org.majorProject.WalletService.Model.UserWallet;
import org.majorProject.WalletService.Services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Wallet")
public class controller {

    @Autowired
    private WalletService walletService;

    @GetMapping("/getWallet")
    public UserWallet getWallet(@RequestParam("contact") String contact ){
        return walletService.getWallet(contact);
    }


}
