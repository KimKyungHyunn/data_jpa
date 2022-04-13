package com.example.demo.entity;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberTest {

	@PersistenceContext
	EntityManager em;
	
	@Test
	public void testEntity() {
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		em.persist(teamA);
		em.persist(teamB);
		
		Member member1 = new Member("member1", 10, teamA);
		Member member2 = new Member("member2", 20, teamA);
		Member member3 = new Member("member3", 30, teamA);
		Member member4 = new Member("member4", 40, teamA);
		
		em.persist(member1);
		em.persist(member1);
		em.persist(member1);
		em.persist(member1);
		
		//�ʱ�ȭ
		em.flush();
		em.clear();
		
		//Ȯ��
		List<Member> members = em.createQuery("select m from Member m", Member.class)
		.getResultList();
		
		for (Member member : members) {
			System.out.println("member" + member);
			System.out.println("memberTeam" + member.getTeam());
		}
	}

}