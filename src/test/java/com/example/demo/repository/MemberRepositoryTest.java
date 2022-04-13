package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.MemberDto;
import com.example.demo.entity.Member;
import com.example.demo.entity.Team;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

	@Autowired 
	MemberRepository memberRepository;
	@Autowired
	TeamRepository teamRepository;
	@PersistenceContext
	EntityManager em;

	@Test
	void testMember() {
		Member member = new Member("memA");
		Member savedMember = memberRepository.save(member);
		
		Member findMember = memberRepository.findById(savedMember.getId()).get();
		
		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
		assertThat(findMember).isEqualTo(member);
	}
	
	@Test
	public void findByUsernameAndAgeGreaterThen() {
		Member member1 = new Member("AAA", 10);
		Member member2 = new Member("AAA", 20);
		memberRepository.save(member1);
		memberRepository.save(member2);
		
		List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
		
		assertThat(result.get(0).getUsername()).isEqualTo("AAA");
		assertThat(result.get(0).getAge()).isEqualTo(20);
		assertThat(result.size()).isEqualTo(1);
		
	}
	
	@Test
	public void testNamedQuery() {
		Member member1 = new Member("AAA", 10);
		Member member2 = new Member("AAA", 20);
		memberRepository.save(member1);
		memberRepository.save(member2);
		
		List<Member> result = memberRepository.findByUsername("AAA");
		Member findMember = result.get(0);
		assertThat(findMember).isEqualTo(member1);
	}
	
	@Test
	public void testQuery() {
		Member member1 = new Member("AAA", 10);
		Member member2 = new Member("BBB", 20);
		memberRepository.save(member1);
		memberRepository.save(member2);
		
		List<Member> result = memberRepository.findUser("AAA", 10);
		assertThat(result.get(0)).isEqualTo(member1);
	}
	
	@Test
	public void findUsernameList() {
		Member member1 = new Member("AAA", 10);
		Member member2 = new Member("BBB", 20);
		memberRepository.save(member1);
		memberRepository.save(member2);
		
		List<String> usernameList = memberRepository.findUsernameList();
		assertThat(usernameList.get(0)).isEqualTo(member1.getUsername());
	}
	
	@Test
	public void findMemberDto() {
		Team team = new Team("teamA");
		teamRepository.save(team);
		
		Member member1 = new Member("AAA", 10);
		member1.setTeam(team);
		memberRepository.save(member1);
		
		List<MemberDto> memberDto = memberRepository.findMemberDto();
		for (MemberDto dto : memberDto) {
			System.out.println("dto =" + dto);
		}
	}	
	
	@Test
	public void findByNames() {
		Member member1 = new Member("AAA", 10);
		Member member2 = new Member("BBB", 20);
		memberRepository.save(member1);
		memberRepository.save(member2);
		String [] str = {"AAA", "BBB"};
		
		List<Member> result = memberRepository.findByNames(Arrays.asList(str));
		for (Member member : result) {
			System.out.println("member" + member);
		}
	}
	
	//������ ��ȯŸ��
	@Test
	public void returnType() {
		Member member1 = new Member("AAA", 10);
		Member member2 = new Member("BBB", 20);
		memberRepository.save(member1);
		memberRepository.save(member2);
		String [] str = {"AAA", "BBB"};
		
		//list empty üũ �˾Ƽ� �ϹǷ� null �� üũ�ϴ� �ڵ� © �ʿ� ����
		List<Member> aaa = memberRepository.findListByUsername("AAA");
		
		//�� null�̸� spring data jpa�� �˾Ƽ� ����ó������
		Member bbb = memberRepository.findMemberByUsername("AAA");
		
		//���� null���� �ƴ��� Ȯ������ ���� ���� optional ���
		Optional<Member> ccc = memberRepository.findOptionalByUsername("AAA");
	}
	
	@Test
	public void paging() {
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 10));
		memberRepository.save(new Member("member3", 10));
		memberRepository.save(new Member("member4", 10));
		
		int age = 10;
		
		PageRequest pageRequest = PageRequest.of(0,  3, Sort.by(Sort.Direction.DESC, "username"));
		
		Page<Member> page = memberRepository.findByAge(age, pageRequest);
		//slice ���� totalcount �ʿ� �������� ���� ������ ���翩�� �˼�����
		//Slice<Member> page = memberRepository.findByAge(age, pageRequest);
		
		Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName()));
		
		List<Member> content = page.getContent();

		assertThat(content.size()).isEqualTo(3);
		assertThat(page.getTotalElements()).isEqualTo(5);
		assertThat(page.getNumber()).isEqualTo(0);
		assertThat(page.getTotalPages()).isEqualTo(2);
		assertThat(page.isFirst()).isTrue();
		assertThat(page.hasNext()).isTrue();
	}
	
	//�Ʒ��� ���� bulk���� © ��� ������ ���Ӽ� ���ؽ�Ʈ�� ������ �� �޴´ٴ� ��
	@Test
	public void bulkUpdate() {
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 19));
		memberRepository.save(new Member("member3", 20));
		memberRepository.save(new Member("member4", 21));
		memberRepository.save(new Member("member5", 40));
		
		//���⼭ member5 age 41�� update.. ���Ӽ� ���ؽ�Ʈ�� �����ϰ� db�� ������ ����
//		int resultCount = memberRepository.bulkAgePlus(20);
//		
//		//db�� member5 age �� 41�� ����Ǿ����� ���Ӽ� ���ؽ�Ʈ�� ������ 40�� ��
//		List<Member> result = memberRepository.findByUsername("member5");
//		Member member5 = result.get(0);
//		System.out.println("member5" + member5);
		
		//�ذ��
		int resultCount = memberRepository.bulkAgePlus(20);
		em.flush();
		em.clear();
		
		List<Member> result = memberRepository.findByUsername("member5");
		Member member5 = result.get(0);
		System.out.println("member5" + member5);
		
		assertThat(resultCount).isEqualTo(3);
	}
	
	//�ϳ��� transaction������ ���� ���Ӽ� ���ؽ�Ʈ�� �����Ѵ�
	@Test
	public void findMemberLazy() {
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		teamRepository.save(teamA);
		teamRepository.save(teamB);
		Member member1 = new Member("member1", 10, teamA);
		Member member2 = new Member("member2", 10, teamB);
		memberRepository.save(member1);
		memberRepository.save(member2);
		
		em.flush();
		em.clear();
		
		List<Member> members = memberRepository.findAll();
		
		for(Member member : members) {
			System.out.println("member =" + member.getUsername());
		}
	}
	
	@Test
	public void queryHing() {
		//given
		Member member1 = new Member("member1", 10);
		memberRepository.save(member1);	
		em.flush();
		em.clear();
		
		Member findMember = memberRepository.findById(member1.getId()).get();
		findMember.setUsername("member2");		
		
		em.flush();	
//		)
	}
	@Test
	public void Lock() {
		//given
		Member member1 = new Member("member1", 10);
		memberRepository.save(member1);	
		em.flush();
		em.clear();
		
		List<Member> result = memberRepository.findLockByUsername("member1");

	}
	
	@Test
	public void callCustom() {
		List<Member> result = memberRepository.findMemberCustom();
	}
}
