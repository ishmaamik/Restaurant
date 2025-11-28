package Security.domain;

import Users.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.stream.Collectors;

public class AppUserDetails implements UserDetails {

    private final User user;

    public AppUserDetails(User user){
        this.user=user;
    }

    public Set<? extends GrantedAuthority> getAuthorities(){
        return user.getRoles()
                .stream()
                .map( r-> new SimpleGrantedAuthority("ROLE_"+ r.name()))
                .collect(Collectors.toSet());
    }

    public String getUsername(){
        return user.getUsername();
    }

    public String getPassword(){
        return user.getPasswordHash();
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}
