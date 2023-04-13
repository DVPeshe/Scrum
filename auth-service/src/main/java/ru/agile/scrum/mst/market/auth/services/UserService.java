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
import ru.agile.scrum.mst.market.api.UserDtoRoles;
import ru.agile.scrum.mst.market.auth.entities.Avatar;
import ru.agile.scrum.mst.market.auth.entities.Role;
import ru.agile.scrum.mst.market.auth.entities.User;
import ru.agile.scrum.mst.market.auth.exceptions.*;
import ru.agile.scrum.mst.market.auth.mappers.UserMapper;
import ru.agile.scrum.mst.market.auth.repositories.UserRepository;
import ru.agile.scrum.mst.market.auth.utils.JwtTokenUtil;

import java.util.*;
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

    public void reg(RegistrationUserDto registrationUserDto) {
        if (registrationUserDto.getUsername() == null || registrationUserDto.getPassword() == null
                || registrationUserDto.getConfirmPassword() == null || registrationUserDto.getEmail() == null
                || registrationUserDto.getFullName() == null) {
            throw new FieldsNotNullException("Все поля формы должны быть заполнены");
        }
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            throw new DontMatchPasswordsException("Пароли не совпадают");
        }
        if (existByUsername(registrationUserDto.getUsername())) {
            throw new TheUserAlreadyExistsException("Пользователь с таким именем уже существует");
        }
        if (existByEmail(registrationUserDto.getEmail())) {
            throw new TheUserAlreadyExistsException("Пользователь с таким email уже существует");
        }
        User user = new User();
        user.setEmail(registrationUserDto.getEmail());
        user.setUsername(registrationUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setFullName(registrationUserDto.getFullName());
        user.setAccess(true);
      
        List<Role> roles = new ArrayList<>();
        roles.add(roleService.getUserRole());
        user.setRoles(roles);
      
        user.setRoles(List.of(roleService.getUserRole()));
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
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("Пользователь " + username + " не найден.")
        );
        Collection<Role> rolesUser = user.getRoles();
        return rolesUser.size() > 1;
    }

    public Page<User> findAll(int page, int pageSize, Specification<User> specification) {
        Sort sort = Sort.by("username");
        return userRepository.findAll(specification, PageRequest.of(page, pageSize, sort));
    }

    @Transactional
    public void editRole(UserDtoRoles userDtoRoles) {
        User user = userRepository.findByUsername(userDtoRoles.getUsername()).orElseThrow(
                () -> new ResourceNotFoundException("Пользователь " + userDtoRoles.getUsername() + " не найден."));
        List<Role> collect = userDtoRoles.getRoles().stream().map(roleService::getRoleByTitle).toList();
        user.getRoles().clear();
        user.getRoles().addAll(collect);
        userRepository.save(user);
    }

    @Transactional
    public void updateAccessUser(Long id, Boolean flag) {
        User user = userRepository.getById(id);
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

    public List<String> getUserRoles(String username) {
        return getByName(username).getRoles().stream().map(Role::getTitle).toList();
    }
}