<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%
Long timestamp = 1L; 
// disable debug mode
// timestamp = System.currentTimeMillis();
request.setAttribute("dateValue", timestamp);
%>

<!-- webjars modules -->
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/jquery/3.2.1/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/webjars/font-awesome/4.7.0/css/font-awesome.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/tools.js?"></script>
<link href="${pageContext.request.contextPath}/static/style.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/bootstrap/3.3.7/css/bootstrap.min.css">
