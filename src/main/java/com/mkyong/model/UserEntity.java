package com.mkyong.model;

import jakarta.persistence.*;
import lombok.Data;

import org.apache.el.stream.Stream;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.mkyong.helpers.CustomIdGenerator;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale.Category;

@Entity
@Data
@Component
@Table(name = "users")
public class UserEntity implements UserDetails {

    public enum user_type {

    }

    @Id
    @GenericGenerator(name = "user_id_seq", type = CustomIdGenerator.class, parameters = {
            // @org.hibernate.annotations.Parameter(name =
            // CustomIdGenerator.VALUE_PREFIX_PARAMETER, value = "USR"),
            @org.hibernate.annotations.Parameter(name = CustomIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%06d") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    private String id;
    @Column(unique = true)
    private String email;
    private String phoneNo;
    private String name;
    private BigDecimal balance = BigDecimal.valueOf(500);
    private String password;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private Date updatedAt;
    // @Enumerated(EnumType.ORDINAL)
    private UserRole role = UserRole.user;
    private int qrcodeLockDuration;
    private boolean isQrcodeLocked = false;

    // for JPA only, no use
    public UserEntity() {
    }

    public UserEntity(String name_, String email_, String phoneNo_, String password_, UserRole role_) {
        this.name = name_;
        this.email = email_;
        this.phoneNo = phoneNo_;
        this.password = password_;
        this.role = role_;

    }

    static public UserEntity init(String name_, String email_, String phoneNo_, String password_, UserRole role_) {
        return new UserEntity("", "", "", "", UserRole.user);

    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        System.out.println("getUserna, method was ca");
        return String.valueOf(id);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum UserRole {
        user("U"), driver("D"), admin("A");

        private String code;

        private UserRole(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    @Converter(autoApply = true)
    public class RoleConverter implements AttributeConverter<UserRole, String> {

        @Override
        public String convertToDatabaseColumn(UserRole role) {
            if (role == null) {
                return null;
            }

            return role.getCode();
        }

        @Override
        public UserRole convertToEntityAttribute(String code) {
            if (code == null) {
                return null;
            }

            // return
            return java.util.stream.Stream.of(UserRole.values()).filter(x -> x.getCode().equals(code)).findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }

    }
}
