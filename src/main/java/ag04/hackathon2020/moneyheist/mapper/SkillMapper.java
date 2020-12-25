package ag04.hackathon2020.moneyheist.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ag04.hackathon2020.moneyheist.entity.Skill;

@Repository
public class SkillMapper {
	
	private SqlSessionFactory sqlSessionFactory;
	
	public SkillMapper(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	@Transactional
	public List<Skill> save(List<Skill> skills) {
		return skills.stream()
				.map(s -> save(s))
				.collect(Collectors.toList());
	}
	
	@Transactional
	public Skill save(Skill skill) {
		SqlSession session = sqlSessionFactory.openSession();
		Long id = skill.getId();
		Skill existingSkill = null;
		if (id != null) {
			existingSkill = session.selectOne("findSkillById", id);
			if (existingSkill == null) {
				throw new RuntimeException("Unable to update skill - skill with ID: " + id + " doesn't exist.");
			}
		}
		if (existingSkill == null) {
			existingSkill = session.selectOne("findSkillByName", skill.getName());
			if (existingSkill == null) {
				session.insert("insertSkill", skill);
			} else {
				skill.setId(existingSkill.getId());
			}
		} else {
			session.update("updateSkill", skill);
		}
		session.commit();
		session.close();
		return skill;
	}
	
}
