package codingFriends_Server.domain.Member.entity;

import codingFriends_Server.domain.SummeryCode.entity.SummeryCode;
import codingFriends_Server.global.auth.oauth.LoginProvider;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
    @Column
    private String email;
    @Column
    private String snsId;
    @Column
    private String user_level;
    @Column
    private String skill_language;
    @Enumerated(EnumType.STRING)
    private LoginProvider loginProvider;
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<SummeryCode> summeryCodeList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // 관리자가 없으므로 ROLE_USER로 설정
        return authorities;
    }


    @Override
    public String getPassword() { // memberPrincipal 클래스에서 인증된 멤버 객체 정보를 받아올 때 보내줌
        return name;
    }

    @Override
    public String getUsername() {
        return snsId;
    } // MemberPrincipal에서 getUsername을 사용하는데 snsId 값이 필요해서 임시로 설정

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
