package arquillian.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamReader {

    public static String readAllAndClose(InputStream is) throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            int read;
            while ((read = is.read()) != -1) {
                out.write(read);
            }
            return out.toString();
        }
    }
}
