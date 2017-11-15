<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
	<title>TDP Datalex Rules</title>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<meta name="author" content="D4">
	<meta name="keywords" content="TDP, BRE, RULES VIEWER">
	<meta name="description" content="try to analize this hell">
	<meta name="copyright" content="D4">
    <link rel="icon" href="${pageContext.request.contextPath}/favicon.ico">
	<script>var ctx = "${pageContext.request.contextPath}"</script>
	
	<jsp:include page="static/import_css.jsp"></jsp:include>
<%-- <jsp:include page="static/import.jsp"></jsp:include> --%>
	
</head>
<body>
	<jsp:include page="static/tmplt_nav.jsp"></jsp:include>

  
	 
	 
    <div class="container">
<!--       <div class="jumbotron"> -->
<!--         <h1>Navbar example</h1> -->
<!--         <p class="lead">This example is a quick exercise to illustrate how fixed to top navbar works. As you scroll, it will remain fixed to the top of your browser's viewport.</p> -->
<!--         <a class="btn btn-lg btn-primary" href="https://v4-alpha.getbootstrap.com/components/navbar/" role="button">View navbar docs »</a> -->
<!--       </div> -->
		<div id="beanPolicyData">
			<ol id="tdpdata" class="tree"></ol>
		</div>
	</div>
	 


<%-- <jsp:include page="static/legend_test.jsp"></jsp:include> --%>
<jsp:include page="static/import_js.jsp"></jsp:include>

</body>
</html>
