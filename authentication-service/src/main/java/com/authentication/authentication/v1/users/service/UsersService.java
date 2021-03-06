package com.authentication.authentication.v1.users.service;

import com.authentication.authentication.v1.users.domain.Users;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {

    public void signIn(Users users);

    public Users signUp(Users users);

    public Users findOneByEmail(String email);

    @Override
    public UserDetails loadUserByUsername(String s);
}