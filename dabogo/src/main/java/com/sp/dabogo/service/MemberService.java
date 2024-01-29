package com.sp.dabogo.service;

import java.util.List;

import com.sp.dabogo.domain.Member;

public interface MemberService {
	public void insertMember(Member dto) throws Exception;
	
	public void updateLastLogin(String userId) throws Exception;
	public void updateMember(Member dto) throws Exception;
	
	public Member findById(String userId);

	public void deleteMember(String userId) throws Exception;
	
	public void insertAuthority(Member dto) throws Exception;
	public void updateAuthority(Member dto) throws Exception;
	public List<String> listAuthority(String userId);
	
	public boolean isPasswordCheck(String userId, String userPwd);	
}
