<html>
${subject("Kündigung des Vertrags [" + cancellation.contractNumber + "]")}
${from("finalrestingheartrate@gmail.com")}
${replyTo("mwhlfrt@gmail.com")}
${to(user.email)}
${to("mwhlfrt@gmail.com")}
<head>

</head>
<body>

${dictionary['emailSalutation']?xhtml},

testmail with template

Ihre Kündigung vom ${cancellationDate} für den Vertrag ${contract.contractNumber} haben wir erhalten. Die Auftragsnummer lautet ${contractCancelledDto.orderNumber}.


</body>
</html>
