package com.spring.seoulmoaapi.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.seoulmoaapi.domain.TimeStamp;
import com.spring.seoulmoaapi.domain.memberInteraction.entity.MemberCategory;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends TimeStamp implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String nickname;

    private int age;

    private String gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.ROLE_USER;

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
//    private List<MemberCategory> memberInterests = new ArrayList<>();

    @Builder
    public Member(String username, String password, String nickname, int age, String gender, Role role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.role = role != null ? role : Role.ROLE_USER;
    }
    public static Member of(String username, String encodedPassword, String nickname, int age, String gender) {
        return com.spring.seoulmoaapi.domain.member.entity.Member.builder()
                .username(username)
                .password(encodedPassword)
                .nickname(nickname)
                .age(age)
                .gender(gender)
                .role(Role.ROLE_USER)
                .build();
    }

    public void updateInfo(String nickname, Integer age, String gender) {
        if (nickname != null) this.nickname = nickname;
        if (age != null) this.age = age;
        if (gender != null) this.gender = gender;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + role);
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    @Override
    public String toString() {
        return "Member{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", role=" + role +
                '}';
    }

}