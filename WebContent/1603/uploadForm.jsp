<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8"
		name="viewprot"
		content="width=divice-width,initial-scale=1.0">
<title>Insert title here</title>
</head>
<body>
	<form name="fileForm" id="fileForm" method="post" action="fileUpload.jsp" enctype="multipart/form-data">
	    <input type="text" name="title" id="title" disabled>
	    <input type="file" name="uploadFile" id="uploadFile"> 
	    <input type="submit" value="전송">
</form>
</body>
</html>