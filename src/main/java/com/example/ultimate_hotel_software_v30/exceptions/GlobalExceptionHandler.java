package com.example.ultimate_hotel_software_v30.exceptions;

import com.example.ultimate_hotel_software_v30.dto.response.ErrorDTOResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTOResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest webRequest){

        String errores = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body( new ErrorDTOResponse(errores, webRequest.getDescription(false)) );
    }

    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<ErrorDTOResponse>handleInvalidIdException(InvalidIdException ex,WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }

    //Excepciones de reservas
    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ErrorDTOResponse>handleReservaNoEncontradaException(BookingNotFoundException ex,WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }

    @ExceptionHandler(BookingStateConflictException.class)
    public ResponseEntity<ErrorDTOResponse>handleConflictoDeEstadoReservaException(BookingStateConflictException ex,WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }

    @ExceptionHandler(CheckInNotFoundException.class)
    public ResponseEntity<ErrorDTOResponse>handleEstadiaNoEncontradaException(CheckInNotFoundException ex,WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }

    @ExceptionHandler(InvalidCheckInException.class)
    public ResponseEntity<ErrorDTOResponse>handleEstadiaInvalidaException(InvalidCheckInException ex,WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ErrorDTOResponse>handleFechaInvalidaException(InvalidDateException ex,WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }

    //Excepciones de habitaciones
    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ErrorDTOResponse>handleHabitacionNoEncontradaException(RoomNotFoundException ex,WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );
    }

    @ExceptionHandler(DisabledRoomException.class)
    public ResponseEntity<ErrorDTOResponse>handleHabitacionNoDisponibleException(DisabledRoomException ex,WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );
    }

    @ExceptionHandler(OccupiedRoomException.class)
    public ResponseEntity<ErrorDTOResponse>handleHabitacionEnUsoException(OccupiedRoomException ex,WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );
    }

    @ExceptionHandler(DuplicatedRoomException.class)
    public ResponseEntity<ErrorDTOResponse>handleInvalidId(DuplicatedRoomException ex, WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }

    @ExceptionHandler(RoomUnderMaintenanceException.class)
    public ResponseEntity<ErrorDTOResponse>handleHabitacionYaEnMantenimientoException(RoomUnderMaintenanceException ex,WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );
    }



    //Excepciones de personas
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDTOResponse>handlePasajeroNoEncontradoException(UserNotFoundException ex,WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }

    @ExceptionHandler(DuplicatedUserException.class)
    public ResponseEntity<ErrorDTOResponse>handlePasajeroDuplicadoException(DuplicatedUserException ex,WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }

    @ExceptionHandler(InvalidUserUpdateException.class)
    public ResponseEntity<ErrorDTOResponse>handleEmpleadoDuplicadoExcepcion(InvalidUserUpdateException ex,WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }


    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorDTOResponse>handleRolNoEncontradoException(RoleNotFoundException ex,WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }


    @ExceptionHandler(DuplicatedRoleException.class)
    public ResponseEntity<ErrorDTOResponse>handleRoleDuplicadoException(DuplicatedRoleException ex,WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }

    @ExceptionHandler(InvalidNameException.class)
    public ResponseEntity<ErrorDTOResponse>handleInvalidId(InvalidNameException ex,WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDTOResponse> handleBadCredentials(BadCredentialsException ex, WebRequest webRequest) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDTOResponse("Usuario o contraseña incorrectos", webRequest.getDescription(false)));
    }


    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorDTOResponse>handleInvalidTokenException(InvalidTokenException ex, WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }
    @ExceptionHandler(CapacityOutOfRangeException.class)
    public ResponseEntity<ErrorDTOResponse>handleInvalidTokenException(CapacityOutOfRangeException ex, WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }

    @ExceptionHandler(DisabledUserException.class)
    public ResponseEntity<ErrorDTOResponse>handleInvalidTokenException(DisabledUserException ex, WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorDTOResponse>handleInvalidId(InvalidPasswordException ex, WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }

    @ExceptionHandler(DuplicatedDNIException.class)
    public ResponseEntity<ErrorDTOResponse>handleInvalidId(DuplicatedDNIException ex, WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<ErrorDTOResponse>handleInvalidId(DuplicatedEmailException ex, WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTOResponse>handleInvalidId(IllegalArgumentException ex, WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }

    @ExceptionHandler(UnauthorizedCommentException.class)
    public ResponseEntity<ErrorDTOResponse>handleInvalidId(UnauthorizedCommentException ex, WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }



    /// /////////////////////
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTOResponse>handleInvalidId(Exception ex, WebRequest webRequest){

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body( new ErrorDTOResponse(ex.getMessage(), webRequest.getDescription(false)) );

    }



}
