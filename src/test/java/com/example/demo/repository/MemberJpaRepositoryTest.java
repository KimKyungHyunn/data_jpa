package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Member;
import com.example.demo.entity.Team;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
	
	@Autowired MemberJpaRepository memberJpaRepository;
	@Test
	void testMember() {
		Member member = new Member("memA");
		Member savedMember = memberJpaRepository.save(member);
		
		Member findMember = memberJpaRepository.find(savedMember.getId());
		
		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
		assertThat(findMember).isEqualTo(member);
	}
	
	@Test
	public void testNamedQuery() {
		Member member1 = new Member("AAA", 10);
		Member member2 = new Member("BBB", 20);
		memberJpaRepository.save(member1);
		memberJpaRepository.save(member2);
		
		List<Member> result = memberJpaRepository.findByUsername("AAA");
		Member findMember = result.get(0);
		assertThat(findMember).isEqualTo(member1);

	}
	
	@Test
	public void bulkUpdate() {
		memberJpaRepository.save(new Member("member1", 10));
		memberJpaRepository.save(new Member("member2", 19));
		memberJpaRepository.save(new Member("member3", 20));
		memberJpaRepository.save(new Member("member4", 21));
		
		int resultCount = memberJpaRepository.bulkAgePlus(20);
		
		assertThat(resultCount).isEqualTo(3);
	}
	
}
