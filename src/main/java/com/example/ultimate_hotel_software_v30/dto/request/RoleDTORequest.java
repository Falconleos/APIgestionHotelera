package com.example.ultimate_hotel_software_v30.dto.request;

import com.example.ultimate_hotel_software_v30.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@Builder
public class RoleDTORequest {
        @NotNull(message = "name is required")
        private String name;

        @NotNull(message = "description is required")
        private String description;
}


