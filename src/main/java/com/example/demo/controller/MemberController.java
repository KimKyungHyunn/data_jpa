package com.example.demo.controller;

import javax.annotation.PostConstruct;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberRepository memberRepository;
	
	@GetMapping("/members")
	public Page<Member> list(@PageableDefault(size = 5, sort = "username")Pageable pageable){
		Page<Member> page = memberRepository.findAll(pageable);
		return page;
	}
	
	@PostConstruct
	public void init() {
		for(int i =0; i<100; i++) {
			memberRepository.save(new Member("user" + i, i));
		}
	}
}
