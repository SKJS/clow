package com.github.skjs.clow;

public class Main {

    public static int i = 1;

    @CommandLineOption(name = "s", longName = "single", isRequired = true)
    public static String singleValue;
//    @CommandLineOption(name = "f", longName = "flag", isRequired = true)
    public static boolean flag;


    public static void main(String[] args) {
        OptionValuesInjector.parseArgs(Main.class, args);
        System.out.println(singleValue);
        System.out.println(flag);
    }
}
