package fr.diginamic.securite;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Utilisateur implements UserDetails {

    private String username;
    private String password;
    private List<Role> roles = new ArrayList<>();

    public Utilisateur (String username, String password, Role role){
        this.username = username;
        this.password = password;
        this.roles.add(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return roles;
    }

    @Override
    public String getPassword() {  return password;  }

    @Override
    public String getUsername() {  return username;  }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

}
