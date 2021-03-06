package ag04.hackathon2020.moneyheist.mapper;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ag04.hackathon2020.moneyheist.entity.Heist;
import ag04.hackathon2020.moneyheist.entity.Member;

@Repository
public class MemberMapper {
	
	private SqlSessionFactory sqlSessionFactory;
	
	public MemberMapper(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	public Member findById(Long id) {
		SqlSession session = sqlSessionFactory.openSession();
		Member member = session.selectOne("findMemberById", id);
		session.commit();
		session.close();
		return member;
	}

	public Member findByName(String name) {
		SqlSession session = sqlSessionFactory.openSession();
		Member member = session.selectOne("findMemberByName", name);
		session.commit();
		session.close();
		return member;
	}
	
	public Member findByEmail(String email) {
		SqlSession session = sqlSessionFactory.openSession();
		Member member = session.selectOne("findMemberByEmail", email);
		session.commit();
		session.close();
		return member;
	}
	
	public List<Member> findEligibleMembers(Heist heist) {
		SqlSession session = sqlSessionFactory.openSession();
		List<Member> members = session.selectList("findEligibleMembers", heist);
		session.commit();
		session.close();
		return members;
	}
	
	@Transactional
	public Member save(Member member) {
		SqlSession session = sqlSessionFactory.openSession();
		Long id = member.getId();
		Member existingMember = null;
		if (id != null) {
			existingMember = session.selectOne("findMemberById", id);
			if (existingMember == null) {
				throw new RuntimeException("Unable to update member - member with ID: " + id + " doesn't exist.");
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
