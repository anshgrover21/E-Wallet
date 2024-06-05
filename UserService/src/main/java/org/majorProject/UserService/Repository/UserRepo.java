package org.majorProject.UserService.Repository;

import org.majorProject.UserService.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {


   public UserDetails findByUserIdentifierValue(String username);

   User findByPhoneNo(String contact);


}
