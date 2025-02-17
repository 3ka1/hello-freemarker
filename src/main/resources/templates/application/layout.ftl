<#macro mainLayout>
    <!DOCTYPE html>
    <html>
    <head>
        <title>Ktor Template Tester</title>
        <style>
            body {
                margin: 0;
                padding: 0;
                font-family: Arial, sans-serif;
                display: flex;
            }

            .nav-sidebar {
                width: 200px;
                background-color: #2c3e50;
                color: white;
                height: 100vh;
                padding: 20px 0;
                position: fixed;
                left: 0;
                top: 0;
                overflow-y: auto;
            }

            .nav-sidebar h2 {
                padding: 0 20px;
                margin-bottom: 20px;
                font-size: 1.2em;
            }

            .nav-sidebar ul {
                list-style: none;
                padding: 0;
                margin: 0;
            }

            .nav-sidebar li {
                padding: 10px 20px;
                cursor: pointer;
                transition: background-color 0.3s;
                font-size: 0.9em;
            }

            .nav-sidebar li:hover {
                background-color: #34495e;
            }

            .nav-sidebar li.active {
                background-color: #3498db;
            }

            .main-content {
                margin-left: 200px;
                padding: 20px;
                flex: 1;
                max-width: 800px;
            }

            textarea {
                width: 100%;
                height: 200px;
                padding: 10px;
                margin-bottom: 10px;
                font-family: monospace;
                font-size: 14px;
            }

            button {
                padding: 10px 20px;
                background-color: #3498db;
                color: white;
                border: none;
                cursor: pointer;
            }

            button:hover {
                background-color: #2980b9;
            }

            button:disabled {
                background-color: #bdc3c7;
                cursor: not-allowed;
            }

            .sample {
                background-color: #f8f9fa;
                padding: 10px;
                border-radius: 4px;
                margin-top: 10px;
            }

            .error {
                color: #e74c3c;
                margin-top: 5px;
                display: none;
            }

            #result {
                margin-top: 20px;
                padding: 15px;
                border: 1px solid #ddd;
                display: none;
            }

            .template-info {
                margin-bottom: 20px;
                padding: 15px;
                background-color: #f8f9fa;
                border-radius: 4px;
            }
        </style>
    </head>
    <body>
    <div class="nav-sidebar">
        <h2>Templates</h2>
        <ul id="templateList">
            <li data-template="greeting" class="active">Greeting Template</li>
            <li data-template="welcome">Welcome Template</li>
            <li data-template="dashboard">Dashboard Template</li>
        </ul>
    </div>

    <div class="main-content">
        <#nested>
    </div>
    </body>
    </html>
</#macro>

// src/main/resources/templates/application/index.ftl
<#import "layout.ftl" as layout>
<@layout.mainLayout>
    <div class="template-info">
        <h2 id="currentTemplate">Greeting Template</h2>
        <p id="templateDescription">A simple template for displaying greetings with sender information and
            timestamp.</p>
    </div>

    <div class="form-group">
        <form id="jsonForm">
            <div class="form-group">
                <label for="jsonInput">JSON Input:</label>
                <textarea id="jsonInput" name="jsonInput" required placeholder='Enter JSON here...'></textarea>
                <div id="jsonError" class="error"></div>
            </div>

            <div class="sample">
                <label>Sample JSON Format:</label>
                <pre id="sampleJson"></pre>
            </div>

            <button type="submit" id="submitBtn">Submit</button>
        </form>
    </div>

    <div id="result"></div>

    <script>
        const templates = {
            greeting: {
                name: "Greeting Template",
                description: "A simple template for displaying greetings with sender information and timestamp.",
                endpoint: "/greet",
                sample: {
                    message: "Your greeting message",
                    sender: "Your name"
                }
            },
            welcome: {
                name: "Welcome Template",
                description: "A welcome page template with user information.",
                endpoint: "/welcome",
                sample: {
                    message: "Welcome message",
                    sender: "Administrator"
                }
            },
            dashboard: {
                name: "Dashboard Template",
                description: "A dashboard layout with user stats and information.",
                endpoint: "/dashboard",
                sample: {
                    username: "user123",
                    stats: {
                        visits: 100,
                        actions: 50
                    }
                }
            }
        };

        let currentTemplate = 'greeting';

        function updateTemplateInfo(templateId) {
            const template = templates[templateId];
            document.getElementById('currentTemplate').textContent = template.name;
            document.getElementById('templateDescription').textContent = template.description;
            document.getElementById('sampleJson').textContent = JSON.stringify(template.sample, null, 4);
        }

        document.querySelectorAll('#templateList li').forEach(li => {
            li.addEventListener('click', () => {
                document.querySelectorAll('#templateList li').forEach(item => item.classList.remove('active'));
                li.classList.add('active');
                currentTemplate = li.dataset.template;
                updateTemplateInfo(currentTemplate);
            });
        });

        const jsonInput = document.getElementById('jsonInput');
        const jsonError = document.getElementById('jsonError');
        const submitBtn = document.getElementById('submitBtn');

        function validateJSON(text) {
            try {
                JSON.parse(text);
                return true;
            } catch (e) {
                return false;
            }
        }

        jsonInput.addEventListener('input', () => {
            const isValid = validateJSON(jsonInput.value);
            jsonError.style.display = isValid ? 'none' : 'block';
            jsonError.textContent = isValid ? '' : 'Invalid JSON format';
            submitBtn.disabled = !isValid;
        });

        document.getElementById('jsonForm').addEventListener('submit', async (e) => {
            e.preventDefault();

            try {
                const jsonData = JSON.parse(jsonInput.value);
                const template = templates[currentTemplate];

                const response = await fetch(template.endpoint, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(jsonData)
                });

                const resultDiv = document.getElementById('result');

                if (response.ok) {
                    const html = await response.text();
                    resultDiv.innerHTML = html;
                    resultDiv.style.display = 'block';
                } else {
                    resultDiv.innerHTML = 'Error: ' + (await response.text());
                    resultDiv.style.display = 'block';
                }
            } catch (error) {
                console.error('Error:', error);
                document.getElementById('result').innerHTML = 'Error: ' + error.message;
                document.getElementById('result').style.display = 'block';
            }
        });

        document.querySelector('.sample').addEventListener('click', () => {
            const template = templates[currentTemplate];
            jsonInput.value = JSON.stringify(template.sample, null, 4);
            jsonInput.dispatchEvent(new Event('input'));
        });

        // Initialize with first template
        updateTemplateInfo(currentTemplate);
    </script>
</@layout.mainLayout>
