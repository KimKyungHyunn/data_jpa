package com.example.demo.repository;

import java.util.List;

import javax.persistence.EntityManager;

import com.example.demo.entity.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{
	
	private final EntityManager em;

	@Override
	public List<Member> findMemberCustom() {
		return em.createQuery("select m from Member m")
				.getResultList();
	}
	
	
}
