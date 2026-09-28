<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <title>Thanks</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/styles/main.css"
          type="text/css"/>

</head>

<body>

    <h1>
        Thanks for joining our email list!
    </h1>

    <p>

        Thank you
        ${user.firstName}
        ${user.lastName}.

    </p>

    <p>

        Your email address
        <b>${user.email}</b>
        has been added to our list.

    </p>

    <p>
        <i>${errorMessage}</i>
    </p>

</body>

</html>