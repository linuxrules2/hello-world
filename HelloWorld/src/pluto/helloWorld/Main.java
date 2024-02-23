package pluto.helloWorld;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

public class Main {

    private final Random rand = new Random();

    public Main() {

    }

    private String getDateString() {
        return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).format(new Date());
    }

    private String bytes2String(byte[] bytes) {

        StringBuilder sb = new StringBuilder();

        for (byte aByte : bytes) {

            if (Character.isLetterOrDigit(aByte)) {
                sb.append((char) aByte);
            } else {
                sb.append(' ');
            }
        }

        return sb.toString();
    }

    private int sizeString2K(String size) {

        char unit = size.charAt(size.length() -1);
        int factor = 1;
        int len = 0;

        // only support K, M and G as unit
        if(Character.isLetter(unit)) {

            switch(unit) {

                case 'K':
                case 'k':
                    factor = 1;
                    break;

                case 'M':
                case 'm':
                    factor = 1024;
                    break;

                case 'G':
                case 'g':
                    factor = 1024 * 1024;
                    break;
            }
        }

        if(Character.isDigit(unit)) {
            len = Integer.parseInt(size);
        }
        else {
            len = Integer.parseInt(size.substring(0, size.length()-1));
        }

        return len * factor;
    }

    private byte[] getRandomBuffer(String size) {

        int bufSize = sizeString2K(size) * 1024;

        byte [] buf = new byte[bufSize];
        rand.nextBytes(buf);

        return buf;
    }

    private String createEmptyNewFile(String dirPath, String size) throws Exception {

        // make sure directory exists
        File dir = new File(dirPath);
        if(!dir.exists()) {
            dir.mkdir();
        }

        String fileName = "test_" + String.valueOf(new Date().getTime()) + "_" + size;
        String fullPath = dirPath + File.separator + fileName;

        File newFile = new File(fullPath);
        newFile.createNewFile();

        return fileName;
    }

    /*
     * create a temporary file with random content, in specified location
     * @return, randomly generated file name
     */
    private String createTempFile(String dirPath, String size) throws Exception {

        long len = sizeString2K(size);

        String fileName = createEmptyNewFile(dirPath, size);
        String fullPath = dirPath + File.separator + fileName;

        try(final BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(fullPath))) {

            for(int i = 0; i<len; i++) {
                byte[] buf = getRandomBuffer("1K");
                bo.write(buf);
            }
        }

        return fileName;
    }

    /*
     *
     */
    private String createTempTextFile(String dirPath, String size) throws Exception {

        long len = sizeString2K(size);

        String fileName = createEmptyNewFile(dirPath, size);
        String fullPath = dirPath + File.separator + fileName;

        try(final FileWriter fw = new FileWriter(fullPath)) {

            for(int i = 0; i<len; i++) {
                byte[] buf = getRandomBuffer("1K");
                fw.write(bytes2String(buf));
            }
        }

        return fileName;
    }

    public static void main(String[] args) throws Exception {

        Main m1 = new Main();
        DatabaseFileStorage dfs = new DatabaseFileStorage();

        System.out.println(m1.getDateString());

        String name = m1.createTempTextFile("/var/tmp/mingtest", "1G");
        System.out.println(m1.getDateString());

        dfs.saveFile(name);
        System.out.println(m1.getDateString());

    }

}
