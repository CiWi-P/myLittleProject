package com.sp.dabogo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sp.dabogo.domain.Member;

@Mapper
public interface MemberMapper {
	public void insertMember(Member dto) throws Exception;
	public void updateMember(Member dto) throws Exception;
	public void deleteMember(String userId) throws Exception;
	public void updateLastLogin(String userId) throws Exception;
	
	public Member findById(String userId);
	
	public void insertAuthority(Member dto) throws Exception;
	public void updateAuthority(Member dto) throws Exception;
	public List<String> listAuthority(String userId);	
}
