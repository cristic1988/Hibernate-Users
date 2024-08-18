package org.example.app.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserUpdateView {

    public Map<String, String> getData() {
        Scanner scanner = new Scanner(System.in);
        Map<String, String> data = new HashMap<>();
        System.out.print("Enter user's ID: ");
        data.put("id", scanner.nextLine().trim());
        System.out.print("Enter name: ");
        data.put("name", scanner.nextLine().trim());
        System.out.print("Enter email in format example@mail.com: ");
        data.put("email", scanner.nextLine().trim());
        return data;
    }

    public void getOutput(String output) {
        System.out.println(output);
    }
}
