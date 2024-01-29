package com.sp.dabogo.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sp.dabogo.domain.Member;
import com.sp.dabogo.service.MemberService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
	private MemberService memberService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberService.findById(username);
		if(member == null) {
			throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
		}
		
		List<String> authorities = memberService.listAuthority(username);
		
		return toUserDetails(member, authorities);
	}

    private UserDetails toUserDetails(Member member, List<String> authorities) {
    	/*
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		String prefix = "ROLE_";
        for (String auth: authorities) {
        	grantedAuthorities.add(new SimpleGrantedAuthority(prefix + auth));
        }
        */
    	
        String[] roles = authorities.toArray(new String[authorities.size()]);
        
        return User.builder()
                .username(member.getUserId())
                .password(member.getUserPwd())
                //.authorities(grantedAuthorities)
                //.authorities(new SimpleGrantedAuthority("ROLE_USER")) // 유저가 하나의 role를 가지고 있는 경우
                //.roles("USER", "ADMIN") // ROLE_ 로 시작할수 없음
                .roles(roles) // ROLE_ 로 시작할수 없음
                .build();
    }	
}
