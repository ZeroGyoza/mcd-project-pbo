package main.java.tubes.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {
    private String id;
    private String name;
    private String email;
    private String password; // hashed
    private String address;
    private LocalDate birthOfDate;
    private Double salary;
    private Role role;
    private String resetPasswordToken;
    private LocalDateTime resetPasswordExpires;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {}

    // Constructor untuk registrasi user baru (sebelum masuk DB)
    public User(String name, String email, String password, String address,
                LocalDate birthOfDate, Double salary, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.birthOfDate = birthOfDate;
        this.salary = salary;
        this.role = role;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDate getBirthOfDate() { return birthOfDate; }
    public void setBirthOfDate(LocalDate birthOfDate) { this.birthOfDate = birthOfDate; }

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getResetPasswordToken() { return resetPasswordToken; }
    public void setResetPasswordToken(String resetPasswordToken) { this.resetPasswordToken = resetPasswordToken; }

    public LocalDateTime getResetPasswordExpires() { return resetPasswordExpires; }
    public void setResetPasswordExpires(LocalDateTime resetPasswordExpires) { this.resetPasswordExpires = resetPasswordExpires; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}