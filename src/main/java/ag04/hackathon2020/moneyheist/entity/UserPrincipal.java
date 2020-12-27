package ag04.hackathon2020.moneyheist.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Member member;

	public UserPrincipal(Member member) {
		this.member = member;
	}

	public Member getMember() {
		return this.member;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (this.member == null) {
			return null;
		}
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(this.member.getRole().getValue()));
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.member == null ? null : this.member.getPassword();
	}

	@Override
	public String getUsername() {
		return this.member == null ? null : this.member.getName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.member == null ? true : this.member.getActive() == 1;
	}

}
