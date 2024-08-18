document.getElementById('bulkVerifyForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const formData = new FormData();
    const fileInput = document.getElementById('fileInput');
    formData.append('file', fileInput.files[0]);

    fetch('/certificate/verify/bulk', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            const resultContainer = document.getElementById('resultContainer');
            resultContainer.innerHTML = '';

            data.forEach(result => {
                const resultDiv = document.createElement('div');
                resultDiv.innerHTML = `
                <h4>Student ID: ${result.studentId}</h4>
                ${result.certificateFound ? `<a href="${result.certificatePath}" target="_blank">View Certificate</a>` : '<p>Certificate not found</p>'}
            `;
                resultContainer.appendChild(resultDiv);
            });
        })
        .catch(error => {
            console.error('Error:', error);
        });
});
