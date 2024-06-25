package com.example.dorundorun.service;

import com.example.dorundorun.dto.CustomUserDetails;
import com.example.dorundorun.entity.MemberEntity;
import com.example.dorundorun.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        MemberEntity userData = memberRepository.findByUsername(username).get();

        if(userData != null){
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
