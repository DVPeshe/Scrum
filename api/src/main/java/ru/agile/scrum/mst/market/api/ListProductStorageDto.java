package ru.agile.scrum.mst.market.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListProductStorageDto {
    private List<ProductStorageDto> list;
}
