package com.scm.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;


    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if(file == null || file.isEmpty()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File cannot be empty").addConstraintViolation();
            return false;
        }
        if(file.getSize() > MAX_FILE_SIZE){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File should be less than 2MB").addConstraintViolation();
            return false;
        }
//        //resolution
//        try {
//            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
//            if(bufferedImage .getHeight() > 1080 || bufferedImage .getWidth() > 1920){}
//
//            return true;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        return true;
    }
}
