package ru.agile.scrum.mst.market.api;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserDtoRoles {
    private String username;
    private List<String> roles;
}
