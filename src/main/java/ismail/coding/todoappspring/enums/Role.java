package ismail.coding.todoappspring.enums;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static ismail.coding.todoappspring.enums.UserAuthorities.* ;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Set.of(TASK_READ,TASK_WRITE)) ,
    ADMIN(Set.of(TASK_READ,TASK_WRITE,USER_WRITE,USER_READ))  ;

    private  final Set<UserAuthorities> authorities ;
    Role(Set<UserAuthorities> authorities) {
          this.authorities = authorities  ;
    }
    public List<SimpleGrantedAuthority> getSimpleGrantedAuthorities() {
        // add authorities
        List<SimpleGrantedAuthority> list =  this.authorities
                .stream().map( (item ) ->  new SimpleGrantedAuthority(item.name()))
                .collect(Collectors.toList());
        // add  role
        list.add(new SimpleGrantedAuthority("ROLE_"+this.name()))  ;
        return list ;


    }

}
