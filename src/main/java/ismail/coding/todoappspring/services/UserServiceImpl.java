package ismail.coding.todoappspring.services;

import ismail.coding.todoappspring.dao.UsersDao;
import ismail.coding.todoappspring.dto.TokensContainer;
import ismail.coding.todoappspring.dto.UpdateUserProfile;
import ismail.coding.todoappspring.dto.UserRequest;
import ismail.coding.todoappspring.exception.ApiRequestException;
import ismail.coding.todoappspring.jwt.JwtConfiguration;
import ismail.coding.todoappspring.model.ApplicationUser;
import ismail.coding.todoappspring.security.AuthenticationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements  UserService , UserDetailsService {
    public final UsersDao usersDao;
    private  final JwtConfiguration jwtConfiguration;


    @Autowired
    public UserServiceImpl(UsersDao usersDao, JwtConfiguration jwtConfiguration) {
        this.usersDao = usersDao;
        this.jwtConfiguration = jwtConfiguration;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ApplicationUser> user = usersDao.findUserByUserName(username) ;
        if (user.isEmpty()) throw new UsernameNotFoundException("no user was found") ;
        log.info("user was found ");
        return user.get();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return  new BCryptPasswordEncoder(10) ;
    }


    public TokensContainer saveUser(ApplicationUser applicationUser) {
        String username = applicationUser.getUsername();
        String email = applicationUser.getEmail()  ;
        validateUsernameAndEmail(username, email,usersDao.findEmailAndUserName(username, email));
        applicationUser.setPassword(passwordEncoder().encode(applicationUser.getPassword()));
         Long id = usersDao.insertUser(applicationUser);
         applicationUser.setId(id);
         return  jwtConfiguration.generateToken(applicationUser);


    }

    private void validateUsernameAndEmail(String username, String email,List<ApplicationUser> list) {
        if (list.size() > 0 ) {
            if ( (list.size() == 2)
                    ||
                    (list.get(0).getUsername().equals(username) && list.get(0).getEmail().equals(email) ) )
                    throw  new ApiRequestException("username and email have been used",HttpStatus.MULTI_STATUS) ;

             if ( list.get(0).getUsername().equals(username)) throw  new ApiRequestException("username is already used",HttpStatus.CONFLICT) ;
             if ( list.get(0).getEmail().equals(email)) throw  new ApiRequestException("email is already used",HttpStatus.CONFLICT) ;
         }
    }

    public UserRequest findUser() {
        var principal = AuthenticationUtils.getAuthenticatorPrincipal() ;
        if ( principal == null ) throw  new ApiRequestException("no principal found",HttpStatus.FAILED_DEPENDENCY) ;

        Optional<ApplicationUser> user =  usersDao.findUserByUserName(principal.getUsername())  ;
        if(user.isEmpty()) throw  new ApiRequestException("there is no user with this data",HttpStatus.NOT_FOUND) ;

        return user.get().generateRequestUser() ;


    }

    public void updateUser(UpdateUserProfile newProfile) {
        var principal = AuthenticationUtils.getAuthenticatorPrincipal() ;
        if ( principal == null ) throw  new ApiRequestException("no principal found",HttpStatus.FAILED_DEPENDENCY) ;
        if ( newProfile.shouldIIgnore())
            return ;
        var user = usersDao.findUserById(principal.getId()) ;
        if ( user.isEmpty() ) throw new ApiRequestException("no user found",HttpStatus.NOT_FOUND) ;
        if(!passwordEncoder().matches(newProfile.getConfirmationPassword(),user.get().getPassword()) )
            throw new ApiRequestException("wrong password",HttpStatus.NOT_ACCEPTABLE) ;
        if (newProfile.getEmail() != null || newProfile.getUsername() != null) {
           validateUsernameAndEmail(
                   newProfile.getUsername(),
                   newProfile.getEmail(),
                   usersDao.findEmailAndUserNameExcludingUser(newProfile.getUsername(), newProfile.getEmail(),principal.getId())
           );
        }

        int rowUpdated = usersDao.updateUser(newProfile,principal.getId()) ;
    }

    private void validateUsernameAndEmailIgnoringThisUser(String username, String email) {


    }
}
