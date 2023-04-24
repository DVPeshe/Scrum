package ru.agile.scrum.mst.market.api;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {

    private String fieldName;
    private String description;
}
