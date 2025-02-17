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
