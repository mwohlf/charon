<html lang="en">
${subject("Registration")}
${from("finalrestingheartrate@gmail.com")}
${replyTo("mwhlfrt@gmail.com")}
${to(email)}
<head>
    <title>Registration</title>
</head>
<body>
<br/>
Welcome ${username},<br/>
<br/>
in order to complete the registration it would be great if you click on the following link:<br/>
<br/>
${tokenUrl}?${tokenKey}=${tokenValue}<br/>
<br/>
Thanks and have a great day!
<br/>
<br/>
</body>
</html>
