package com.bartolome.aitor.mapper;

import com.bartolome.aitor.dto.CategoryDTO;
import com.bartolome.aitor.model.entities.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toDto(Category entity);
    Category toEntity(CategoryDTO dto);
}
