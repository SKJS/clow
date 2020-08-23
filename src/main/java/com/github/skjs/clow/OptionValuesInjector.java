package com.github.skjs.clow;


import org.apache.commons.cli.*;

import java.lang.reflect.Field;

public class OptionValuesInjector {
    public static void parseArgs(Class main, String[] args) {
        Options options = new Options();
        for (Field field : main.getFields()) {
            CommandLineOption clo = field.getAnnotation(CommandLineOption.class);
            if (clo != null) {
                Option.Builder builder = Option.builder(clo.name());
                builder.required(clo.isRequired()).longOpt(clo.longName()).desc(clo.description()).valueSeparator(clo.valueSeparator());
                if (field.getType() == String[].class) {
                    builder.hasArgs();
                }
                else if (field.getType() == String.class) {
                    builder.hasArg();
                }
                options.addOption(builder.build());
            }
        }

        CommandLine commandLine = null;

        try {
            commandLine = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            new HelpFormatter().printHelp(main.getCanonicalName(), options);
            System.exit(1);
        }

        for (Field field : main.getFields()) {
            CommandLineOption clo = field.getAnnotation(CommandLineOption.class);
            if (clo != null) {
                try {
                    if (field.getType() == String[].class) {
                        field.set(null, commandLine.getOptionValues(clo.name()));
                    }
                    else if (field.getType() == String.class) {
                        field.set(null, commandLine.getOptionValue(clo.name()));
                    }
                    else if (field.getType() == Boolean.class) {
                        field.set(null, commandLine.hasOption(clo.name()));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
