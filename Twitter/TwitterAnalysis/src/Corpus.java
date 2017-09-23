import java.io.*;
import java.util.*;

public class Corpus {
	String text;
	Emotion emotions;
	
	enum Emotions{
		Joy,Trust,Fear,Surprise,Sad,Disgust,Anger,Anticipate,
	}
	
	/**
     * コンストラクタ
     */ 
	public Corpus(String txt, double[] emo){
		text = txt;
		emotions = new Emotion(emo);
	}
	
	/**
     * CSVファイルの読み込みによるコンストラクタ
     */ 
	public Corpus(String lineCSV){
		String[] nums = lineCSV.split(",", 0);
		text = nums[0];
		double[] emos = {
    			Double.parseDouble(nums[1]),Double.parseDouble(nums[2]),
    			Double.parseDouble(nums[3]),Double.parseDouble(nums[4]),
    			Double.parseDouble(nums[5]),Double.parseDouble(nums[6]),
    			Double.parseDouble(nums[7]),Double.parseDouble(nums[8])
    			};
		emotions = new Emotion(emos);
	}
	
	/**
     * このテキストの内容を返す
     */
    public String getText() {
        return text;
    }
    
    /**
     * このテキストの感情値の配列を返す
     */
    public double[] getEmotionsArray() {
        return emotions.getEmotions();
    };
    
    /**
     * このテキストの感情値のEmotionクラスを返す
     */
    public Emotion getEmotions() {
        return emotions;
    };
    
    /**
     * このテキストの各感情値を返す
     */
    public double getEmotion(int i) {
    	// 列挙型Emotionの範囲内であればその感情値の値をint型で返す。
    	if(i >= 0 && i < EnumSet.allOf(Emotions.class).size()) return emotions.getEmotion(i);
    	else return -1.0;
    }
    
    /**
     * このテキストの各感情値を更新する
     */
    public int changeEmotion(int i, double val) {
    	// 列挙型Emotionの範囲内であればその感情値の値をint型で返す。
    	if(i >= 0 && i < EnumSet.allOf(Emotions.class).size()){
    		emotions.setEmotion(i,val);
    		return 0;
    	}
    	else return -1;
    }
    
  //比較メソッド（データクラスを比較して-1, 0, 1を返すように記述する）
    public int compare(Corpus a, Corpus b) {
    	// a>b:正, a==b:0, a<b:負の値が返される
        int x = a.getText().compareTo(b.getText());

        //昇順でソートされる
        if (x > 0) return 1;
        else if (x == 0) return 0;
        else return -1;
    }
    
    
    public static void main(String[] args){
        try {
        	// ファイル入力の準備
        	String fname = "D:\\Twitter\\TwitterAnalysis\\samples\\SampleCorpus.csv";
    		BufferedReader br = FileRead.makeBR(fname, "SJIS");

        	//１行ごとにファイル入力
			List<Corpus> corpora = new ArrayList<Corpus>();;
			String line;
            while( ( line = br.readLine() ) != null ){
            	corpora.add(new Corpus(line));
            }
         	br.close();
         	
         	// 入力した形態素のソート
         	Collections.sort(corpora, new Comparator<Corpus>(){
         		public static final int ASC = 1;   //昇順 (1.2.3....)
                public static final int DESC = -1; //降順 (3.2.1....)
         		public int compare(Corpus a, Corpus b) {
         			int sortType = ASC;
	                return sortType * a.getText().compareTo(b.getText());
         		}
         	});
            
            for(Corpus co : corpora){
            	System.out.print( co.getText() );
            	for(double em : co.emotions.getEmotions()){
            		System.out.print("," + em);
            	}
            	System.out.println();
            }

		} catch (IOException e) {
			e.printStackTrace();
		}

    }
    
}



