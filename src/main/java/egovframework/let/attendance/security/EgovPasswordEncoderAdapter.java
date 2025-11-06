package egovframework.let.attendance.security;

import org.egovframe.rte.fdl.cryptography.EgovPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EgovPasswordEncoderAdapter implements PasswordEncoder {
	private final EgovPasswordEncoder delegate;

	public EgovPasswordEncoderAdapter(EgovPasswordEncoder delegate) {
		this.delegate = delegate;
	}

	@Override
	public String encode(CharSequence rawPassword) {
		return delegate.encryptPassword(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return delegate.checkPassword(encodedPassword, rawPassword.toString());
	}
}
