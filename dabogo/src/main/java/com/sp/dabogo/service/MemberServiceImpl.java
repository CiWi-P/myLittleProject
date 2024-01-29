package com.sp.dabogo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sp.dabogo.domain.Member;
import com.sp.dabogo.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	private MemberMapper mapper;

	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Override
	public void insertMember(Member dto) throws Exception {
		try {
			// 패스워드 암호화
			String encPassword = bcryptEncoder.encode(dto.getUserPwd());
			dto.setUserPwd(encPassword);
			
			// 회원정보 저장
			mapper.insertMember(dto);

			// 권한저장
			dto.setAuthority("USER");
			mapper.insertAuthority(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Member findById(String userId) {
		Member dto = null;

		try {
			dto = mapper.findById(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public void updateLastLogin(String userId) throws Exception {
		try {
			mapper.updateLastLogin(userId);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void updateMember(Member dto) throws Exception {
		try {
			/*
			boolean bPwdUpdate = ! isPasswordCheck(dto.getUserId(), dto.getUserPwd());
			if(bPwdUpdate) {
				// 패스워드가 변경된 경우만 member 테이블의 패스워드 변경
				String encPassword = bcryptEncoder.encode(dto.getUserPwd());
				dto.setUserPwd(encPassword);
				
				mapper.updateMember(dto);
			}
			*/
			
			String encPassword = bcryptEncoder.encode(dto.getUserPwd());
			dto.setUserPwd(encPassword);
			
			mapper.updateMember(dto);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void deleteMember(String userId) throws Exception {
		try {
			mapper.deleteMember(userId);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void insertAuthority(Member dto) throws Exception {
		try {
			mapper.insertAuthority(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void updateAuthority(Member dto) throws Exception {
		try {
			mapper.updateAuthority(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<String> listAuthority(String userId) {
		List<String> list = null;
		
		try {
			list = mapper.listAuthority(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	@Override
	public boolean isPasswordCheck(String userId, String userPwd) {
		Member dto = findById(userId);
		if(dto == null) {
			return false;
		}
		
		return bcryptEncoder.matches(userPwd, dto.getUserPwd());
	}
	
}
