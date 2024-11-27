package meteor.serverarticle.service;

import lombok.RequiredArgsConstructor;

import meteor.serverarticle.repository.UserRepository;
import meteor.serverarticle.utils.UserDetailsIml;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       var user =  userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("not found"));
       return new UserDetailsIml(user);
    }
}
