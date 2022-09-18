package pluto.helloWorld;

import java.text.DateFormat;
import java.util.Date;

public class Main {
    private String name;

    private static class Inner {
        private static String m_str = getDateString();
    }

    private static String getDateString() {
        return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).format(new Date());
    }

    public Main() {
        this.name = Inner.m_str;
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) throws Exception {

        Main m1 = new Main();
        Thread.sleep(2000);
        Main m2 = new Main();

        System.out.println("Hello World!");
        System.out.println(m1.getName());
        System.out.println(m2.getName());
        System.out.println("Hello World Again!");
    }
}
