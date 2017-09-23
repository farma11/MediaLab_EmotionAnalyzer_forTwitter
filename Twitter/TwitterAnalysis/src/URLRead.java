import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;


public class URLRead {
	String urlName;
	String charCode;
	URL url;
	InputStream inputStream;
	InputStreamReader isReader;
	BufferedReader bufferReader;
	
	public URLRead(String urlname, String code){
		urlName = urlname;
		charCode = code;
		try {
			url = new URL(urlName);
			inputStream = url.openStream();
			isReader = new InputStreamReader(inputStream, charCode);
			bufferReader = new BufferedReader(isReader);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getURLName(){
		return urlName;
	}
	public String getCharCode(){
		return charCode;
	}
	public BufferedReader getBReader(){
		return bufferReader;
	}
	
	/**
	 * URLì«Ç›çûÇ›ópBufferedReaderÇê∂ê¨Ç∑ÇÈÅB
	 */
	static public BufferedReader makeBR(String urlname, String code){
		URLRead fread = new URLRead(urlname, code);
		return fread.getBReader();
	}
	
	public static void main(String[] args){
		String url = "http://fama11.sitemix.jp/MediaLab3/tweet-corpus1.csv";
		BufferedReader br = makeBR( url,"SJIS");
		
		String line;
		try {
			while((line = br.readLine()) != null){
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
