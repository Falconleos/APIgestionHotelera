package com.example.ultimate_hotel_software_v30.service.serviceImpl;

import com.example.ultimate_hotel_software_v30.dto.request.EmployeeCreateUnifiedDTO;
import com.example.ultimate_hotel_software_v30.dto.request.EmployeeDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.EmployeeDTOResponse;
import com.example.ultimate_hotel_software_v30.enums.Role;
import com.example.ultimate_hotel_software_v30.enums.Shift;
import com.example.ultimate_hotel_software_v30.exceptions.DuplicatedUserException;
import com.example.ultimate_hotel_software_v30.exceptions.UserNotFoundException;
import com.example.ultimate_hotel_software_v30.mapper.EmployeeMapper;
import com.example.ultimate_hotel_software_v30.model.EmployeeEntity;
import com.example.ultimate_hotel_software_v30.model.RoleEntity;
import com.example.ultimate_hotel_software_v30.model.UserEntity;
import com.example.ultimate_hotel_software_v30.repository.EmployeeRepository;
import com.example.ultimate_hotel_software_v30.repository.RoleRepository;
import com.example.ultimate_hotel_software_v30.repository.UserRepository;
import com.example.ultimate_hotel_software_v30.service.EmployeeService;
import com.example.ultimate_hotel_software_v30.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final EmployeeMapper employeeMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public EmployeeEntity findEntityById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Empleado no encontrado con ID: " + id));
    }

    @Override
    public EmployeeDTOResponse getById(Long id) {
        return employeeMapper.toEmployeeDTOResponse(findEntityById(id));
    }

    @Override
    public List<EmployeeDTOResponse> getAll() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toEmployeeDTOResponse)
                .toList();
    }

    @Override
    @Transactional
    public EmployeeDTOResponse createEmployee(EmployeeCreateUnifiedDTO request) {
        System.out.println("--- [DEBUG] INICIO createEmployee ---");
        System.out.println("[DEBUG] DNI recibido: " + (request != null ? request.getDni() : "REQUEST ES NULO"));

        if (request == null) {
            throw new RuntimeException("El DTO request llegó nulo al servicio.");
        }

        // 1. Buscar si ya existe un usuario con ese DNI
        UserEntity userEntity = userService.userEntityByDni(request.getDni()).orElse(null);
        System.out.println("[DEBUG] Usuario existente encontrado por DNI? " + (userEntity != null));

        // Definimos qué rol tendrá (por defecto RECEPCIONIST si el DTO no envía ninguno)
        Role targetRoleEnum = request.getRole() != null ? request.getRole() : Role.RECEPCIONIST;
        System.out.println("[DEBUG] Rol objetivo: " + targetRoleEnum);

        RoleEntity employeeRole = roleRepository.findByRole(targetRoleEnum)
                .orElseThrow(() -> new RuntimeException("Error: Rol " + targetRoleEnum + " no encontrado en el sistema."));
        System.out.println("[DEBUG] RoleEntity obtenido con éxito ID: " + employeeRole.getId());

        // Procesar la foto de perfil si fue enviada en la petición
        byte[] profilePictureBytes = null;
        if (request.getProfilePictureFile() != null && !request.getProfilePictureFile().isEmpty()) {
            try {
                profilePictureBytes = request.getProfilePictureFile().getBytes();
                System.out.println("[DEBUG] Foto procesada correctamente. Tamaño en bytes: " + profilePictureBytes.length);
            } catch (IOException e) {
                System.out.println("[DEBUG ERROR] Falló al procesar la foto: " + e.getMessage());
                throw new RuntimeException("Error al procesar la foto de perfil: " + e.getMessage(), e);
            }
        } else {
            System.out.println("[DEBUG] No se envió foto o viene vacía.");
        }

        if (userEntity != null) {
            // El usuario YA existe, verificamos si ya tiene un perfil de empleado activo
            if (employeeRepository.existsById(userEntity.getId())) {
                System.out.println("[DEBUG ERROR] El usuario ya tiene un perfil de empleado activo.");
                throw new DuplicatedUserException("El usuario con DNI " + request.getDni() + " ya cuenta con un perfil de empleado activo.");
            }

            // Verificamos si el usuario solo tenía el rol GUEST
            boolean isGuestOnly = userEntity.getRoles().stream()
                    .anyMatch(r -> r.getRole() == Role.GUEST);

            if (isGuestOnly) {
                userEntity.setRoles(Set.of(employeeRole));
            } else {
                userEntity.getRoles().add(employeeRole);
            }

            // Actualizar la foto si se proporcionó una nueva
            if (profilePictureBytes != null) {
                userEntity.setProfilePicture(profilePictureBytes);
            }
            System.out.println("[DEBUG] Usuario existente actualizado en memoria.");

            // Como el usuario ya existe en BD, guardamos los cambios de rol/foto primero
            userEntity = userRepository.save(userEntity);

        } else {
            // 2. Si el usuario NO existe, construimos el UserEntity completo (aún sin guardar para evitar doble insert conflictivo con @MapsId)
            System.out.println("[DEBUG] Construyendo nuevo UserEntity en memoria...");
            userEntity = UserEntity.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .name(request.getName())
                    .surname(request.getSurname())
                    .dni(request.getDni())
                    .gender(request.getGender())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .address(request.getAddress())
                    .birthDay(request.getBirthDay())
                    .createAt(LocalDate.now())
                    .roles(Set.of(employeeRole))
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(request.getEnabled() != null ? request.getEnabled() : true)
                    .profilePicture(profilePictureBytes)
                    .build();
        }

        // 3. Creamos la entidad de Empleado y la asociamos al UserEntity antes de hacer el guardado final en cascada
        System.out.println("[DEBUG] Asociando EmployeeEntity...");
        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .userEntity(userEntity)
                .employeeNumber(request.getEmployeeNumber())
                .hireDate(request.getHireDate() != null ? request.getHireDate() : LocalDate.now())
                .shift(request.getShift())
                .salary(request.getSalary())
                .emergencyPhoneNumber(request.getEmergencyPhoneNumber())
                .build();

        // Sincronización bidireccional obligatoria
        userEntity.setEmployeeEntity(employeeEntity);

        // 4. Único guardado final que persiste tanto al usuario como al empleado de forma limpia en cascada
        System.out.println("[DEBUG] Guardando userEntity y employeeEntity de forma unificada...");
        UserEntity savedUser = userRepository.save(userEntity);
        System.out.println("[DEBUG] ¡Guardado exitoso! Mapeando a DTO respuesta...");

        return employeeMapper.toEmployeeDTOResponse(savedUser.getEmployeeEntity());
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        EmployeeEntity employee = findEntityById(id);

        // Rompemos la relación bidireccional con el usuario para evitar problemas de persistencia en cascada si aplica
        if (employee.getUserEntity() != null) {
            employee.getUserEntity().setEmployeeEntity(null);
        }

        employeeRepository.delete(employee);
    }

    @Override
    @Transactional
    public EmployeeDTOResponse updateEmployee(Long id, EmployeeDTORequest request) {
        EmployeeEntity employee = findEntityById(id);

        // Actualizamos los campos editables del empleado
        employee.setEmployeeNumber(request.getEmployeeNumber());
        employee.setHireDate(request.getHireDate());
        employee.setShift(request.getShift());
        employee.setSalary(request.getSalary());

        EmployeeEntity updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toEmployeeDTOResponse(updatedEmployee);
    }

    @Override
    @Transactional
    public EmployeeDTOResponse cambiarTurno(Long id, Shift nuevoShift) {
        EmployeeEntity employee = findEntityById(id);
        if (employee.getShift().equals(nuevoShift)) {
            throw new IllegalArgumentException("El empleado ya se encuentra asignado a ese turno");
        }
        employee.setShift(nuevoShift);

        EmployeeEntity updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toEmployeeDTOResponse(updatedEmployee);
    }
}