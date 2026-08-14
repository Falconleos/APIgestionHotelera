package com.example.ultimate_hotel_software_v30.service.serviceImpl;

import com.example.ultimate_hotel_software_v30.dto.request.UserDTORequest;
import com.example.ultimate_hotel_software_v30.dto.request.UserDTORequestCreation;
import com.example.ultimate_hotel_software_v30.dto.response.UserDTOResponse;
import com.example.ultimate_hotel_software_v30.exceptions.DuplicatedDNIException;
import com.example.ultimate_hotel_software_v30.exceptions.DuplicatedEmailException;
import com.example.ultimate_hotel_software_v30.exceptions.InvalidNameException;
import com.example.ultimate_hotel_software_v30.exceptions.UserNotFoundException;
import com.example.ultimate_hotel_software_v30.mapper.UserMapper;
import com.example.ultimate_hotel_software_v30.model.RoleEntity;
import com.example.ultimate_hotel_software_v30.model.UserEntity;
import com.example.ultimate_hotel_software_v30.repository.RoleRepository;
import com.example.ultimate_hotel_software_v30.repository.UserRepository;
import com.example.ultimate_hotel_software_v30.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTOResponse getById(Long id) {
        return userMapper.toUserDTOResponse(findEntityById(id));
    }

    @Override
    public UserEntity findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    @Override
    public List<UserDTOResponse> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDTOResponse)
                .toList();
    }

    @Override
    public UserDTOResponse createUserWithRole(UserDTORequestCreation request) {
        // 1. Validaciones de existencia
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicatedEmailException("email already exists");
        }
        if (userRepository.existsByDni(request.getDni())) {
            throw new DuplicatedDNIException("dni already exists");
        }

        // 2. Mapear DTO a Entidad (asumiendo que existe el método en el mapper)
        UserEntity userEntity = userMapper.toUserEntityFromCreation(request);

        // 3. Obtener el rol dinámico enviado en el request (convirtiendo el Enum a String)
        RoleEntity userRole = roleRepository.findByRole(request.getRole())
                .orElseThrow(() -> new InvalidNameException("No role named: " + request.getRole()));
        userEntity.setRoles(Set.of(userRole));

        // 4. Encriptar contraseña provista
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        // 5. Configurar campos obligatorios y estados de seguridad de la cuenta
        userEntity.setCreateAt(LocalDate.now());
        userEntity.setAccountNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setCredentialsNonExpired(true);
        userEntity.setEnabled(true);

        // 6. Guardar en Base de Datos
        UserEntity savedUser = userRepository.save(userEntity);

        return userMapper.toUserDTOResponse(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity userEntity = findEntityById(id);
        userRepository.delete(userEntity);
    }

    @Override
    @Transactional
    public UserDTOResponse updateUser(Long id, UserDTORequest userDtoRequest) {
        UserEntity userEntity = findEntityById(id);

        // 2. Validar DNI: si el DNI ingresado es diferente al actual, verificar que no exista en otro usuario
        if (!userEntity.getDni().equals(userDtoRequest.getDni()) &&
                userRepository.existsByDni(userDtoRequest.getDni())) {
            throw new DuplicatedDNIException("Dni already exists");
        }

        // 3. Validar Email: si el Email ingresado es diferente al actual, verificar duplicados
        if (!userEntity.getEmail().equals(userDtoRequest.getEmail()) &&
                userRepository.existsByEmail(userDtoRequest.getEmail())) {
            throw new DuplicatedEmailException("Email already exists");
        }

        // 4. Modificaciones completas según los campos de UserDTORequest
        userEntity.setUsername(userDtoRequest.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userDtoRequest.getPassword()));
        userEntity.setName(userDtoRequest.getName());
        userEntity.setSurname(userDtoRequest.getSurname());
        userEntity.setDni(userDtoRequest.getDni());
        userEntity.setGender(userDtoRequest.getGender());
        userEntity.setEmail(userDtoRequest.getEmail());
        userEntity.setPhoneNumber(userDtoRequest.getPhoneNumber());
        userEntity.setAddress(userDtoRequest.getAddress());
        userEntity.setBirthDay(userDtoRequest.getBirthDay());

        return userMapper.toUserDTOResponse(userEntity);
    }

    @Override
    public UserDTOResponse userByDni(String dni) {
        UserEntity userEntity = userRepository.findByDni(dni)
                .orElseThrow(() -> new UserNotFoundException("user does not exist with dni: " + dni));
        return userMapper.toUserDTOResponse(userEntity);
    }

    @Override
    public UserDTOResponse findByUsername(String username) {
        return userMapper.toUserDTOResponse(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username inexistent")));
    }

    @Override
    public Optional<UserEntity> userEntityByDni(String dni) {
        return userRepository.findByDni(dni);
    }
}