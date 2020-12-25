package ag04.hackathon2020.moneyheist.mapper;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ag04.hackathon2020.moneyheist.entity.Heist;

@Repository
public class HeistMapper {

	private SqlSessionFactory sqlSessionFactory;
	
	public HeistMapper(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public Heist findByName(String heistName) {
		SqlSession session = sqlSessionFactory.openSession();
		Heist heist = session.selectOne("findHeistByName", heistName);
		session.commit();
		session.close();
		return heist;
	}

	@Transactional
	public Heist save(Heist heist) {
		SqlSession session = sqlSessionFactory.openSession();
		Long id = heist.getId();
		Heist existingHeist = null;
		if (id != null) {
			existingHeist = session.selectOne("findHeistById", id);
			if (existingHeist == null) {
				throw new RuntimeException("Unable to update heist - heist with ID: " + id + " doesn't exist.");
			}
		}
		if (existingHeist == null) {
			session.insert("insertHeist", heist);
		} else {
			session.update("updateHeist", heist);
		}
		session.commit();
		session.close();
		return heist;
	}

}
