import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Test_browser {

	public static void main(String[] args) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		Desktop dt = Desktop.getDesktop();
		String uriStr = "http://shippoproj1.webcrow.jp/jsTest/index.html?key=French";
		
		try{
			URI uri = new URI(uriStr);
			dt.browse(uri);
		} catch(URISyntaxException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		
	}

}
