package org.majorProject.UserService.Request;
import org.majorProject.TrxService.UserIdentifier;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.majorProject.UserService.CustomAnnotaion.AgeLimit;
import org.majorProject.UserService.Model.User;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "email Cannot Be blank")
    private String email;

    private String phoneNo;

    private String name;

    private String password;


    private String address;

    @AgeLimit(minmumAge = 16 ,message = "Age cannot be lower than 16")
    private String dob;

    private UserIdentifier userIdentifier;

    private String userIdentifierValue;

    public User toUser(){
        return User.builder().
                name(this.getName())
                .email(this.getEmail())
                .phoneNo(this.getPhoneNo())
                .password(this.getPassword())
                .dob(this.getDob())
                .userIdentifier(this.getUserIdentifier())
                .userIdentifierValue(this.getUserIdentifierValue())
                .address(this.getAddress()).build();
    }

}
