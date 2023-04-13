package ru.agile.scrum.mst.market.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDto {
    private Long id;
    private String username;
    private List<String> rolesTitle;
    private Boolean access;
}
