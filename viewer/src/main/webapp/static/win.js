	var d = document;
	var offsetfromcursorY=15
	var ie=d.all && !window.opera;
	var ns6=d.getElementById && !d.all;
	var tipobj,op;

	function tooltip(el,txt) {
		tipobj=d.getElementById('win');
		tipobj.innerHTML = txt;
		tipobj.style.visibility="visible";
		tipobj.style.backgroundColor = "rgba(105, 105, 105, 1.3)";
		el.onmousemove=positiontip;
		
	}

	function hide_info(el) {
		d.getElementById('win').style.visibility='hidden';
		el.onmousemove='';
	}

	function ietruebody(){
	return (d.compatMode && d.compatMode!="BackCompat")? d.documentElement : d.body
	}

	function positiontip(e) {
		var curX=(ns6)?e.pageX : event.clientX+ietruebody().scrollLeft;
		var curY=(ns6)?e.pageY : event.clientY+ietruebody().scrollTop;
		var winwidth=ie? ietruebody().clientWidth : window.innerWidth-20
		var winheight=ie? ietruebody().clientHeight : window.innerHeight-20

		var rightedge=ie? winwidth-event.clientX : winwidth-e.clientX;
		var bottomedge=ie? winheight-event.clientY-offsetfromcursorY : winheight-e.clientY-offsetfromcursorY;

		if (rightedge < tipobj.offsetWidth)	tipobj.style.left=curX-tipobj.offsetWidth+"px";
		else tipobj.style.left=curX+"px";

		if (bottomedge < tipobj.offsetHeight) tipobj.style.top=curY-tipobj.offsetHeight-offsetfromcursorY+"px"
		else tipobj.style.top=curY+offsetfromcursorY+"px";
	}
	