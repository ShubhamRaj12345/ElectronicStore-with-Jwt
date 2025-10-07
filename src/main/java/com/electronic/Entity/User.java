package com.electronic.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


@Entity
@Table(name ="user_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    private String  userId;
    @Column(name ="name")
    private String  name;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name ="password" , length =200)
    private String password;
    @Column(name = "gender", length =6)
    private String gender;
    private  String about;
    @Column(name = "user_imageName")
    private String imageName;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Role> roles= new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Order> orders = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> collect = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

        return collect;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }
    @Override
    public String getPassword() {
        return password;
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
        return true;
    }
}
