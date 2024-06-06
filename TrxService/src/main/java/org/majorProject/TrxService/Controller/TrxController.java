package org.majorProject.TrxService.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.majorProject.TrxService.Model.Trx;
import org.majorProject.TrxService.Service.TrxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trx")
public class TrxController {


    @Autowired
    private TrxService trxService;



    @PostMapping("/createTrx")
    public Trx createTrx(@RequestParam("amount") double amount , @RequestParam("reciever") String reciever , @RequestParam("purpose") String purpose) throws JsonProcessingException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
//        Trx trx= Trx.builder().senderId(userDetails.getUsername()).receiverId(reciever)..build()


        return trxService.createTrx(userDetails.getUsername(), reciever, amount, purpose);

    }

    @GetMapping("/getTrx")
    public List<Trx> getTrx(@RequestParam("Page") int page, @RequestParam("size") int size){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       return trxService.getTrx(userDetails.getUsername(),page,size);

    }



}
