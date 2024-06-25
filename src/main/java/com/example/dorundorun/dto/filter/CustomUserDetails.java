package com.example.dorundorun.dto;

import com.example.dorundorun.entity.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private MemberEntity memberEntity;

    public CustomUserDetails(MemberEntity memberEntity){
        this.memberEntity = memberEntity;
    }

    @Override   //사용자의 권한에 대한 것을 리턴해줌(role 값 리턴)
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return memberEntity.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return memberEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return memberEntity.getUsername();
    }


    //계정이 만료되었는지에 관한 내용(이부분을 구현하고 싶으면 데이터베이스에 만료되었는지에 관한 내용을 집어넣고 가져오면 됨)
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
