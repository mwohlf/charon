<html lang="en">
${subject("Registration")}
${from("finalrestingheartrate@gmail.com")}
${replyTo("mwhlfrt@gmail.com")}
${to(user.email)}
${to("mwhlfrt@gmail.com")}
<head>
    <title>Registration</title>
</head>
<body>

Welcome ${username},

in order to complete the registration it would be great if you click on the following link:


${registration_url}?token=${registration_token}

Thanks and have a great day!


</body>
</html>
