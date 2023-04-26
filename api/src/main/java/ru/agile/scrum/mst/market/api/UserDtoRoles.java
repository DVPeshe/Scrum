package ru.agile.scrum.mst.market.api;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserDtoRoles {
    private List<String> roles;
}
