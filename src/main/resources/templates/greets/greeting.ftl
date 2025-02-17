<!DOCTYPE html>
<html>
<head>
    <title>Greeting</title>
    <style>
        .greeting {
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

        .timestamp {
            color: #999;
            font-size: 0.9em;
        }
    </style>
</head>
<body>
<div class="greeting">
    <div class="message">${message}</div>
    <div class="sender">From: ${sender}</div>
    <div class="timestamp">Time: ${timestamp!''}</div>
</div>
</body>
</html>
