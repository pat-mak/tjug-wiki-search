<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:useBean id="modifierFacade"
	class="pl.jug.trojmiasto.lucene.facade.ModifierFacade"
	scope="application" /><!doctype html>
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
<title>Trójmiasto JUG - wyszukiwarka Wikipedii</title>

</head>
<%
	boolean save = null != request.getParameter("save");
	String saveMsg = "";
	if (save) {
		String title = request.getParameter("title");
		String category = request.getParameter("category");
		String content = request.getParameter("content");
		if (modifierFacade.add(title, category, content)) {
			saveMsg = "Artykuł <b>" + title
					+ "</b> został pomyślnie zapisany.";
		} else {
			saveMsg = "Zapisanie artykułu <b>" + title
					+ "</b> nie powiodło się.";
		}
	}
%>
<body>
	<div id="outer">
		<div id="middle">
			<div id="inner">
				<img alt="TJUG" src='/tjug-wiki-search/img/logo_tjug.png'>
				<form method="get" action="add.jsp">
					<%
						if (save) {
					%>
					<div id="savedMsg"><%=saveMsg%></div>
					<%
						}
					%>
					<div>
						Tytuł<br /> <input name="title" size="40" id="tags" />
					</div>
					<div>
						Kategoria<br /> <input name="category" size="40" id="tags" />
					</div>
					<div>
						Treść<br />
						<textarea rows="4" cols="50" name="content" id="tags"></textarea>
					</div>
					<br /> <input type="submit" value="Zapisz" name="save" />
				</form>
			</div>
		</div>
	</div>
</body>
</html>