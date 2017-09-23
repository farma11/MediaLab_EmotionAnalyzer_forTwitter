import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URL;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Test {
	
	/**
     * PHPのfile_get_contentsもどき
     * urlに指定されたページのHTMLを取得します
     * @param url HTMLを取得したいページのURL
     * @param encode 取得するHTMLページのURL
     * @return 取得したHTML
     */
    public static StringBuffer fileGetContents( String url, String encode ) {
        StringBuffer buffer = new StringBuffer();
        try {
            InputStream is = new URL(url).openStream();
            InputStreamReader isr = new InputStreamReader(is, encode);
            BufferedReader in = new BufferedReader(isr);
            String s = null;
            while ( (s = in.readLine()) != null) {
                buffer.append(s).append("\n");
            }
        } catch ( Exception e ) {
            System.out.println( e.toString() );
            buffer = null;
        } finally {
            return buffer;
        }
    }
 
    /**
     * PHPのfile_get_contentsもどき
     * urlに指定されたページのHTMLを取得します
     * エンコードはJISAutoDetectで自動判別させる（失敗する可能性あり！？）
     * @param url HTMLを取得したいページのURL
     * @return 取得したHTML
     */
    public static StringBuffer fileGetContents( String url ) {
        return fileGetContents( url, "JISAutoDetect" );
    }
	
    public Test(String url) throws FileNotFoundException, IOException {
    	StringBuffer htmlBuffer = Test.fileGetContents( url );
    	StringBuffer htmlBuffer2 = Test.fileGetContents( "https://code.jquery.com/jquery-2.1.1.min.js" );

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        
        String[] action = {
        		"printHelloWorld(", ");"
        };
        String query = "5";

        if (engine != null) {
            try {
            	String ans = htmlBuffer2.toString() + htmlBuffer.toString() + action[0] + query + action[1];
                engine.eval(ans);
            } catch (ScriptException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Test("http://shippoproj1.webcrow.jp/jsTest/test.js");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}