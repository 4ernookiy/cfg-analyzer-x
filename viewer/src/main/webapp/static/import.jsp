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

<link href="${pageContext.request.contextPath}/static/xc_without_bs.css?${dateValue}" rel="stylesheet">
<link href="${pageContext.request.contextPath}/static/template.css?${dateValue}" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/tools.js?"></script>
<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/static/menu.js?${dateValue}"></script> --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/win.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/win.js"></script>