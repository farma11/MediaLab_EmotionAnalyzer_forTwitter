import java.io.*;
import java.util.*;

public class TweetAnalze {
	
	// 各形態素のDF値を格納するHashMap
    static HashMap<String,Integer> dfMap = new HashMap<String,Integer>();
    // DFを数えた文書集合の全文書数
    static int N;
	
	// 入力された形態素コーパス
	static TreeMap<String,Corpus> corpora = new TreeMap<String,Corpus>();
	// 形態素コーパス内に存在しない単語 val:{回数,TF-IDF}
	static TreeMap<String,Pair<Integer,Double> > undefs = new TreeMap<String,Pair<Integer,Double> >();
	// ツイートDB
	static List<TwitCorpus> tweetDB = new ArrayList<TwitCorpus>();
	
    public static void main( String[] args ) {
    	 try {
    		 
    		 setupAnalyisis();
    		 
    		 String Id = inputId();
    		// ツイートリストを取得後、全体の感情値を計算
            List<Tweet> tweets = getTweets(Id, 1);
            Emotion scoresAVE = analyzeTweets(tweets);

            Emotion.toJSonWEB(scoresAVE);
            System.out.println();
            System.out.println("ALL TWEETS EMOTIONS");
            Emotion.printEmotions(scoresAVE);
            
            outputUndefMorphs();
        }
        catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 解析の準備
     */
    static void setupAnalyisis(){
    	try {
    		// 各単語のDFをdfMapに読み込んで初期化
   		 	initDf();
   		 
   		 	inputTwitCorpus();
   		 	inputCorpus();
   		 	
   		 	// MeCabを起動
   		 	Morpheme.startMeCab();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * 単一ツイートの解析
     * @param tw
     * @return
     */
    static Emotion analyzeTweet(Tweet tw){
		 
    	Emotion scores = new Emotion();
    	// 各形態素のTF-IDF値を計算
        ArrayList<Double> morphsTFIDF = calTFIDF(tw.getMorphs());
        calScore(tw.getMorphs(), morphsTFIDF, scores);
        modifyEmotion(tw.getMorphs(), scores);
    	
    	return scores;
    }
    
    /**
     * 単一ツイートの解析(Corpus変更可)
     * @param tw
     * @return
     */
    static Emotion analyzeTweet(Tweet tw, TreeMap<String,Corpus> co){
		 
    	Emotion scores = new Emotion();
    	// 各形態素のTF-IDF値を計算
        ArrayList<Double> morphsTFIDF = calTFIDF(tw.getMorphs());
        calScore(tw.getMorphs(), morphsTFIDF, scores, co);
        modifyEmotion(tw.getMorphs(), scores);
    	
    	return scores;
    }
    
    /**
     * 複数ツイートの解析
     */
    static Emotion analyzeTweets(List<Tweet> tweets){
    	Emotion scoresAVE = new Emotion();
    	for(Tweet tw : tweets){
        	// 各感情値の演算
        	Emotion scores = analyzeTweet(tw);
            for(int j = 0; j < 8; j++){
            	scoresAVE.setEmotion(j, scoresAVE.getEmotion(j) + scores.getEmotion(j));
            }
        }
        for(int j = 0; j < 8; j++){
        	scoresAVE.setEmotion(j, scoresAVE.getEmotion(j)/tweets.size());
        }
    	return scoresAVE;
    }
    
    /**
     * 複数ツイートの解析(Corpus変更可)
     */
    static Emotion analyzeTweets(List<Tweet> tweets, TreeMap<String,Corpus> co){
    	Emotion scoresAVE = new Emotion();
    	for(Tweet tw : tweets){
        	// 各感情値の演算
        	Emotion scores = analyzeTweet(tw, co);
            for(int j = 0; j < 8; j++){
            	scoresAVE.setEmotion(j, scoresAVE.getEmotion(j) + scores.getEmotion(j));
            }
        }
        for(int j = 0; j < 8; j++){
        	scoresAVE.setEmotion(j, scoresAVE.getEmotion(j)/tweets.size());
        }
    	return scoresAVE;
    }
    
    /**
     * 解析アカウントのID入力
     */
    static String inputId(){
    	Scanner sc = new Scanner(System.in);
    	System.out.print("ID: ");
    	return sc.next();
    }
    
    /**
     * IDからツイート一覧を取得
     * kind = 1:Latest, kind = 2:100 items
     */
    static List<Tweet> getTweets(String id, int kind){
    	List<Tweet> tweets = new ArrayList<Tweet>();
    	
    	try{
	    	// ファイル入力の準備
    		String url = "";
    		int warning = 0;
    		switch(kind){
    		case 1:
    			url = "http://shippoproj1.webcrow.jp/twit2-bot.php?id=" + id ;
    			warning = 5;
    			break;
    		case 2:
    			url = "http://shippoproj1.webcrow.jp/twit2.php?id=" + id ;
    			warning = 3;
    			break;
    		}
	    	
			BufferedReader br = URLRead.makeBR( url,"UTF-8");
	        
			// webページ冒頭のwarming文を飛ばす
	        for(int i = 0; i < warning; i++){
	        	String firstline = br.readLine(); //とばす
	        }
	    	
	    	String[] inputs = {"","","",""};
			int i = 0;
			int endi = 0;
			
	        while( ( inputs[i] = br.readLine() ) != null ){
	        	
	        	int inputSize = inputs[i].length();
	        	StringBuilder temp = new StringBuilder();
	        	inputs[i] = temp.append(inputs[i]).delete(inputSize-6,inputSize).toString();
	        	i = (i + 1) % 4;
	        	if(i == 0){
	        		//@以下のIDを削除
	        		inputs[3] = inputs[3].replaceAll("@[^ ]+ ","");
	        		
	        		tweets.add(new Tweet(inputs[0],inputs[1],inputs[2],inputs[3]));
	        		endi += 1;
	        		if(endi == 100) break;
	        	}
	        } 
    	}
        catch( IOException e ){
            System.err.println(e);
        }
    	return tweets;
    }
    
    /**
     * ツイートDBの入力
     */
    static void inputTwitCorpus(){
    	try {
	        	
		// ファイル入力の準備
    	//String url = "http://fama11.sitemix.jp/MediaLab3/tweet-corpus1.csv";
    	String url = "http://shippoproj1.webcrow.jp/Corpus/tweet-corpus1.csv";
		BufferedReader br = URLRead.makeBR( url,"SJIS");
	
		String line;
	    while( ( line = br.readLine() ) != null ){
	    	tweetDB.add(new TwitCorpus(line));
	    }
	 	br.close();
 	
    	} catch (IOException e) {
    		System.out.println("コーパスの入力に失敗しました。");
			e.printStackTrace();
		}
    }
    
    /**
     * ツイートDBの入力(twDB変更可)
     */
    static void inputTwitCorpus(List<TwitCorpus> twDB){
    	try {
	        	
		// ファイル入力の準備
    	String url = "http://fama11.sitemix.jp/MediaLab3/tweet-corpus1.csv";
		BufferedReader br = URLRead.makeBR( url,"SJIS");
	
		String line;
	    while( ( line = br.readLine() ) != null ){
	    	twDB.add(new TwitCorpus(line));
	    }
	 	br.close();
 	
    	} catch (IOException e) {
    		System.out.println("コーパスの入力に失敗しました。");
			e.printStackTrace();
		}
    }
    
    
    /**
     *  コーパスの入力
     */
    static void inputCorpus(){
    	try {       
    		
            //String url = "http://fama11.sitemix.jp/MediaLab3/corpus1.csv";
    		String url = "http://shippoproj1.webcrow.jp/Corpus/corpus1.csv";
    		BufferedReader br = URLRead.makeBR( url,"SJIS");

			String line;
            while( ( line = br.readLine() ) != null ){
            	Corpus co = new Corpus(line);
            	corpora.put(co.getText(),co);
            }
         	br.close();
    	} catch (IOException e) {
    		System.out.println("コーパスの入力に失敗しました。");
			e.printStackTrace();
		}
    }
    
    /**
     *  コーパスの入力(Corpus変更可)
     */
    static void inputCorpus(TreeMap<String,Corpus> co){
    	try {       
            String url = "http://fama11.sitemix.jp/MediaLab3/corpus1.csv";
    		BufferedReader br = URLRead.makeBR( url,"SJIS");

			String line;
            while( ( line = br.readLine() ) != null ){
            	Corpus c = new Corpus(line);
            	co.put(c.getText(),c);
            }
         	br.close();
    	} catch (IOException e) {
    		System.out.println("コーパスの入力に失敗しました。");
			e.printStackTrace();
		}
    }
    
    /**
     * 形態素コーパスの重みづけ
     */
     static void calScore(ArrayList<Morpheme> morphs, ArrayList<Double> morphsTFIDF, Emotion scores){
    	 // 各形態素について
    	 for(int i = 0; i < morphs.size(); i++){
    		 Morpheme morph = morphs.get(i);
    		 if(morph.base.equals("*")) continue;
    		 
    		 // コーパス内に存在していたら
         	if(corpora.containsKey(morph.base)){
         		Corpus coefficiency = corpora.get(morph.base);
         		for(int j = 0; j < 8; j++){
         			scores.setEmotion(j, calWeight(scores.getEmotion(j), morphsTFIDF.get(i) * coefficiency.getEmotion(j)));
         		}
         	} //コーパス内に存在していなかったら、未定義リストととして出力
         	else {
         		// System.out.println("undefined: " + morph.base);
         		if(undefs.containsKey(morph.base)){
         			undefs.put(morph.base, new Pair(undefs.get(morph.base).first+1,undefs.get(morph.base).second));
         		} else {
         			undefs.put(morph.base, new Pair( 1,morphsTFIDF.get(i)));
         		}
         	}
    	 }
    	 
     }
     
     /**
      * 形態素コーパスの重みづけ(corpus変更可)
      */
      static void calScore(ArrayList<Morpheme> morphs, ArrayList<Double> morphsTFIDF, Emotion scores, TreeMap<String,Corpus> co){
     	 // 各形態素について
     	 for(int i = 0; i < morphs.size(); i++){
     		 Morpheme morph = morphs.get(i);
     		 if(morph.base.equals("*")) continue;
     		 
     		 // コーパス内に存在していたら
          	if(co.containsKey(morph.base)){
          		Corpus coefficiency = co.get(morph.base);
          		for(int j = 0; j < 8; j++){
          			scores.setEmotion(j, calWeight(scores.getEmotion(j), morphsTFIDF.get(i) * coefficiency.getEmotion(j)));
          		}
          	} //コーパス内に存在していなかったら、未定義リストととして出力
          	else {
          		// System.out.println("undefined: " + morph.base);
          		if(undefs.containsKey(morph.base)){
          			undefs.put(morph.base, new Pair(undefs.get(morph.base).first+1,undefs.get(morph.base).second));
          		} else {
          			undefs.put(morph.base, new Pair( 1,morphsTFIDF.get(i)));
          		}
          	}
     	 }
     	 
      }
     
     /**
      * 重みづけ関数
      */
     static double calWeight(Double score, double val){
    	 score += val;
    	 return score;
     }
     
     /**
      * 各単語のDFを記録したdf.tsv をdfMap に読み込む
      */
     static void initDf() throws Exception {
         BufferedReader in = new BufferedReader(new FileReader("df.tsv"));
         for(String line = in.readLine(); line != null; line = in.readLine()) {
             String[] array = line.split("\t");
             if (array[0].equals("TOTAL_DOC_COUNT_N")) {
                 N = Integer.parseInt(array[1]); // 文書集合の全文書数
             } else if (array.length == 2) {
                 // 単語array[0]の現れた文書数array[1]をdfMapに格納
                 dfMap.put(array[0], Integer.parseInt(array[1]));
             }
         }
     }
     
     /**
      * 形態素tのIDFを返す
      */
     static double getIdf(Morpheme t) {
         return 1 + Math.log((double)N / getDf(t));
     }
     
     /**
      * 形態素tのDFを返す
      */
     static int getDf(Morpheme t) {
         String base = t.getBaseform(); // 形態素の原形を取得
         if (dfMap.get(base) != null) {
             return dfMap.get(base);    // dfMapに格納しておいたDF値を返す
         } else {
             return 1;                  // DF値が格納されていなければ1を返す
         }
     }

     /**
      * 形態素リストmorphsの中で数えた形態素tのTF（出現回数）を返す
      */
     static int getTf(Morpheme t, List<Morpheme> morphs) {
         int tf = 0;
         String tBase = t.getBaseform();  // tの原形
         for(int i = 0; i < morphs.size(); i++) {  // 形態素を1つずつ取り出すforループ
             Morpheme morph = morphs.get(i);          // i番目の形態素morph
             String morphBase = morph.getBaseform();  // morphの原形
             if (morphBase.equals(tBase)) {    // もし同じ原形なら
                 tf++;
             }
         }
         return tf;
     }

     /**
      * 形態素リストmorphsの中で数えた形態素tのTF-IDFを返す
      */
     static double getTfIdf(Morpheme t, List<Morpheme> morphs) {
         return getTf(t, morphs) * getIdf(t);
     }
     
     /**
      * TF-IDFを使ったスコア計算
      */
     static ArrayList<Double> calTFIDF(List<Morpheme> morphs){
    	 ArrayList<Double> vals = new ArrayList<Double>();
    	 for (int i = 0; i < morphs.size(); i++) {
             Morpheme morph = morphs.get(i);
             double score = getTfIdf(morph, morphs);  // TF-IDFを使ったスコア計算
             vals.add(score);
             // System.out.println(morph.getBaseform()+": "+score);  // スコア表示
         }
    	 return vals;
     }
        
     /**
      * 未定義形態素リストのファイル出力
      */
     static void outputUndefMorphs(){
    	 try{
         	String fname = "D:\\Twitter\\TwitterAnalysis\\samples\\undefMorphs.csv";
    		BufferedReader br = FileRead.makeBR(fname, "SJIS");
    		 
         	String in_line = "";
         	while( ( in_line = br.readLine() ) != null ){
         		String[] inputs = in_line.split(",",-1);
         		
         		if(undefs.containsKey(inputs[0])){
         			undefs.put(inputs[0], new Pair(undefs.get(inputs[0]).first+1,undefs.get(inputs[0]).second));
         		} else {
         			undefs.put(inputs[0], new Pair( 1,Double.parseDouble(inputs[2])));
         		}
         	}
    		 
         	// 未定義リストのファイル出力
    	      File file = new File("D:\\Twitter\\TwitterAnalysis\\samples\\undefMorphs.csv");

    	      if (checkBeforeWritefile(file)){
    	        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

    	        for(String str : undefs.keySet()){
                	pw.println(str + ", " + undefs.get(str).first + ", " + undefs.get(str).second);
                }
    	        pw.close();
    	      }else{
    	        System.out.println("ファイルに書き込めません");
    	      }
    	    }catch(IOException e){
    	      System.out.println(e);
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
      * ツイートDB上のツイートと類似度を計算し、解析後の感情値を変動させる
      */
     static void modifyEmotion(ArrayList<Morpheme> morphs, Emotion scores){
    	 HashMap<String, Integer> morphsCount = new HashMap<String, Integer>();
    	 for(Morpheme morph: morphs){
    		 if(morphsCount.containsKey(morph.base)){
         		morphsCount.put(morph.base, morphsCount.get(morph.base) + 1);
         	} else {
         		morphsCount.put(morph.base, 1);
         	}
    	 }
    	 
    	 for(TwitCorpus twDB : tweetDB){
    		 double simi = calSimilarity(morphsCount, twDB);
    		 if(simi > 0.5){
    			 for(int i = 0; i < 8; i++){
    				 scores.setEmotion(i, scores.getEmotion(i) * 0.6 + twDB.getEmotion(i) * 0.4); 
    			 }
    			 //System.out.println("類似: " + simi + " " + twDB.getText());
    		 }
    	 }
     }
     
     /**
      * 2つのツイートの類似度を求める
      */
     static double calSimilarity(HashMap<String, Integer> tw1, TwitCorpus tw2_db){
    	 double ans = 0.0;
    	 int tw1_morph_num = 0;
    	 for(Map.Entry<String, Integer> entry : tw1.entrySet()) {
    		 tw1_morph_num += entry.getValue();
    		 if(tw2_db.morphsCount.containsKey(entry.getKey())){
    			ans += entry.getValue() * tw2_db.morphsCount.get(entry.getKey());
         	}
     	}
    	 return ans / (tw1_morph_num + tw2_db.morph_num);
     }
     
     static double calSimilarity(HashMap<String, Integer> tw1, HashMap<String, Integer> tw2){
    	 double ans = 0.0;
    	 for(Map.Entry<String, Integer> entry : tw1.entrySet()) {
    		 if(tw2.containsKey(entry.getKey())){
    			ans += entry.getValue() * tw2.get(entry.getKey());
         	}
     	}
    	 return ans / (tw1.size() * tw2.size());
     }
     
     
     
     
}