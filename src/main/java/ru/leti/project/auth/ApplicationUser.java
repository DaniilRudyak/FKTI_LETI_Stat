package ru.leti.project.auth;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ApplicationUser implements UserDetails {
    private Set<? extends GrantedAuthority> grantedAuthorities;

    public String getRole() {
        return grantedAuthorities.stream()
                .filter(grantedAuthority -> grantedAuthority.toString().startsWith("ROLE"))
                .collect(Collectors.toList()).toString();
    }

    public void setRole(String role) {
        this.role = grantedAuthorities.stream()
                .filter(grantedAuthority -> grantedAuthority.toString().startsWith("ROLE"))
                .collect(Collectors.toList()).toString();
    }

    @Override
    public String toString() {
        return "ApplicationUser{" +
                "grantedAuthorities=" + grantedAuthorities +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", isAccountNonExpired=" + isAccountNonExpired +
                ", isAccountNonLocked=" + isAccountNonLocked +
                ", isCredentialsNonExpired=" + isCredentialsNonExpired +
                ", isEnabled=" + isEnabled +
                '}';
    }

    private String role;
    @NotEmpty
    private String password;
    @Email
    private String username;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;


    public ApplicationUser (Set<? extends GrantedAuthority> grantedAuthorities,
                           String password, String username,
                           String role,
                           boolean isAccountNonExpired,
                           boolean isAccountNonLocked,
                           boolean isCredentialsNonExpired,
                           boolean isEnabled) {
        this.grantedAuthorities = grantedAuthorities;
        this.password = password;
        this.username = username;
        this.role = role;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }
    public ApplicationUser(){}


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public boolean getPasswordConfirm(){
        return (this.password.equals(getPassword()));
    }
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
