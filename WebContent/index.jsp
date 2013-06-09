<%@page import="org.apache.lucene.util.Version"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
#outer {
	overflow: hidden;
	position: relative;
	width: 100%;
}

#outer {
	display: table;
	position: static;
}

#middle {
	display: table-cell;
	vertical-align: middle;
	position: static;
}

#inner {
	width: 400px;
	margin-left: auto;
	margin-right: auto;
	text-align: center;
}
</style>
<!-- jQuery dla suggestera -->
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script>
	$(function() {
		$("#tags").autocomplete({
			source : "http://localhost:8080/tjug-wiki-search/suggests.jsp"
		});
	});
</script>
<!-- end: jQuery dla suggestera  -->
<title>Trójmiasto JUG - wyszukiwarka Wikipedii</title>

</head>
<body>
	<div id="outer">
		<div id="middle">
			<div id="inner">
				<img alt="TJUG" src='/tjug-wiki-search/img/logo_tjug.png'>
				<form method="get" action="search.jsp">
					<input name="query" size="40" id="tags" />&nbsp;<input
						type="submit" value="search">
				</form>
				<h3>Szukamy z Apache Lucene v. <%=Version.LUCENE_43 %></h3>
			</div>
		</div>
	</div>
</body>
</html>