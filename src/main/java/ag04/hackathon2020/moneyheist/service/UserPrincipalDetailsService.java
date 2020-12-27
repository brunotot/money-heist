package ag04.hackathon2020.moneyheist.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.entity.UserPrincipal;
import ag04.hackathon2020.moneyheist.mapper.MemberMapper;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {

	private MemberMapper memberMapper;

	public UserPrincipalDetailsService(MemberMapper memberMapper) {
		super();
		this.memberMapper = memberMapper;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberMapper.findByName(username);
		UserPrincipal userPrincipal = new UserPrincipal(member);
		return userPrincipal;
	}

}