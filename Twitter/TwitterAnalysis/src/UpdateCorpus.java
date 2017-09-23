import java.util.*;

public class UpdateCorpus {

	// ツイートDB
	static List<TwitCorpus> tweetDB = new ArrayList<TwitCorpus>();
	// ツイートDB上のツイート集合
	static List<Tweet> twList = new ArrayList<Tweet>();
	
	// 更新される形態素コーパス
	static TreeMap<String,Corpus> co_old = new TreeMap<String,Corpus>();
	// 更新候補となる形態素コーパス
	static TreeMap[] co_nexts = new TreeMap[100];
	// 各候補の評価値
	static double[] co_scores;
	
	
	public static void main(String[] args) {
		setup();
		twList = makeTweets();
		//Tweet.showTweets(twList);
		
		HashSet<Morpheme> morphs = new HashSet();
		
		// 解析結果と理想感情値との差を求める
		Emotion dist = new Emotion();
		for(int i = 0; i < twList.size(); i++){
			Emotion score = TweetAnalze.analyzeTweet(twList.get(i));
			Emotion ideal = tweetDB.get(i).getEmotions();
			dist = Emotion.add(Emotion.distance(ideal, score), dist);
			for(Morpheme m : twList.get(i).getMorphs()){
				if(!morphs.contains(m)){
					morphs.add(m);
				}
			}
		}
		Emotion.printEmotions(dist);
		Emotion.writeEmotions(dist);
		
		System.out.println("-UpDate0------------------------------------------------");
		co_nexts[0] = new TreeMap<String,Corpus>();
		co_nexts[0] = modifyCorpus(co_old, dist, morphs);
		
		for(int i = 0; i < twList.size(); i++){
			Emotion score = TweetAnalze.analyzeTweet(twList.get(i),co_nexts[0]);
			Emotion ideal = tweetDB.get(i).getEmotions();
			dist = Emotion.add(Emotion.distance(ideal, score), dist);
		}
		Emotion.printEmotions(dist);
		Emotion.writeEmotions(dist);
		
		for(int j = 1; j < 100; j++){
			System.out.println("-UpDate" + j + "------------------------------------------------");
			
			co_nexts[j&1] = new TreeMap<String,Corpus>();
			co_nexts[j&1] = modifyCorpus(co_nexts[(j-1)&1], dist, morphs);
			
			
			for(int i = 0; i < twList.size(); i++){
				Emotion score = TweetAnalze.analyzeTweet(twList.get(i),co_nexts[j&1]);
				Emotion ideal = tweetDB.get(i).getEmotions();
				dist = Emotion.add(Emotion.distance(ideal, score), dist);
			}
			Emotion.printEmotions(dist);
			Emotion.writeEmotions(dist);
			
		}
		
	}
	
	/**
	 * コーパスの更新のための準備をする
	 */
	static void setup(){
		TweetAnalze.setupAnalyisis();
		TweetAnalze.inputTwitCorpus(tweetDB);
		TweetAnalze.inputCorpus(co_old);
	}
	
	/**
	 * ツイートコーパス上のツイートをTweetクラスに
	 * @return
	 */
	static List<Tweet> makeTweets(){
		List<Tweet> tweets = new ArrayList<Tweet>();

		for(TwitCorpus tc : tweetDB){
			tweets.add(new Tweet(tc));
		}
		return tweets;
	}
	
	/**
	 * 理想感情値との差異distよりコーパスを修正する
	 * @param old
	 * @param dist
	 * @return
	 */
	static TreeMap<String,Corpus> modifyCorpus(TreeMap<String,Corpus> old, Emotion dist, HashSet<Morpheme> morphs){
		TreeMap<String,Corpus> newcorpus = old;
		double rate = 0.15;
		
		for(Map.Entry<String, Corpus> entry : newcorpus.entrySet()) {
			Corpus temp = entry.getValue();
			if(morphs.contains(temp.getText())) continue;
			
			for(int i = 0; i < 8; i++){
				temp.changeEmotion(i,(temp.getEmotion(i) + rate * dist.getEmotion(i)) / 2.0);
			}
			entry.setValue(temp);
    	}
		
//		//Randomクラスのインスタンス化
//        Random rnd = new Random();
//
//        TreeMap<String,Corpus> temps = new TreeMap<String, Corpus>();
//        for(int k = 0; k < 10; k++){
//	        int index = rnd.nextInt(8);
//	        double ran = Math.random()*4 - 2.0;
//	        
//	        
//        }
		return newcorpus;
	}
	

	
}
