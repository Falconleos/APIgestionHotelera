package com.example.ultimate_hotel_software_v30.service;


import com.example.ultimate_hotel_software_v30.dto.request.UserDTORequest;
import com.example.ultimate_hotel_software_v30.dto.request.UserDTORequestCreation;
import com.example.ultimate_hotel_software_v30.dto.response.UserDTOResponse;
import com.example.ultimate_hotel_software_v30.model.UserEntity;

import java.util.List;

public interface UserService {
    UserDTOResponse getById (Long id);
    UserEntity findEntityById(Long id);
    List<UserDTOResponse> getAll();
    UserDTOResponse createUserWithRole(UserDTORequestCreation request);
    void deleteUser (Long id);
    UserDTOResponse updateUser(Long id, UserDTORequest userDtoRequest);
    UserDTOResponse userByDni(String dni);
    UserDTOResponse findByUsername(String username);
}
