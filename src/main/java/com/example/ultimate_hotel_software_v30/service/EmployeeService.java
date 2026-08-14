package com.example.ultimate_hotel_software_v30.service;

import com.example.ultimate_hotel_software_v30.dto.request.EmployeeCreateUnifiedDTO;
import com.example.ultimate_hotel_software_v30.dto.request.EmployeeDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.EmployeeDTOResponse;
import com.example.ultimate_hotel_software_v30.enums.Shift;
import com.example.ultimate_hotel_software_v30.model.EmployeeEntity;

import java.util.List;

public interface EmployeeService {

    EmployeeEntity findEntityById(Long id);
    EmployeeDTOResponse getById(Long id);
    List<EmployeeDTOResponse> getAll();
    EmployeeDTOResponse createEmployee(EmployeeCreateUnifiedDTO request);
    void deleteEmployee(Long id);
    EmployeeDTOResponse updateEmployee(Long id, EmployeeDTORequest request);
    EmployeeDTOResponse cambiarTurno(Long id, Shift nuevoShift);

}
