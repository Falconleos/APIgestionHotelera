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
        // 1. Buscar si ya existe un usuario con ese DNI
        UserEntity userEntity = userService.userEntityByDni(request.getDni()).orElse(null);

        // Definimos qué rol tendrá (por defecto RECEPCIONIST si el DTO no envía ninguno)
        Role targetRoleEnum = request.getRole() != null ? request.getRole() : Role.RECEPCIONIST;

        RoleEntity employeeRole = roleRepository.findByRole(targetRoleEnum)
                .orElseThrow(() -> new RuntimeException("Error: Rol " + targetRoleEnum + " no encontrado en el sistema."));

        if (userEntity != null) {
            // El usuario YA existe, verificamos si ya tiene un perfil de empleado activo
            if (employeeRepository.existsById(userEntity.getId())) {
                throw new DuplicatedUserException("El usuario con DNI " + request.getDni() + " ya cuenta con un perfil de empleado activo.");
            }

            // Verificamos si el usuario solo tenía el rol GUEST (asumiendo que getRole() o getName() devuelve el Enum)
            boolean isGuestOnly = userEntity.getRoles().stream()
                    .anyMatch(r -> r.getRole() == Role.GUEST); // Cambia .getRole() según tu entidad RoleEntity

            if (isGuestOnly) {
                userEntity.setRoles(Set.of(employeeRole));
            } else {
                userEntity.getRoles().add(employeeRole);
            }

            // Opcional: aseguramos que los cambios en el usuario se pasen a la BD
            userEntity = userRepository.save(userEntity);

        } else {
            // 2. Si el usuario NO existe, lo creamos desde cero con el rol indicado en el DTO
            userEntity = UserEntity.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .name(request.getName())
                    .surname(request.getSurname())
                    .dni(request.getDni())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .birthDay(request.getBirthDay())
                    .createAt(LocalDate.now())
                    .roles(Set.of(employeeRole))
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .build();

            userEntity = userRepository.save(userEntity);
        }

        // 3. Creamos la entidad de Empleado utilizando el ID del usuario (gracias a @MapsId)
        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .id(userEntity.getId())
                .userEntity(userEntity)
                .employeeNumber(request.getEmployeeNumber())
                .hireDate(request.getHireDate() != null ? request.getHireDate() : LocalDate.now())
                .shift(request.getShift())
                .salary(request.getSalary())
                .emergencyPhoneNumber(request.getEmergencyPhoneNumber())
                .build();

        // Sincronización bidireccional
        userEntity.setEmployeeEntity(employeeEntity);

        // 4. Guardamos el empleado
        // Nota: Si usas @MapsId y el usuario ya existía, asegúrate de que employeeRepository
        // maneje el merge/persistencia correctamente o usa userRepository.save(userEntity) si tienes CascadeType.ALL
        EmployeeEntity savedEmployee = employeeRepository.save(employeeEntity);

        return employeeMapper.toEmployeeDTOResponse(savedEmployee);
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