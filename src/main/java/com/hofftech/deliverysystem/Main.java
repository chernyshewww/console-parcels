package com.hofftech.deliverysystem;

import com.hofftech.deliverysystem.controller.AppController;

public class Main {

    public static void main(String[] args) {
        try {
            AppController appController = new AppController();
            appController.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
