//package com.cms.Service;
//
//import com.cms.Enum.Roles;
//import com.cms.Model.User;
//import com.cms.Repository.UserRepository;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Collection;
//import java.util.List;
//
//@Service
//public class CustomUserDetailsService implements UserDetails {
//
//    private final UserRepository userRepository;
//
//    public CustomUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
////    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUserId(Integer.parseInt(username));
////                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        return new CustomUserDetails(user);
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//
//    @Override
//    public String getPassword() {
//        return null;
//    }
//
//    @Override
//    public String getUsername() {
//        return null;
//    }
//
//    private class CustomUserDetails implements UserDetails {
//
//        private final User user;
//
//        public CustomUserDetails(User user) {
//            this.user = user;
//        }
//
//        @Override
//        public Collection<? extends GrantedAuthority> getAuthorities() {
//            Roles role = user.getRole();
//            if (role == Roles.ROLE_ADMIN) {
//                return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
//            } else if (role == Roles.ROLE_CLIENT) {
//                return List.of(new SimpleGrantedAuthority("ROLE_CLIENT"));
//            } else {
//                return List.of();
//            }
//        }
//
//        @Override
//        public String getPassword() {
//            return user.getPassword();
//        }
//
//        @Override
//        public String getUsername() {
//            return String.valueOf(user.getUserId());
//        }
//
//        @Override
//        public boolean isAccountNonExpired() {
//            return true;
//        }
//
//        @Override
//        public boolean isAccountNonLocked() {
//            return true;
//        }
//
//        @Override
//        public boolean isCredentialsNonExpired() {
//            return true;
//        }
//
//        @Override
//        public boolean isEnabled() {
//            return user.getStatus() == 1;
//        }
//    }
//
//}
