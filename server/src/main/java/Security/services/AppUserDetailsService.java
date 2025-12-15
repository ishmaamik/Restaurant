package Security.services;

import Security.domain.AppUserDetails;
import Users.domain.User;
import Users.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public AppUserDetailsService(UserService userService){
        this.userService= userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userService.getUserByUsername(username);
        return new AppUserDetails(user);
    }
}
