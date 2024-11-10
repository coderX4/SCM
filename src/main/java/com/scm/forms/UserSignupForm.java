package com.scm.forms;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserSignupForm {
    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Min 3 Character is required")
    private String userName;
    @Email(message = "Invalid Email Address")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Min 6 Character is required")
    private String password;
    @NotBlank(message = "Phone Number is required")
    @Size(min = 10, max = 10, message = "Invalid Phone Number")
    private String phoneNumber;
    @NotBlank(message = "About is required")
    private String about;
}
