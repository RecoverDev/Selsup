package ru.selsup.dev;

import java.util.concurrent.Callable;

public class TestTask implements Callable<String> {
    private String strNum;

    public TestTask(String num) {
        this.strNum = num;
    }


    @Override
    public String call() {
        System.out.printf("Запустили задание %s\n", strNum);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Завершили задаиние %s\n", strNum);
        return strNum;
        
    }

}
