<%--
  Created by IntelliJ IDEA.
  User: Gabriel
  Date: 17/09/2015
  Time: 01:25 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Import ADM Locations</title>
</head>

<body>
<h2>Import ADM1/ADM2/ADM3</h2>
<g:uploadForm name="myUpload" action="importFiles">
    <input type="file" id="files" name="files[]" multiple />
    <g:submitButton name="submit"/>
</g:uploadForm>
<hr>
</body>
</html>