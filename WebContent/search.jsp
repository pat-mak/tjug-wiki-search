<%@page import="pl.jug.trojmiasto.lucene.search.SearchResult"%>
<%@page import="pl.jug.trojmiasto.lucene.model.Category"%>
<%@page import="pl.jug.trojmiasto.lucene.model.Article"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:useBean id="searchFacade"
	class="pl.jug.trojmiasto.lucene.facade.SearchFacade" scope="application" /><!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
#outer {
	display: table;
	position: static;
}

#middle {
	display: table-cell;
	vertical-align: middle;
	position: static;
}

#results {
	float: left;
	width: 70%;
	height: 100%;
	border: dashed rgb(146, 142, 142) thin;
	margin-right: 5px;
	padding: 5px;
}

#categories {
	width: 300px;
	float: left;
	height: 100%;
	border: dashed rgb(146, 142, 142) thin;
	margin-right: 5px;
	padding: 5px;
}

.separator {
	color: black;
	font-weight: bold;
}

.result {
	margin: 10px
}

.title {
	font-weight: bold;
}

.date {
	float: right;
}

.article-category {
	color: gray;
}

.description {
	font-style: italic;
	color: gray;
}

.category {
	font-family: monospace;
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
<%
	String query = request.getParameter("query");
%>
<body>
	<div id="outer">
		<img alt="TJUG" src="/tjug-wiki-search/img/logo_tjug.png"
			style="float: left">
		<div id="middle">
			<div style="vertical-align: middle; display: table-cell;">
				<form method="get" action="search.jsp">
					<input name="query" size="40" id="tags" value="<%=query%>" />&nbsp;<input
						type="submit" value="search">
				</form>
			</div>
		</div>
	</div>
	<% SearchResult result = searchFacade.search(query); %>
	<!-- Wyniki kategoryzacji dla parametru query-->
	<div id="categories">
		<h2>Kategorie</h2>
		<%
			for (Category category : result.getCategories()) {
		%>
		<div class="category"><%=category.getPrintableName()%>(<%=category.getCount() %>)</div>
		<%
			}
		%>
	</div>
	<!-- Wyniki wyszukiwania frazy z parametru query -->
	<div id="results">
		<%if (!result.isSearchSucceded()) { %>
		<h2>Wyszukiwanie nie powiodło się bo: <%=result.getFailMessage() %></h2>
		<%} else { %>
		<h2>Wyników: <%= result.getCount() %>(<%= result.getSearchTime() %>ms)</h2>
		<%
			for (Article article : result.getArticles()) {
		%>
		<div class="result">
			<div class="title">
				<a href="http://pl.wikipedia.org/wiki/<%=article.getTitle()%>"><%=article.getTitle()%></a>
				<div class="date"><%=article.getTimeString()%></div>
			</div>
			<div class="article-category">Kategoria: <%=article.getCategory()%></div>
			<div class="description"><%=article.getContent()%></div>
		</div>

		<%
			}
		}
		%>
	</div>
</body>
</html>
