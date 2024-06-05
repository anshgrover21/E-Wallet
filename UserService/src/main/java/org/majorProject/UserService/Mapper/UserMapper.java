package org.majorProject.UserService.Mapper;

import org.majorProject.UserService.Model.User;
import org.majorProject.UserService.Request.UserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserRequest userToUserRequest(User user);
    User userRequestToUser(UserRequest userRequest);


}
