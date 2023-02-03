import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;



public class BuildFunction {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int len= scanner.nextInt();
        int n=pow(len);
        int operationCount=0;
        String[] values= new String[n];
        int countOne=0;
        for(int i=0;i<n;i++){
            String tempStr=scanner.nextWord();
            int tempInt= scanner.nextInt();
            if(tempInt==1){
                values[countOne]=tempStr;
                countOne++;
                char[] xn=tempStr.toCharArray();
                for(int j=0;j<len;j++) {
                    if (xn[j] == '0') {
                        operationCount++;
                    }
                }
            }
        }
        if(countOne==0){
            System.out.println(2+len);
            System.out.println(1 + " "+ 1);
            System.out.println(2 + " " + 1 + " " + (len+1));
        } else{
            System.out.println(operationCount+len+(len-1)*countOne+countOne-1);
        }
        operationCount=len;
        ArrayList<Integer> lastOp= new ArrayList<>();
        for(int i=0;i<countOne;i++){
            char[] xn=values[i].toCharArray();
            int[] operations= new int[len];
            for(int j=0;j<len;j++) {
                operations[j] = j;
                if (xn[j] == '0') {
                    //System.out.print(operationCount + 1 + ")  ");
                    System.out.println(1 + " " + (j + 1));
                    operations[j] = operationCount;
                    operationCount++;
                    //n+i+j+1 операция
                }
                //System.out.println(Arrays.toString(operations));
            }
            for(int j=1;j<len;j++){
                //System.out.print(operationCount+1+")  ");
                if((operations[j-1]+1)<=(operations[j]+1)) {
                    System.out.println(2 + " " + (operations[j - 1] + 1) + " " + (operations[j] + 1));
                } else{
                    System.out.println(2 + " " + (operations[j] + 1) + " " + (operations[j - 1] + 1));
                }
                operations[j]=operationCount;
                operationCount++;
                //System.out.println(Arrays.toString(operations));
            }
            lastOp.add(operationCount);
        }

        for(int i=0;i< lastOp.size()-1;i++){
            //System.out.print(operationCount+1+")  ");
            if(operationCount<=lastOp.get(i)) {
                System.out.println(3 + " " + operationCount + " " + lastOp.get(i));
            } else{
                System.out.println(3 + " " + lastOp.get(i)+ " " + operationCount);
            }
            operationCount++;
        }

    }
    private static int pow(int n) {
        int res = 1;
        for (int i = 0; i < n; i++) {
            res *= 2;
        }
        return res;
    }

}




class Scanner implements AutoCloseable {
    private final Reader reader;
    private final int BUFFER_SIZE = 128;
    private int size = 0;
    private int pos = 0;
    private boolean lineEnd = false;
    private char[] buffer = new char[BUFFER_SIZE];

    public Scanner(File file) throws IOException {
        this.reader = new FileReader(file, StandardCharsets.UTF_8);

    }

    public Scanner(InputStream inputStream) {
        this.reader = new InputStreamReader(inputStream);
    }

    public Scanner(String s) {
        this.reader = new StringReader(s);
    }

    private static boolean wordChar(char s) {
        return ((Character.DASH_PUNCTUATION == Character.getType(s)) || Character.isLetter(s) || Character.isDigit(s) || s == '\'');
    }

    private static boolean intChar(char s) {
        return Character.isDigit(s);
    }

    private static boolean lineChar(char current) {
        return !(current == '\n' || current == '\r');
    }

    public static boolean correctSymbol(char current, Type type, boolean startSymbol) {
        switch (type) {
            case WORD -> {
                return wordChar(current);
            }
            case INT -> {
                return intChar(current);
            }
            case LINE -> {
                if (startSymbol) {
                    return true;
                } else {
                    return lineChar(current);
                }
            }
            default -> {
                return false;
            }
        }
    }

    public String token(Type type) throws IOException {
        StringBuilder tempStr = new StringBuilder();
        if (hasNextToken(type)) {
            while (pos < size && correctSymbol(buffer[pos], type, false) && lineChar(buffer[pos])) {
                tempStr.append(buffer[pos]);
                pos++;
                if (pos == size) {
                    read();
                }
            }
            if (pos >= size) {
                return tempStr.toString();
            } else {
                if (!correctSymbol(buffer[pos], type, false) && lineChar(buffer[pos])) {
                    return tempStr.toString();
                }
                if (!lineChar(buffer[pos])) {
                    lineEnd = true;
                    if (buffer[pos] == '\r') {
                        pos++;
                        if (pos == buffer.length) {
                            read();
                        }
                        if (buffer[pos] == '\n') {
                            pos++;
                        }
                    } else {
                        pos++;
                    }
                    return tempStr.toString();
                }
            }

        }
        throw new NoSuchElementException();
    }

    private boolean hasNextToken(Type type) throws IOException {
        if (pos == size) {
            read();
            if (size == -1) {
                return false;
            }
        }
        while (pos < size) {
            if (correctSymbol(buffer[pos], type, true)) {
                return true;
            } else {
                if (!lineChar(buffer[pos])) {
                    lineEnd = true;
                }
                pos++;
                if (pos == size) {
                    if (size == buffer.length) {
                        read();
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public int nextInt() throws IOException {
        return Integer.parseInt(token(Type.INT));
    }

    public String nextWord() throws IOException {
        return token(Type.WORD);
    }

    public String nextLine() throws IOException {
        return token(Type.LINE);
    }

    public boolean hasNext() throws IOException {
        return (hasNextToken(Type.INT) || hasNextToken(Type.WORD) || hasNextToken(Type.LINE));
    }

    public boolean hasNextLine() throws IOException {
        return hasNextToken(Type.LINE);
    }

    public boolean hasNextWord() throws IOException {
        return hasNextToken(Type.WORD);
    }

    public boolean hasNextInt() throws IOException {
        return hasNextToken(Type.INT);
    }

    public boolean isLineEnd() {
        if (lineEnd) {
            lineEnd = false;
            return true;
        }
        return false;
    }

    private void read() throws IOException {
        buffer = new char[BUFFER_SIZE];
        size = reader.read(buffer);
        pos = 0;
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException ignored) {
        }
    }

    public enum Type {
        WORD, INT, LINE
    }
}