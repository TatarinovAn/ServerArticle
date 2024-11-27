package meteor.serverarticle.utils;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import meteor.serverarticle.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//Детали пользователя
@AllArgsConstructor
public class UserDetailsIml implements UserDetails {
    private User user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> list = new ArrayList<>();
        for(int i = 0; i < user.getRoles().size(); i++) {
            System.out.println(user.getRoles().get(i).getName());
            list.add(new SimpleGrantedAuthority(user.getRoles().get(i).getName()));
        }
        return list;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
}
