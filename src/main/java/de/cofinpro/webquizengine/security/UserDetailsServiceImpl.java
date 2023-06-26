package de.cofinpro.webquizengine.security;

import de.cofinpro.webquizengine.persistence.RegisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * implementation of the UserDetailsService, that is provided to the authentication manager
 * and handles an authentication versus the RegisteredUserRepository in addition to the
 * in-memory authentication for the two hardcoded users (admin and user).
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private RegisteredUserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(RegisteredUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetailsImpl(userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found!")));
    }
}
