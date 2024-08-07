package dev.wuason.mechanics.app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        if(System.console() == null){
            JOptionPane.showMessageDialog(null, "This is a plugin for a minecraft spigot, please run the plugin in a server.");
        }

        else {
            System.out.println("This is a plugin for a minecraft spigot, please run the plugin in a server.");
        }

    }
}