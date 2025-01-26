package com.hofftech.deliverysystem.util;

import com.hofftech.deliverysystem.model.Parcel;

import java.util.List;

public class ParcelHelper {
    public static Parcel createParcel(String name, List<String> form, Character symbol) {
        Parcel parcel = new Parcel();
        parcel.setName(name);

        char[][] formMatrix = new char[form.size()][];
        for (int i = 0; i < form.size(); i++) {
            formMatrix[i] = form.get(i).toCharArray();
        }
        parcel.setForm(formMatrix);

        parcel.setSymbol(symbol);
        return parcel;
    }
}
