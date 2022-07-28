package ismail.coding.todoappspring.model;

import ismail.coding.todoappspring.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {


    private Long id ;
    private String fullName  ;
    private String username ;
    private String bio  ;
    private  String email  ;
    private boolean enabled = true ;
    private String password ;
    private Role role ;

    public User(String fullName,
                String username, String bio, String email, boolean enabled, String password, Role role) {
        this.fullName = fullName;
        this.username = username;
        this.bio = bio;
        this.email = email;
        this.enabled = enabled;
        this.password = password;
        this.role = role;
    }
    public List<SimpleGrantedAuthority> getAuthoritiesAsList() {
        return  role.getSimpleGrantedAuthorities() ;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getSimpleGrantedAuthorities() ;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return enabled;
    }
}
