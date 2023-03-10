/**
 * 
 */
//폼태그의 모든 입력 데이터의 이름과 값을 콘솔에 로그로 출력할 수 있는 함수.
//ex) $("form").log().serializeObject();
$.fn.serializeObject=function(){
	if(this.prop('tagName')!='FORM')
		throw Error("form 태그 외에는 사용 불가.");
	let fd = new FormData(this[0]);
	let nameSet = new Set();
	for(let key of fd.keys()){
		nameSet.add(key);
	}
	let data = {};
	for(let name of nameSet){
		let values = fd.getAll(name)
		if(values.length>1){
			data[name] = values;  
		}else{
			data[name] = values[0];
		}
	}
	return data;
}
$.fn.log=function(){
	let data = this.serializeObject();
	for(let prop in data){
		console.log(prop + " : " + data[prop]);
	}
	return this;
}

$.fn.sessionTimer=function(timeout, msgObj){
	if(!timeout)
		throw Error("세션 타임아웃 값이 없음.");
	
	const SPEED = 100;
	// $(document).on("click",".controlBtn",function(){});
	const TIMEOUT = timeout;
	const timerArea = this;
	
//	event propagetion : bubbling 방식
	let msgArea = null;
	if(msgObj){
		msgArea = $(msgObj.msgAreaSelector).on("click", msgObj.btnSelector, function(event){
			//	 		console.log(this.id + ", " + $(this).prop("id"));
			if(this.id=="YES"){
				//타이머 다시 되돌리기	, 1분남았을때 다시 창띄우기
				jobClear();
				timerInit();
				
				//서버에서 세션을 연장하기 위해씀, 가져오는게 아님, 라인하고 헤더만 받아오면됨
				//url빼면 현재 브라우저에 다시요청
				$.ajax({
					//	 				url : 
					method : "head"
						//	 				data : {},
						//	 				dataType : "",
						//	 				success : function(resp) {
						
						//	 				},
						//	 				error : function(jqXHR, status, error) {
						//	 					console.log(jqXHR);
						//	 					console.log(status);
						//	 					console.log(error);
						//	 				}
				});
			}
			msgArea.hide();
		}).hide();
	}
	
	let jobClear = function(){
		let timerJob = timerArea.data("timerJob");
		if(timerJob)
			clearInterval(timerJob);
		let msgJob = msgArea.data('msgJob');
		if(msgJob)
			clearTimeout(msgJob);
	}
	
	//1분남았을때 창띄우기
	let timerInit = function(){
		if(msgObj){
			let msgJob = setTimeout(() => {
				msgArea.show();	
			}, (TIMEOUT-60) * SPEED);
			msgArea.data('msgJob', msgJob);
		}
		
		let timer = TIMEOUT;
		//시간 discount
		let timerJob = setInterval(()=>{
			if(timer==1){
				clearInterval(timerJob);
				location.reload(); //새로고침
			}else{
				timerArea.html(timeFormat(--timer));
			}
		},SPEED);
		timerArea.data("timerJob", timerJob);
	}
	
	timerInit();
	
	let timeFormat = function(time){
		let min = Math.trunc( time / 60 );
		let sec = time % 60;
		return min + ":" + sec;
	}
	return this;
}