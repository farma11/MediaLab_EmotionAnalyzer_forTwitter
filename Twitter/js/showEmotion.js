
// 感情名の一覧
var emoNames = [
		'Joy','Trust','Fear','Surprise','Sad','Disgust','Anger','Anticipate'
	];
// 感情の列挙型
var emoEnum = {
	Joy       : 0,
	Trust     : 1,
	Fear      : 2,
	Surprise  : 3,
	Sad       : 4,
	Disgust   : 5,
	Anger     : 6,
	Anticipate: 7,
};

// ページを表示させたと同時に実行される関数
function onloadFunc(){
	vars = getURLVars();
	
	// 感情によって異なる処理を行う
	switch(Number(vars[0])){
		case emoEnum.Joy:
			JoyMOVE(Number(vars[1]));
			break;
			
		case emoEnum.Trust:
			TrustMOVE(Number(vars[1]));
			break;
			
		case emoEnum.Fear:
			FearMOVE(Number(vars[1]));
			break;
			
		case emoEnum.Surprise:
			SurpriseMOVE(Number(vars[1]));
			break;
			
		case emoEnum.Sad:
			SadMOVE(Number(vars[1]));
			break;
			
		case emoEnum.Disgust:
			DisgustMOVE(Number(vars[1]));
			break;
			
		case emoEnum.Anger:
			AngerMOVE(Number(vars[1]));
			break;
			
		case emoEnum.Anticipate:
			AnticipatMOVE(Number(vars[1]));
			break;
	}
}

/**
 * URL解析して、クエリ文字列を返す
 * @returns {Array} クエリ文字列
 */
function getURLVars(){
    var url = window.location.href;
    var hash = url.split("#");
    var vars = hash[1].split("&");
    
    return vars;
}


/**
 * 各感情に対して、その度合いscoreに応じた処理を行う
 */
function JoyMOVE(score){
	if(score == 0){
		ScoreIs0();
	} else if (score < 5.0){
		document.getElementById("t1").textContent="嬉しいな";
	} else {
		document.getElementById("t1").textContent="恍惚！";
	}
}

function TrustMOVE(score){
	if(score == 0){
		ScoreIs0();
	} else if (score < 5.0){
		document.getElementById("t1").textContent="信じてるよ";
	} else {
		document.getElementById("t1").textContent="敬愛しております";
	}
}

function FearMOVE(score){
	if(score == 0){
		ScoreIs0();
	} else if (score < 5.0){
		document.getElementById("t1").textContent="不安だな";
	} else {
		document.getElementById("t1").textContent="怖い…";
	}
}

function SurpriseMOVE(score){
	if(score == 0){
		ScoreIs0();
	} else if (score < 5.0){
		document.getElementById("t1").textContent="驚いたぁ";
	} else {
		document.getElementById("t1").textContent="ビックリ仰天";
	}
}

function SadMOVE(score){
	if(score == 0){
		ScoreIs0();
	} else if (score < 5.0){
		document.getElementById("t1").textContent="悲しいな…";
	} else {
		document.getElementById("t1").textContent="…（悲嘆）";
	}
}

function DisgustMOVE(score){
	if(score == 0){
		ScoreIs0();
	} else if (score < 5.0){
		document.getElementById("t1").textContent="嫌だな";
	} else {
		document.getElementById("t1").textContent="憎らしい";
	}
}

function AngerMOVE(score){
	if(score == 0){
		ScoreIs0();
	} else if (score < 5.0){
		document.getElementById("t1").textContent="怒った";
	} else {
		document.getElementById("t1").textContent="…（激怒）";
	}
}

function AnticipateMOVE(score){
	if(score == 0){
		ScoreIs0();
	} else if (score < 5.0){
		document.getElementById("t1").textContent="そんな気がする";
	} else {
		document.getElementById("t1").textContent="警戒しないと";
	}
}
function ScoreIs0(){
	document.getElementById("t1").textContent="…（沈黙）";
}

 
