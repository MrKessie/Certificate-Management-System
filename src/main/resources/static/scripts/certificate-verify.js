let verifiedCertificates = [];

$('#showVerifyForm').on('click', function() {
    $('#getCertificateFormContainer').hide();
    $('#verifyCertificateFormContainer').show();
});

$('#showGetForm').on('click', function() {
    $('#verifyCertificateFormContainer').hide();
    $('#getCertificateFormContainer').show();
});



document.getElementById("getCertificateForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    const studentId = document.getElementById("verifyStudentId").value ;
    const employer = document.getElementById("employer").value;
    const organization = document.getElementById("organization").value
    const resultsContainer = document.getElementById("resultsGet");
    const errorContainer = document.getElementById("errorGet");
    const getBtn = document.getElementById("getBtn")

    if (!studentId || !employer || !organization) {
        await Swal.fire({
            icon: 'warning',
            title: 'Required Fields',
            text: 'All fields are required.'
        });
        return;
    }

    if (isNaN(studentId)) {
        await Swal.fire({
            icon: 'warning',
            title: 'Invalid Student ID',
            text: 'Enter a valid Student ID.'
        });
        return;
    }

    if (!/^[a-zA-Z\s]+$/.test(employer)) {
        await Swal.fire({
            icon: 'warning',
            title: 'Invalid Employer Name',
            text: 'Enter a valid Employer Name'
        });
        return;
    }

    if (!/^[a-zA-Z\s]+$/.test(organization)) {
        await Swal.fire({
            icon: 'warning',
            title: 'Invalid Organization Name',
            text: 'Enter a valid Organization Name'
        });
        return;
    }

    const formData = new FormData();
    formData.append('studentId', studentId);
    formData.append('employer', employer);
    formData.append('organization', organization);

    Swal.fire({
        title: 'Processing...',
        text: 'Please wait while we verify the certificate.',
        allowOutsideClick: false,
        allowEscapeKey: false,
        showConfirmButton: false,
        willOpen: () => {
            Swal.showLoading();
        }
    });

    fetch(`/certificate/verify/student/${studentId}?employer=${employer}&organization=${organization}`, {method: 'GET'})
        .then(response => response.json())
        .then(data => {
            if (data.exists) {
                Swal.close();
                // Populate the results if the certificate exists
                document.getElementById("statusGet").textContent = "Certificate Found";
                document.getElementById("studentIdGet").textContent = studentId;
                document.getElementById("nameGet").textContent = data.name;
                document.getElementById("programmeGet").textContent = data.programme.programmeName;
                document.getElementById("departmentGet").textContent = data.department.departmentName;
                document.getElementById("academicYearGet").textContent = data.academicYear.academicYear;
                document.getElementById("classHonoursGet").textContent = data.classHonours;
                document.getElementById("viewCertificateGet").setAttribute("href", data.viewLink);
                // console.log("View Link: " + data.viewLink);
                resultsContainer.classList.remove("hidden");
                errorContainer.textContent = "";
            } else {
                Swal.close();
                // Display "N/A" if the certificate does not exist
                document.getElementById("statusGet").textContent = "N/A";
                document.getElementById("studentIdGet").textContent = "N/A";
                document.getElementById("nameGet").textContent = "N/A";
                document.getElementById("programmeGet").textContent = "N/A";
                document.getElementById("departmentGet").textContent = "N/A";
                document.getElementById("academicYearGet").textContent = "N/A";
                document.getElementById("classHonoursGet").textContent = "N/A";
                document.getElementById("viewCertificateGet").removeAttribute("href");
                resultsContainer.classList.remove("hidden");
                errorContainer.textContent = "Certificate not found.";
            }
        })
        .catch(error => {
            console.error('Error:', error);
            errorContainer.textContent = "An error occurred while fetching the certificate.";
        });
});


