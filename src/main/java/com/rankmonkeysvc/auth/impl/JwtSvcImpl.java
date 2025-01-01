package com.rankmonkeysvc.auth.impl;

import com.rankmonkeysvc.auth.JwtSvc;
import com.rankmonkeysvc.dao.User;
import com.rankmonkeysvc.repositories.UserInfoRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import static com.rankmonkeysvc.messages.StaticMessages.*;

@Service
public class JwtSvcImpl implements JwtSvc {
	private final UserInfoRepository userInfoRepository;
	
	@Autowired
	public JwtSvcImpl (
		UserInfoRepository userInfoRepository
	) {
		this.userInfoRepository = userInfoRepository;
	}

    @Override
    public String generateToken(UserDetails user, Long tokenExpiration) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim(EMAIL, user.getUsername())
                .claim(ROLE, populateAuthorities(user.getAuthorities()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateDummyTokenWithHS256(User user, boolean admin) {
        if (admin) {
            return Jwts.builder()
                    .setSubject(user.getUsername())
                    .claim(AUTH_TOKEN, user.getId().toString())
                    .claim(ROLE, populateAuthorities(user.getAuthorities()))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis()))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        } else {
            return null;
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> authoritiesSet = new HashSet<>();
        for(GrantedAuthority authority: authorities) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(COMA_SEPARATED, authoritiesSet);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

	@Override
    public String getUser(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
            String jwt = authorizationHeader.substring(BEARER_INDEX);
			return userInfoRepository.findByEmail(extractUsername(jwt)).get().getId().toString();
        }
        return null;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()));
    }
}