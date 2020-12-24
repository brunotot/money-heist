package ag04.hackathon2020.moneyheist.mapper;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import ag04.hackathon2020.moneyheist.entity.Skill;

@Repository
public class SkillMapper {
	
	private SqlSessionFactory sqlSessionFactory;
	
	public SkillMapper(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
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
			session.insert("insertSkill", skill);
		} else {
			session.update("updateSkill", skill);
		}
		session.commit();
		session.close();
		return skill;
	}
	
}
