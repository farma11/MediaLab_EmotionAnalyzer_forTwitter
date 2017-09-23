import java.io.*;
import java.util.*;
import java.text.*;

public class Tweet {
	Date date;		//日時
	String name;	//アカウント名
	String id;		//アカウントのID
	String text;	//ツイート本文
	// ツイート内の形態素のリスト
	ArrayList<Morpheme> morphs;
	// 形態素別の個数
	HashMap<String, Integer> morphsCount = new HashMap<String, Integer>();
	
	/**
     * コンストラクタ
     */ 
	public Tweet(String dates, String accountName, String accountId, String txt){
		SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try {
			date = DateFormat.parse(dates);
			name = accountName;
			id = accountId;
			text = txt;
			morphs = Morpheme.analyzeMorpheme(txt);
			
			for(Morpheme morph : morphs){
	        	if(morphsCount.containsKey(morph.base)){
	        		morphsCount.put(morph.base, morphsCount.get(morph.base) + 1);
	        	} else {
	        		morphsCount.put(morph.base, 1);
	        	}
	        }
			
		} catch (ParseException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
	}
	
	/**
     * コンストラクタ(TwitCorpus->Tweet)
     */ 
	public Tweet(TwitCorpus txt){
		date = null;
		name = id = null;
		text = txt.getText();
		morphs = Morpheme.analyzeMorpheme(text);
		
		for(Morpheme morph : morphs){
			if(morphsCount.containsKey(morph.base)){
				morphsCount.put(morph.base, morphsCount.get(morph.base) + 1);
			} else {
				morphsCount.put(morph.base, 1);
			}
		}
		
	}
	
	/**
     * このツイートの日時をDate型で返す
     */
    public Date getDate() {
        return date;
    }
	
	/**
     * このアカウント名を返す
     */
    public String getName() {
        return name;
    }

    /**
     * このアカウントのIDを返す
     */
    public String getId() {
        return id;
    }

    /**
     * このツイート本文を返す
     */
    public String getText() {
        return text;
    }

    /**
     * 添え字idxに対応する形態素を返す
     */
    public Morpheme getMorph(int idx) {
        return morphs.get(idx);
    }
    
    /**
     * 形態素リストを返す
     */
    public ArrayList<Morpheme> getMorphs() {
        return morphs;
    }
    
    /**
     * 形態素別の個数を保持したHashMapを返す
     */
    public HashMap<String, Integer> getMorphsCount() {
        return morphsCount;
    }
	
	public static void main(String[] args){
        // MeCabを起動
        Morpheme.startMeCab();
        try {
        	String fname = "D:\\Twitter\\TwitterAnalysis\\samples\\SampleTL1.txt";
    		BufferedReader br = FileRead.makeBR(fname, "UTF-8");

			List<Tweet> tweets = new ArrayList<Tweet>();
			
			String[] inputs = {"","","",""};
			int i = 0;
			
            while( ( inputs[i] = br.readLine() ) != null ){
            	int inputSize = inputs[i].length();
            	StringBuilder temp = new StringBuilder();
            	inputs[i] = temp.append(inputs[i]).delete(inputSize-6,inputSize).toString();
            	i = (i + 1) % 4;
            	if(i == 0){
            		tweets.add(new Tweet(inputs[0],inputs[1],inputs[2],inputs[3]));
            	}
            }      
         	br.close();
            
         	showTweets(tweets);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
	
	static String print_info_ac(Tweet tw){
		String info = tw.getName() + " @" + tw.getId() + " " + tw.getDate().toString();
		return info;
	}
	
	static void showTweets(List<Tweet> tweets){
		for(Tweet tw : tweets){
        	System.out.println( tw.getName() + " @" + tw.getId() );
        	System.out.println( tw.getText() );
        	for(Map.Entry<String, Integer> entry : tw.morphsCount.entrySet()) {
        	    System.out.print(entry.getKey() + ": " + entry.getValue()+ " ");
        	}
        	System.out.println();
        }
	}
}
