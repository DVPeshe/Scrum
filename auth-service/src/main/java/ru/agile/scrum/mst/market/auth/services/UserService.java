package ru.agile.scrum.mst.market.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.agile.scrum.mst.market.api.JwtRequest;
import ru.agile.scrum.mst.market.api.RegistrationUserDto;
import ru.agile.scrum.mst.market.api.RoleTitlesResponse;
import ru.agile.scrum.mst.market.api.UserDtoRoles;
import ru.agile.scrum.mst.market.auth.entities.Avatar;
import ru.agile.scrum.mst.market.auth.entities.Role;
import ru.agile.scrum.mst.market.auth.entities.User;
import ru.agile.scrum.mst.market.auth.exceptions.AccessForbiddenException;
import ru.agile.scrum.mst.market.auth.exceptions.BanUserException;
import ru.agile.scrum.mst.market.auth.exceptions.IncorrectLoginOrPasswordException;
import ru.agile.scrum.mst.market.auth.exceptions.ResourceNotFoundException;
import ru.agile.scrum.mst.market.auth.mappers.UserMapper;
import ru.agile.scrum.mst.market.auth.repositories.UserRepository;
import ru.agile.scrum.mst.market.auth.utils.JwtTokenUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public void auth(JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new IncorrectLoginOrPasswordException("Некорректный логин или пароль");
        }
    }

    public void reg(RegistrationUserDto form) {
        User user = new User();
        user.setEmail(form.getEmail());
        user.setUsername(form.getUsername());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setFullName(form.getFullName());
        user.setAccess(true);

        List<Role> roles = new ArrayList<>();
        roles.add(roleService.getUserRole());
        user.setRoles(roles);

        Avatar avatar = Avatar.builder()
                .avatar(null)
                .user(user)
                .build();
        user.setAvatar(avatar);
        userRepository.save(user);
    }

    public String getToken(UserDetails userDetails) {
        return jwtTokenUtil.generateToken(userDetails);
    }

    public boolean getAccessAdmin(String username) {
        User user = getUserByName(username);
        Collection<Role> rolesUser = user.getRoles();
        return rolesUser.size() > 1;
    }

    public boolean getAccessUserPanel(String username) {
        return containsRoleUser(username, "ROLE_ADMIN");
    }

    public boolean getAccessProductPanel(String username) {
        return containsRoleUser(username, "ROLE_MANAGER");
    }

    public boolean getAccessEditRole(String username) {
        return containsRoleUser(username, "ROLE_SUPERADMIN");
    }

    public Page<User> findAll(int page, int pageSize, Specification<User> specification) {
        Sort sort = Sort.by("username");
        return userRepository.findAll(specification, PageRequest.of(page, pageSize, sort));
    }

    @Transactional
    public void editRole(UserDtoRoles userDtoRoles) {
        User user = getUserByName(userDtoRoles.getUsername());
        List<Role> collect = userDtoRoles.getRoles().stream().map(roleService::getRoleByTitle).toList();
        user.getRoles().clear();
        user.getRoles().addAll(collect);
        userRepository.save(user);
    }

    public User getUserByName(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("Пользователь " + username + " не найден."));
    }

    public boolean containsRoleUser(String username, String nameRole) {
        User userByName = getUserByName(username);
        List<Role> roles = userByName.getRoles();
        for (Role role : roles) {
            if (role.getName().equals(nameRole)) return true;
        }
        return false;
    }

    @Transactional
    public void updateAccessUser(Long id, Boolean flag) {
        User user = userRepository.getById(id);
        if (user.getRoles().stream().map(Role::getName).toList().contains("ROLE_SUPERADMIN")) {
            throw new AccessForbiddenException("Данное действие недопустимо с генеральным директором.");
        }
        if (!flag) user.getRoles().clear();
        else {
            Role roleUser = roleService.getRoleByName("ROLE_USER");
            user.getRoles().add(roleUser);
        }
        user.setAccess(flag);
        userRepository.save(user);
    }

    public User getByName(String name) {
        return userRepository.findByUsername(name).orElseThrow(
                () -> new ResourceNotFoundException("Пользователь " + name + " не найден."));
    }

    public void userFilter(String name) {
        User user = getByName(name);
        if (!user.getAccess()) throw new BanUserException("Вам запрещен доступ");
    }

    public String getUserEmailByName(String name) {
        User user = getByName(name);
        return user.getEmail();
    }

    public String getFullNameByName(String name) {
        return getByName(name).getFullName();
    }

    @Transactional
    public void updateUser(RegistrationUserDto registrationUserDto) {
        JwtRequest jwtRequest = userMapper.mapRegistrationUserDtoToJwtRequest(registrationUserDto);
        auth(jwtRequest);
        User user = getByName(jwtRequest.getUsername());
        if (registrationUserDto.getEmail() != null) {
            user.setEmail(registrationUserDto.getEmail());
        }
        if (registrationUserDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        }
        if (registrationUserDto.getFullName() != null) {
            user.setFullName(registrationUserDto.getFullName());
        }
        userRepository.save(user);
    }

    public RoleTitlesResponse getUserRoles(String username) {
        return RoleTitlesResponse.builder()
                .roleTitles(getByName(username).getRoles().stream().map(Role::getTitle).toList())
                .build();
    }
}