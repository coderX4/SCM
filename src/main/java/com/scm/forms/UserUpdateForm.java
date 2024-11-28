package com.scm.forms;

import com.scm.validators.ValidFile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserUpdateForm {
    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Min 3 Character is required")
    private String uName;

    private String password;

    @Size(min = 10, max = 10, message = "Invalid Phone Number")
    private String phoneNumber;

    private String about;

    private boolean phoneNumberVerified;

    @ValidFile
    private MultipartFile profilePic;

    private String picture;

}
