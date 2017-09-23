import java.io.*;
import java.util.*;

public class TwitCorpus extends Corpus{
	HashMap<String, Integer> morphsCount = new HashMap<String, Integer>();
	int morph_num;
	
	public TwitCorpus(String str, double[] emo) {
		super(str, emo);
		
		// 文字列を形態素解析
        ArrayList<Morpheme> morphs = analyzeMorpheme(str);
        morph_num = morphs.size();
        for(Morpheme morph : morphs){
        	if(morphsCount.containsKey(morph.base)){
        		morphsCount.put(morph.base, morphsCount.get(morph.base) + 1);
        	} else {
        		morphsCount.put(morph.base, 1);
        	}
        }
	}
	
	public TwitCorpus(String str) {
		super(str);
		
		// 文字列を形態素解析
        ArrayList<Morpheme> morphs = analyzeMorpheme(str.substring(0,str.indexOf(",")));
        for(Morpheme morph : morphs){
        	if(morphsCount.containsKey(morph.base)){
        		morphsCount.put(morph.base, morphsCount.get(morph.base) + 1);
        	} else {
        		morphsCount.put(morph.base, 1);
        	}
        }
	}

	/**
     * 形態素解析の結果をMorphemeオブジェクトのリストにして返す
     */
    static ArrayList<Morpheme> analyzeMorpheme(String str) {
        return Morpheme.analyzeMorpheme(str);   
    }
    
    /**
     * 形態素別の個数を保持したHashMapを返す
     */
    public HashMap<String, Integer> getMorphsCount() {
        return morphsCount;
    }
    
    
    public static void main(String[] args){
        try {
        	// ファイル入力の準備
        	String url = "http://fama11.sitemix.jp/MediaLab3/tweet-corpus1.csv";
    		BufferedReader br = URLRead.makeBR( url,"SJIS");

        	//１行ごとにファイル入力
			List<TwitCorpus> tweets = new ArrayList<TwitCorpus>();;
			String line;
            while( ( line = br.readLine() ) != null ){
            	tweets.add(new TwitCorpus(line));
            }
         	br.close();
         	
            
            for(TwitCorpus co : tweets){
            	System.out.print( co.getText() );
            	for(double em : co.emotions.getEmotions()){
            		System.out.print("," + em);
            	}
            	System.out.println();
            	
            	int newline = 0;
            	for(Map.Entry<String, Integer> entry : co.morphsCount.entrySet()) {
            	    System.out.print(entry.getKey() + ": " + entry.getValue()+ " ");
            	    
            	    newline++;
            	    if(newline % 10 == 0) System.out.println();
            	}
            	System.out.println("形態素数: " + co.morphsCount.size());
        	    System.out.println("---------------------------------------------");
            }
            

		} catch (IOException e) {
			e.printStackTrace();
		}

    }
}
