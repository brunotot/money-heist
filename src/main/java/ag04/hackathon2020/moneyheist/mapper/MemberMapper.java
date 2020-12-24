package ag04.hackathon2020.moneyheist.mapper;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.entity.MemberSkill;
import ag04.hackathon2020.moneyheist.entity.Skill;

@Repository
public class MemberMapper {
	
	private SqlSessionFactory sqlSessionFactory;
	
	private SkillMapper skillMapper;
	
	public MemberMapper(SkillMapper skillMapper, SqlSessionFactory sqlSessionFactory) {
		this.skillMapper = skillMapper;
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	public Member findByEmail(String email) {
		SqlSession session = sqlSessionFactory.openSession();
		Member member = session.selectOne("findMemberByEmail", email);
		session.commit();
		session.close();
		return member;
	}
	
	public Member save(Member member) {
		SqlSession session = sqlSessionFactory.openSession();
		Skill mainSkill = member.getMainSkill();		
		Long id = member.getId();
		Member existingMember = null;
		if (id != null) {
			existingMember = session.selectOne("findMemberById", id);
			if (existingMember == null) {
				throw new RuntimeException("Unable to update member - member with ID: " + id + " doesn't exist.");
			}
		}
		List<MemberSkill> memberSkills = member.getMemberSkills();
		if (memberSkills != null && !memberSkills.isEmpty()) {
			for (MemberSkill memberSkill : memberSkills) {
				Skill currentSkill = memberSkill.getSkill();
				Skill savedSkill = skillMapper.save(currentSkill);
				memberSkill.setSkill(savedSkill);
				if (mainSkill != null && savedSkill.getName().equals(mainSkill.getName())) {
					mainSkill.setId(savedSkill.getId());
				}
			}
		}
		if (existingMember == null) {
			session.insert("insertMember", member);
		} else {
			session.update("updateMember", member);
		}
		session.commit();
		session.close();
		return member;
	}
	
}
