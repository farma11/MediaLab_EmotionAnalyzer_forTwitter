import java.util.EnumSet;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;

public class Emotion {
	double[] emotions;
	
	// �ő�̊���l
	int maxEmotionIndex;
	
	// �ŏ��̊���l
	int minEmotionIndex;
	

	enum EmotionKinds{
		Joy,Trust,Fear,Surprise,Sad,Disgust,Anger,Anticipate,
	}
	String[] EmotionNames = {
		"Joy","Trust","Fear","Surprise","Sad","Disgust","Anger","Anticipate"
	};
	
	/**
     * �f�t�H���g�R���X�g���N�^
     */ 
	public Emotion(){
		double[] emos = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		emotions = emos;
		maxEmotionIndex = minEmotionIndex = 0;
	}
	
	/**
     * ���l�ɂ��R���X�g���N�^
     */ 
	public Emotion(double[] emos){
		emotions = emos;
		setMaxMin(emos);
	}
	
	/**
	 * �Y����idx�ɑΉ����銴��l��Ԃ�
	 * @param idx
	 * @return emotions[idx]
	 */
	public double getEmotion(int idx){
		return emotions[idx];
	}
	
	
	/**
	 * �ő�ŏ��̍X�V
	 * @param emos
	 */
	public void setMaxMin(double[] emos){
		maxEmotionIndex = minEmotionIndex = 0;
		for(int i = 0; i < EnumSet.allOf(EmotionKinds.class).size(); i++){
			if(emotions[maxEmotionIndex] < emos[i]){
				maxEmotionIndex = i;
			} else if (emotions[minEmotionIndex] > emos[i]){
				minEmotionIndex = i;
			}
		}
	}
	
	/**
	 * �Y����idx�ɑΉ����銴��l��setValue�ɕύX����
	 * @param idx
	 * @param setValue
	 */
	public void setEmotion(int idx, double setValue){
		emotions[idx] = setValue;
		
		// ����l�̍ő�ŏ����X�V
		setMaxMin(emotions);
	}
	public double[] getEmotions(){
		return emotions;
	}
	
	// �ő�ɂ��Ă̎擾
	public double getMaxEmotionValue(){
		return emotions[maxEmotionIndex];
	}
	public int getMaxEmotionIndex(){
		return maxEmotionIndex;
	}
	public String getMaxEmotionName(){
		return EmotionNames[maxEmotionIndex];
	}
	
	// �ŏ��ɂ�������擾
	public double getMinEmotionValue(){
		return emotions[minEmotionIndex];
	}
	public int getMinEmotionIndex(){
		return minEmotionIndex;
	}
	public String getMinEmotionName(){
		return EmotionNames[minEmotionIndex];
	}
	
	public static void main(String[] args) {
		Emotion test = new Emotion();
		test.setEmotion(1, 3.0);
		test.setEmotion(3,-1.0);
		test.setEmotion(0, 2.0);
		test.setEmotion(1,-3.0);
		
		printEmotions(test);
		
		
		double[] e = {
			2.0, -0.9, 0.0, 0.0, 4.9, 0.0, 1.0, -2.3 	
		};
		Emotion test2 = new Emotion(e);
		printEmotions(test2);
		toJSonWEB(test2);

	}
	
	static public void printEmotions(Emotion emo){
		for(int i = 0; i < EnumSet.allOf(EmotionKinds.class).size(); i++){
			System.out.print(emo.getEmotion(i) + ", ");
		}
		System.out.println();
		System.out.println("Max: " + emo.getMaxEmotionName() + "  Score = "+ emo.getMaxEmotionValue());
		System.out.println("Min: " + emo.getMinEmotionName() + "  Score = "+ emo.getMinEmotionValue());
	}
	
	static public String printEmotions_lite(Emotion emo){
		String str = "Max: " + emo.getMaxEmotionName() + " Min: " + emo.getMinEmotionName();
		return str;
	}
	
	static public void writeEmotions(Emotion emo){
	      File file = new File("D:\\Twitter\\TwitterAnalysis\\samples\\update.csv");

	      if (checkBeforeWritefile(file)){
	    	  try {
	    		  	PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
			        for(int i = 0; i < EnumSet.allOf(EmotionKinds.class).size(); i++){
						pw.print(emo.getEmotion(i) + ", ");
					}
			        pw.println();
			        
			        pw.close();
		      
		      } catch (IOException e) {
					// TODO �����������ꂽ catch �u���b�N
					e.printStackTrace();
		      }
	      }else{
		        System.out.println("�t�@�C���ɏ������߂܂���");
		  }
	}
	
	private static boolean checkBeforeWritefile(File file){
	    if (file.exists()){
	      if (file.isFile() && file.canWrite()){
	        return true;
	      }
	    }
	    return false;
 }
	
	/**
	 * 2��Emotion�N���X�̘a(a + b)
	 * @param a
	 * @param b
	 * @return
	 */
	static public Emotion add(Emotion a, Emotion b){
		double[] temp = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		for(int i = 0; i < 8; i++){
			temp[i] = a.getEmotion(i) + b.getEmotion(i);
		}
		
		Emotion ans = new Emotion(temp);
		return ans;
	}
	
	/**
	 * 2��Emotion�N���X�̍�(a - b)
	 * @param a
	 * @param b
	 * @return
	 */
	static public Emotion distance(Emotion a, Emotion b){
		double[] dist = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		for(int i = 0; i < 8; i++){
			dist[i] = a.getEmotion(i) - b.getEmotion(i);
		}
		
		Emotion ans = new Emotion(dist);
		return ans;
	}
	
	/**
	 * ����̍ő�l��Web���JavaScript�ɑ���
	 */
	static public void toJSonWEB(Emotion emo){
		Desktop dt = Desktop.getDesktop();
		String uriStr = "http://shippoproj1.webcrow.jp/EmotionMoving/index.html#" 
				+ emo.getMaxEmotionIndex() + "&" + emo.getMaxEmotionValue();
		
		//String uriStr = "https://shippoproject.zugyuuun.com/index.html#key="
		//		+ emo.getMaxEmotionName() + "&value=" + emo.getMaxEmotionValue();

		
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