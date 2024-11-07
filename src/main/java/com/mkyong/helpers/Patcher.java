package com.mkyong.helpers;

import com.mkyong.model.UserEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;

@Component
public class Patcher {
    public static void userPatcher(UserEntity userData, UserEntity newData) throws IllegalAccessException {

        // GET THE COMPILED VERSION OF THE CLASS
        Class<?> internClass = UserEntity.class;
        Field[] userFields = internClass.getDeclaredFields();
        System.out.println(userFields.length);
        for (Field field : userFields) {
            // System.out.println(field.getName());
            // CANT ACCESS IF THE FIELD IS PRIVATE
            field.setAccessible(true);
            // CHECK IF THE VALUE OF THE FIELD IS NOT NULL, IF NOT UPDATE EXISTING INTERN
            Object value = field.get(newData);
            if (value != null) {
                field.set(userData, value);
                System.out.println(" user fields is " + field.get(userData));
            }
            // MAKE THE FIELD PRIVATE AGAIN
            field.setAccessible(false);
        }

    }

}