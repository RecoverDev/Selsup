package ru.selsup.dev;

import java.util.List;

public class ConsoleVisitorResult implements Visitor<String> {

    @Override
    public void out(List<String> values) {
        for (String s : values) {
            System.out.println(s);
        }
    }

}
