<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <style>
        .dashboard {
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }
        .user-info {
            margin-bottom: 20px;
        }
        .stats {
            display: flex;
            gap: 20px;
        }
        .stat-card {
            background: white;
            padding: 15px;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            flex: 1;
        }
        .stat-value {
            font-size: 24px;
            font-weight: bold;
            color: #3498db;
        }
        .stat-label {
            color: #7f8c8d;
            font-size: 14px;
        }
    </style>
</head>
<body>
<div class="dashboard">
    <div class="user-info">
        <h2>Welcome, ${username}</h2>
    </div>
    <div class="stats">
        <div class="stat-card">
            <div class="stat-value">${stats.visits}</div>
            <div class="stat-label">Total Visits</div>
        </div>
        <div class="stat-card">
            <div class="stat-value">${stats.actions}</div>
            <div class="stat-label">Total Actions</div>
        </div>
    </div>
</div>
</body>
</html>