<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8"
		name="viewprot"
		content="width=divice-width,initial-scale=1.0">
	<link rel="stylesheet" href="css/upload.css"/>
	<title>음악 올리기</title>
	<script>
	function goSubmit() {
	    window.opener.name = "UploadProc"; // 부모창의 이름 설정
	    document.fileForm.target = "UploadProc"; // 타켓을 부모창으로 설정
	    document.fileForm.action = "/ListenIt/UploadProc.it";
	    document.fileForm.submit();
	    self.close();
	}
	</script>
</head>
<body>
	<form name="fileForm" id="fileForm" method="post" action="/ListenIt/UploadProc.it" enctype="multipart/form-data">
	    <table>
			<tr>
				<td class="title">제목</td>
				<td><input type="text" name="singer_name"/></td>
				<td class="title">가수</td>
				<td><input type="text" name="song_name"/></td>
			</tr>
			
		</table>
		<br>
		
		<input type="file" name="uploadFile" id="uploadFile"> 
	    <input type="submit" onclick="goSubmit()" value="전송">
</form>
</body>
</html>