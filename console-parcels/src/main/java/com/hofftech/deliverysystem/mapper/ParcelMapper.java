package com.hofftech.deliverysystem.mapper;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.entity.ParcelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ParcelMapper {

    @Mapping(target = "form", source = "form", qualifiedByName = "toCharArrayMatrixFromString")
    Parcel toDto(ParcelEntity entity);

    @Mapping(target = "form", source = "form", qualifiedByName = "toStringFromCharArrayMatrix")
    ParcelEntity toEntity(Parcel dto);

    List<Parcel> toDto(List<ParcelEntity> entities);

    @Named("toCharMatrix")
    default char[][] toCharMatrix(List<List<Character>> form) {
        if (form == null) {
            return null;
        }

        return form.stream()
                .map(row -> row.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining())
                        .toCharArray())
                .toArray(char[][]::new);
    }

    // Convert char[][] back to List<List<Character>> (for form field)
    @Named("toListMatrix")
    default List<List<Character>> toListMatrix(char[][] form) {
        if (form == null) {
            return null;
        }

        List<List<Character>> result = new ArrayList<>();
        for (char[] row : form) {
            List<Character> rowList = new ArrayList<>();
            for (char c : row) {
                rowList.add(c);
            }
            result.add(rowList);
        }
        return result;
    }

    // Convert String to char[][] (For cases where form is stored as a String in DB)
    @Named("toCharArrayMatrixFromString")
    default char[][] toCharArrayMatrixFromString(String form) {
        if (form == null || form.isEmpty()) {
            return new char[0][0];
        }

        String[] lines = form.split("\n");
        char[][] matrix = new char[lines.length][];

        for (int i = 0; i < lines.length; i++) {
            matrix[i] = lines[i].toCharArray();
        }
        return matrix;
    }

    // Convert char[][] back to String (For cases where form is retrieved as a char[][])
    @Named("toStringFromCharArrayMatrix")
    default String toStringFromCharArrayMatrix(char[][] form) {
        if (form == null || form.length == 0) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (char[] row : form) {
            stringBuilder.append(new String(row)).append("\n");
        }
        return stringBuilder.toString().trim();
    }
}