document.getElementById('verifyCertificateForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const fileInput = document.getElementById('file');
    const file = fileInput.files[0];

    if (!file) {
        showError('Please select a file to upload.');
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    Swal.fire({
        title: 'Processing...',
        text: 'Please wait while we verify the certificates.',
        allowOutsideClick: false,
        allowEscapeKey: false,
        showConfirmButton: false,
        willOpen: () => {
            Swal.showLoading();
        }
    });

    fetch('/certificate/verify/bulk', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            Swal.close();
            displayResults(data);
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.close();
            showError('An error occurred while processing the file.');
        });
});

function displayResults(results) {
    const tableBody = document.querySelector('#resultsVerifyTable tbody');
    tableBody.innerHTML = '';
    verifiedCertificates = [];

    results.forEach(result => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${result.studentId || 'N/A'}</td>
            <td>${result.exists ? 'Verified' : 'Not Found'}</td>
            <td>${result.name || 'N/A'}</td>
            <td>${result.programme || 'N/A'}</td>
            <td>${result.department || 'N/A'}</td>
            <td>${result.academicYear || 'N/A'}</td>
            <td>${result.classHonours || 'N/A'}</td>
            <td>
                ${result.exists
            ? `<a href="${result.viewLink}" class="btn btn-sm btn-primary" target="_blank">View</a>`
            : result.message || 'N/A'}
            </td>
        `;
        tableBody.appendChild(row);

        if (result.exists) {
            verifiedCertificates.push(result);
        }
    });

    document.getElementById('resultsBulkTable').style.display = 'block';

    const printButton = document.getElementById('printVerifiedBtn');
    const printPdfBtn = document.getElementById('printCertificatesPdfBtn');

    if (printButton && printPdfBtn) {
        const hasVerifiedCertificates = verifiedCertificates.length > 0;
        printButton.style.display = hasVerifiedCertificates ? 'inline-block' : 'none';
        printPdfBtn.style.display = hasVerifiedCertificates ? 'inline-block' : 'none';
    }
}


function showError(message) {
    const errorDiv = document.getElementById('errorVerify');
    errorDiv.textContent = message;
    errorDiv.style.display = 'block';
}


// Add this new function for printing
function printVerifiedCertificates() {
    const printWindow = window.open('', '_blank');
    printWindow.document.write(`
        <html lang="en">
        <head>
            <title>Verified Certificates</title>
            <style>
                body { font-family: Arial, sans-serif; }
                table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
                th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                th { background-color: #f2f2f2; }
            </style>
        </head>
        <body>
            <h1>Verified Certificates</h1>
            ${verifiedCertificates.map(cert => `
                <table>
                    <tr><th>Student ID</th><td>${cert.studentId}</td></tr>
                    <tr><th>Name</th><td>${cert.name}</td></tr>
                    <tr><th>Programme</th><td>${cert.programme}</td></tr>
                    <tr><th>Department</th><td>${cert.department}</td></tr>
                    <tr><th>Academic Year</th><td>${cert.academicYear}</td></tr>
                    <tr><th>Class</th><td>${cert.classHonours}</td></tr>
                </table>
            `).join('')}
        </body>
        </html>
    `);
    printWindow.document.close();
    printWindow.print();
}


// New function to print PDF certificates
function printCertificatesPdf() {
    // Create an invisible iframe
    const iframe = document.createElement('iframe');
    iframe.style.display = 'none';
    document.body.appendChild(iframe);

    let printCount = 0;

    function printNext() {
        if (printCount < verifiedCertificates.length) {
            const cert = verifiedCertificates[printCount];
            iframe.src = cert.viewLink; // Assuming viewLink is the URL to the PDF
            iframe.onload = function() {
                setTimeout(() => {
                    iframe.contentWindow.print();
                    printCount++;
                    setTimeout(printNext, 1000); // Wait for 1 second before printing next
                }, 1000); // Wait for 1 second to ensure PDF is loaded
            };
        } else {
            document.body.removeChild(iframe);
        }
    }

    printNext();
}

document.getElementById('printVerifiedBtn').addEventListener('click', printVerifiedCertificates);
document.getElementById('printCertificatesPdfBtn').addEventListener('click', printCertificatesPdf);