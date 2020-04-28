package org.noear.water.utils;

import java.io.InputStream;
import java.util.Scanner;

public class IOUtils {
    public static String toString(InputStream stream){
        Scanner scanner = new Scanner(stream, "UTF-8");
        String text = scanner.next();
        scanner.close();
        return text;
    }
}
