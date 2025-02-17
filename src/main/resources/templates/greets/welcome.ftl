<!DOCTYPE html>
<html>
<head>
    <title>Welcome</title>
    <style>
        .welcome {
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }

        .message {
            font-size: 24px;
            color: #333;
            margin-bottom: 10px;
        }

        .sender {
            color: #666;
            font-style: italic;
        }
    </style>
</head>
<body>
<div class="welcome">
    <div class="message">${message}</div>
    <div class="sender">From: ${sender}</div>
</div>
</body>
</html>
