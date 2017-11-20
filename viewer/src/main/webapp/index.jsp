<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<!DOCTYPE html>
<html lang="en">

<head>
<title>TDP Datalex Rules</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="author" content="D4">
<meta name="keywords" content="TDP, BRE, RULES VIEWER">
<meta name="description" content="try to analize this hell">
<meta name="copyright" content="D4">
<link rel="icon" href="${pageContext.request.contextPath}/favicon.ico">
<script>
	var ctx = "${pageContext.request.contextPath}"	
</script>
<jsp:include page="static/import.jsp"></jsp:include>
</head>
<body>
	<div class="container">
	<div id="win" ></div>
		<div id="detailData"></div>
		<div id="beanPolicyData">
			<ol id="tdpdata" class="tree"></ol>
		</div>
	</div>
</body>
</html>