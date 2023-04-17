package ru.agile.scrum.mst.market.comment.securityConfigs;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestSecurityFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String usernameHeader = request.getHeader("username");
        String userRolesHeader = request.getHeader("roles");

        if (usernameHeader != null && userRolesHeader != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            List<String> userListRoles = convertStringToListString(userRolesHeader);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(usernameHeader,
                    null, userListRoles.stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList()));
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(request, response);
    }

    private List<String> convertStringToListString(String roles) {
        roles = roles.replace("[", "").replace("]", "");
        return Arrays.stream(roles.split(", ")).toList();
    }
}
