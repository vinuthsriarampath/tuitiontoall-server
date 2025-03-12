package edu.vinu.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.vinu.enums.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {

    @NotBlank(message = "First Name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank")
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Past(message = "Date of Birth must be in the past")
    @NotNull(message = "Date of Birth cannot be null")
    private LocalDate dob;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotBlank(message = "Contact cannot be blank")
    @Size(min = 10, max = 10, message = "Contact must be 10 digits long")
    private String contact;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid Email Address")
    private String email;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private Role role;
}
