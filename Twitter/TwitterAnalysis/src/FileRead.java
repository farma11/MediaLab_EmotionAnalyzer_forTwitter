import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class FileRead {
	String fileName;
	String charCode;
	FileInputStream inputStream;
	InputStreamReader isReader;
	BufferedReader bufferReader;
	
	public FileRead(String fname, String code){
		fileName = fname;
		charCode = code;
		try {
			inputStream = new FileInputStream(fileName);
			isReader = new InputStreamReader(inputStream, charCode);
			bufferReader = new BufferedReader(isReader);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public String getFileName(){
		return fileName;
	}
	public String getCharCode(){
		return charCode;
	}
	public BufferedReader getBReader(){
		return bufferReader;
	}
	
	/**
	 * ファイル読み込み用BufferedReaderを生成する。
	 */
	static public BufferedReader makeBR(String fname, String code){
		FileRead fread = new FileRead(fname, code);
		return fread.getBReader();
	}
	
	public static void main(String[] args){
		String fname = "D:\\Twitter\\TwitterAnalysis\\samples\\SampleTL1.txt";
		BufferedReader br = makeBR( fname,"UTF-8");
		
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
