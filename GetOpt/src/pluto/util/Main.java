package pluto.util;

import java.util.Arrays;
import java.util.List;

public class Main {

    private static<T> T[] subArray(T[] array, int begin) {
       return Arrays.copyOfRange(array, begin, array.length);
    }

    public static void main(String[] args) {
	    // write your code here
        GetOpt getOpt = new GetOpt(null, Arrays.asList("name=", "host=", "verbose"));
        List params = getOpt.getOpt(subArray(args, 1));
        System.out.println(params);
    }
}
