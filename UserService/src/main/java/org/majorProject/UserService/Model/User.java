package org.majorProject.UserService.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.majorProject.TrxService.UserIdentifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User  implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 15, unique = true, nullable = false)
    private String phoneNo;

    @Column(length = 30, unique = true)
    private String email;

    @Column(length = 30)
    private String name;

    private String address;

    @CreationTimestamp // use to create a time when ever date is added
    private Date createdOn;

    @UpdateTimestamp // update the time
    private Date updatedOn;

    @Enumerated(value = EnumType.STRING)
    private UserIdentifier userIdentifier;

    @Enumerated(value = EnumType.STRING)
    private UserType userType;


//    @Column( )
    private String userIdentifierValue;

    private String password;

    private String authorities;

    private String dob;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(authorities.split(",")).map(atr -> new SimpleGrantedAuthority(atr)).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.phoneNo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
