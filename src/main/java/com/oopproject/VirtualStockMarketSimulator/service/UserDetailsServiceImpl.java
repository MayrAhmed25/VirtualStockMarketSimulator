package com.oopproject.VirtualStockMarketSimulator.service;

import com.oopproject.VirtualStockMarketSimulator.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;

@Service // Tells Spring this is a service component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Try to find our custom User entity by username
        com.oopproject.VirtualStockMarketSimulator.model.User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username)
                );

        // 2. Map our custom User entity to the required Spring Security UserDetails object
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(), // Note: Spring expects the HASH here, not the plain password
                new ArrayList<>() // Simple empty list for roles/authorities for now
        );
    }
}