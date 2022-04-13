package com.example.demo.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQuery;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//연관관계인 member도 쓰면 member도 타고들어가게되서 안쓰는것이 좋다
@ToString(of = {"id", "username", "age"})
@NamedQuery(
		name="Member.findByUsername",
		query="select m from Member m where m.usernmae = :username"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("items"))
public class Member {
	
	@Id @GeneratedValue
	@Column(name = "member_id")
	private Long id;
	private String username;
	private int age;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;
	
	public Member(String username) {
		this.username = username;
	}
	
	public Member(String username, int age, Team team) {
		this.username = username;
		this.age = age;
		changeTeam(team);
	}
	
	public void changeTeam(Team team) {
		this.team = team;
		team.getMembers().add(this);
	}

	public Member(String username, int age) {
		this.username = username;
		this.age = age;
	}
}
