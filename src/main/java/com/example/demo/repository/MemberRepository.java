package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.MemberDto;
import com.example.demo.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{
	
	List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
	
	//@Query(name = "Member.findByUsername")
	List<Member> findByUsername(@Param("username") String username);
	
	//repostitory method에 쿼리 작성하는 법
	//findByUsernameAndAgeGreaterThan의 경우 파라미터 길어지면 메소드명 길어지는 문제
	@Query("select m from Member m where m.username = :username and m.age = :age")
	List<Member> findUser(@Param("username") String username, @Param("age") int age);
	
	@Query("select m.username from Member m")
	List<String> findUsernameList();
	
	@Query("select new com.example.demo.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
	List<MemberDto> findMemberDto();
	
	//파라미터 바인딩
	@Query("select m from Member m where m.username in :names")
	List<Member> findByNames(@Param("names") List<Object> list);
	
	//유연한 반환 타입 지원 
	List<Member> findListByUsername(String username);
	Member findMemberByUsername(String username);
	Optional<Member> findOptionalByUsername(String username);
	
	@Query(value = "select m from Member m left join m.team t",
			countQuery = "select count(m) from Member m")
	Page<Member> findByAge(int age, Pageable pageable);
	//Slice<Member> findByAge(int age, Pageable pageable);
	
	@Modifying(clearAutomatically = true) //bulk연산의 영속성 컨텍스트 문제 해결
	@Query("update Member m set m.age = m.age + 1 where m.age >= :age")
	int bulkAgePlus(@Param("age") int age);
	
	@Query("select m from Member m left join fetch m.team")
	List<Member> findMemberFetchJoin();
	
	@Override
	@EntityGraph(attributePaths = {"team"})
	List<Member> findAll();
	
	@EntityGraph(attributePaths = {"team"})
	@Query("select m from Member m")
	List<Member> findMemberEntityGraph();
	
	//@EntityGraph(attributePaths = {"team"})
	@EntityGraph("Member.all")
	List<Member> findMemberEntityGraph(@Param("usernames") String username);
	
	//@QueryHints((@QueryHint(name = "org.hibernate.readOnly", value = "true"))
	Member findReadOnlyByUsername(String username);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<Member> findLockByUsername(String username);
}
