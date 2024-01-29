package com.sp.dabogo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.sp.dabogo.domain.Board;


public interface BoardRepository extends JpaRepository<Board, Long>{
//Page<T>는 레코드 외에도 부가적인 정보를 들고 다닌다.
	//JPQL를 이용한 검색
	/*@Query(
			value = "SELECT b from Board b where b.name LIKE %:kwd%",
			countQuery = "SELECT count(b.num) from Board b where b.name LIKE %:kwd%"
			) //이건 오라클이 아니라 JPA의 쿼리다 //bbs가 아니라 entity명을 쓴 모습이다.
	public Page<Board> findByName(@Param("kwd") String kwd,Pageable pageable) ;

	@Query(
			
			value = "SELECT b from Board b where b.subject LIKE %:kwd% OR  b.content LIKE %:kwd% ",
			countQuery = "SELECT count(b.num) from Board b where  b.subject LIKE %:kwd% OR  b.content LIKE %:kwd% "
			) //이건 오라클이 아니라 JPA의 쿼리다 //bbs가 아니라 entity명을 쓴 모습이다.
	public Page<Board> findByALL(@Param("kwd") String kwd,Pageable pageable) ;
	*/
	
	
	//findBy컬럼명Containing()=>LIKE 비교
	//findBy요소1And요소2(String 요소1,String 요소2)()=>복수의 AND 비교 (요소1=요소 and 요소2=요소2)
	public Page<Board> findByNameContaining(String kwd,Pageable pageable);
	//like 조건 두개를 or로 연결해라
	public Page<Board> findBySubjectContainingOrContentContaining(String subject,String content,Pageable pageable);
	
	
	
	
	
	
	
	
	@Transactional//
	@Modifying//update delete 할때 필수
	@Query(//위는 jpa쿼리지만  이건 진짜 쿼리 //nativeQuery: false면 JPQL, true면 SQL, 기본은 false
			value="update bbs set hitcount=hitcount+1 where num= :num", nativeQuery = true
			)
	public	int updateHitCount(@Param("num") long num);
	
	
	
	@Query(
			value = "select * from bbs where  num>:num order by num asc fetch first 1 rows only", nativeQuery = true
			)
	public Board findByPrev(@Param("num") long num);
	@Query(
			value = "select * from bbs where  num>:num and name like '%'||:kwd||'%' order by num asc fetch first 1 rows only", nativeQuery = true
			)
	public Board findByPrevName(@Param("num") long num,@Param("kwd") String kwd);
	@Query(
			value = "select * from bbs where  num>:num and (subject like '%'||:kwd||'%' or content like '%'||:kwd||'%') order by num asc fetch first 1 rows only", nativeQuery = true
			)
	public Board findByPrevAll(@Param("num") long num,@Param("kwd") String kwd);
	
	@Query(
			value = "select * from bbs where  num<:num order by num desc fetch first 1 rows only", nativeQuery = true
			)
	public Board findByNext(@Param("num") long num);
	@Query(
			value = "select * from bbs where  num<:num and name like '%'||:kwd||'%' order by num desc fetch first 1 rows only", nativeQuery = true
			)
	public Board findByNextName(@Param("num") long num,@Param("kwd") String kwd);
	@Query(
			value = "select * from bbs where  num<:num and (subject like '%'||:kwd||'%' or content like '%'||:kwd||'%')  order by num desc fetch first 1 rows only", nativeQuery = true
			)
	public Board findByNextAll(@Param("num") long num,@Param("kwd") String kwd);
	
}
