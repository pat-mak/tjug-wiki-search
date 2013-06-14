<%@page import="pl.jug.trojmiasto.lucene.search.SearchResult"%>
<%@page import="pl.jug.trojmiasto.lucene.model.Article"%>
<%@page import="pl.jug.trojmiasto.lucene.facade.SearchFacade"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:useBean id="searchFacade"
	class="pl.jug.trojmiasto.lucene.facade.SearchFacade" scope="application" />
<%
	String prefix = request.getParameter("term");

	SearchResult result = searchFacade.suggestions(prefix);
	
	String buffer = "[";
	for (Article article : result.getArticles()) {
		buffer = buffer + "\"" + article.getTitle() + "\",";
	}
	buffer = buffer.substring(0, buffer.length() - 1) + "]";
	
	response.getWriter().println(buffer);
%>