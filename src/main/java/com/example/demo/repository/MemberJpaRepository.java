package com.example.demo.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.example.demo.entity.Member;

@Repository
public class MemberJpaRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public Member save(Member member) {
		em.persist(member);
		return member;
	}
	
	public Member find(Long id) {
		return em.find(Member.class, id);
	}
	
	public List<Member> findByUsername(String username){
		return em.createNamedQuery("Member.findByUsername", Member.class)
		.setParameter("username", username)
		.getResultList();
	}
	
	public int bulkAgePlus(int age) {
		return em.createQuery(
				"update Member m set m.age = m.age + 1" +
				"where m.age >= :age")
				.setParameter("age", age)
				.executeUpdate();
	}
}
