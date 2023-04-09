package ru.agile.scrum.mst.market.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {

    private String id;

    private String title;

    private byte[] image;
}
