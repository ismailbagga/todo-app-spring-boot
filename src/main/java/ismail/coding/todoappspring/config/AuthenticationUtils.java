package ismail.coding.todoappspring.config;

import ismail.coding.todoappspring.dto.IsAuthenticatedRequest;
import ismail.coding.todoappspring.model.UsernameUserIdPrincipal;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtils {
    public static IsAuthenticatedRequest isAuthenticatedRequest() {
        UsernameUserIdPrincipal principal =
                getAuthenticatorPrincipal() ;
        if ( principal == null )
            return  new IsAuthenticatedRequest(null,false) ;
        return  new IsAuthenticatedRequest(
                principal.getUsername(),
                true) ;
    }
    public static UsernameUserIdPrincipal getAuthenticatorPrincipal () {
        Authentication authentication
                = SecurityContextHolder.getContext().getAuthentication() ;
        if ( authentication instanceof AnonymousAuthenticationToken) {
            return  null ;
        }
        var principal = authentication.getPrincipal() ;
        return ( UsernameUserIdPrincipal) authentication.getPrincipal() ;
    }
}
