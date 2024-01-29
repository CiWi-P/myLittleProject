package com.sp.dabogo.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "bbs")//원래 클래스명으로 테이블 만드는데 만들테이블명 지정
public class Board implements Serializable{//직렬화- 네트워크 송수신용
	private static final long serialVersionUID=1L;
	
	@Id
	@Column(name = "num",columnDefinition = "NUMBER")
	@SequenceGenerator(name="S_MY_SEQ",sequenceName = "bbs_seq",allocationSize = 1)//allo...:nocache,++1
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "S_MY_SEQ")
	private long num;

	@Column(name = "hitcount",columnDefinition = "NUMBER DEFAULT 0",insertable = false,updatable = false)
	private long hitCount;
	
	
	
	@Column(nullable = false,length = 30)
	private String name;
	
	@Column(nullable = false,length = 50)
	private String pwd;
	
	@Column(nullable = false,length = 500)
	private String subject;
	
	@Column(nullable = false,length = 4000)
	private String content;
	
	@Column(name="ipaddr",nullable = false,length = 50,updatable = false)
	private String ipAddr;
	
	@Column(nullable = false,columnDefinition = "DATE DEFAULT SYSDATE",updatable = false)
	private String reg_date;

	@PrePersist // 인서트 전에 호출
	public void PrePersist() {
		this.reg_date= this.reg_date == null?new java.sql.Date(new java.util.Date().getTime()).toString() : this.reg_date;
	}
	
	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	
	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public long getHitCount() {
		return hitCount;
	}

	public void setHitCount(long hitCount) {
		this.hitCount = hitCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
