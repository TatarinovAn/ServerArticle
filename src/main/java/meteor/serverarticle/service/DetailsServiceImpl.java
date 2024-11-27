package meteor.serverarticle.service;

import meteor.serverarticle.repository.UserRepository;
import meteor.serverarticle.utils.UserDetailsIml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;



    //получить пользователя по имени
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username don't found"));

        return new UserDetailsIml(user);
    }
}
