console.log("Verify script loaded successfully")

$('#showVerifyForm').on('click', function() {
    $('#getCertificateFormContainer').hide();
    $('#verifyCertificateFormContainer').show();
});

$('#showGetForm').on('click', function() {
    $('#verifyCertificateFormContainer').hide();
    $('#getCertificateFormContainer').show();
});

// document.getElementById('verifyCertificateForm').addEventListener('submit', function (event) {
//     event.preventDefault();
//
//     const studentId = document.getElementById('verifyStudentId').value;
//
//     fetch(`/certificate/student/${studentId}`)
//         .then(response => {
//             if (!response.ok) {
//                 throw new Error('Certificate not found for this student ID.');
//             }
//             return response.json();
//         })
//         .then(data => {
//             if (data.certificateFile) {
//                 const baseUrl = 'http://localhost/';  // Base URL for your XAMPP server
//                 const certificateFilePath = data.certificateFile.replace(/^C:\\xampp\\htdocs\\/, '');
//                 const viewLink = `${baseUrl}${certificateFilePath}`;
//                 const results = document.getElementById('result');
//
//                 results.innerHTML = `
//                 <h4>Certificate Found.</h4>
//                 <h4>Student ID: ${studentId}</h4>
//                 <a href="${viewLink}" target="_blank"> <h4>View Certificate</h4></a>
//             `;
//             } else {
//                 document.getElementById('result').innerHTML = `
//                 <h4>No Certificate Found</h4>
//                 <h4>Student ID: ${studentId}</h4>
//             `;
//             }
//         })
//         .catch(error => {
//             console.error('Error:', error);
//             alert('User Id does not exists');
//         });
// });


// document.getElementById('getCertificateForm').addEventListener('submit', function (event) {
//     event.preventDefault();
//
//     const studentId = document.getElementById('verifyStudentId').value;
//
//     fetch(`/certificate/student/${studentId}`)
//         .then(response => {
//             if (!response.ok) {
//                 throw new Error('Certificate not found for this student ID.');
//             }
//             return response.json();
//         })
//         .then(data => {
//             if (data.certificateFile) {
//                 const baseUrl = 'http://localhost/';  // Base URL for your XAMPP server
//                 const certificateFilePath = data.certificateFile.replace(/^C:\\xampp\\htdocs\\/, '');
//                 const viewLink = `${baseUrl}${certificateFilePath}`;
//
//                 console.log('Constructed View Link:', viewLink);
//
//                 const results = document.getElementById('resultsGet');
//                 const status = document.getElementById('statusGet');
//                 const name = document.getElementById('nameGet');
//                 const programme = document.getElementById('programmeGet');
//                 const course = document.getElementById('courseGet');
//                 const classHonors = document.getElementById('classHonorsGet');
//                 const viewCertificate = document.getElementById('viewCertificateGet');
//
//                 results.innerHTML = `
//                 <h4>Certificate Found.</h4>
//                 <h4>Student ID: ${studentId}</h4>
//                 <a href="${viewLink}" target="_blank"><h4>View Certificate</h4></a>
//             `;
//             } else {
//                 document.getElementById('result').innerHTML = `
//                 <h4>No Certificate Found</h4>
//                 <h4>Student ID: ${studentId}</h4>
//             `;
//             }
//         })
//         .catch(error => {
//             console.error('Error:', error);
//             alert('Failed to verify certificate');
//         });
// });
//
//
//
// document.getElementById('getCertificateForm').addEventListener('submit', function (event) {
//     event.preventDefault();
//
//     const studentId = document.getElementById('getStudentId').value;
//
//     fetch(`/certificate/student/${studentId}`)
//         .then(response => response.json())
//         .then(data => {
//             if (data) {
//                 const results = document.getElementById('result');
//                 results.innerHTML = `
//                 <h4>Certificate Found.</h4>
//                 <h4>Student ID: ${studentId}</h4>
//                 <a href="${data.certificateFile}" target="_blank">View Certificate</a>
//             `;
//             } else {
//                 document.getElementById('result').innerHTML = `
//                 <h3>No Certificate Found</h3>
//                 <p>Student ID: ${studentId}</p>
//             `;
//             }
//         })
//         .catch(error => {
//             console.error('Error:', error);
//             alert('Failed to verify certificate');
//         });
// });


document.getElementById("getCertificateForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const studentId = document.getElementById("getStudentId").value;
    const resultsContainer = document.getElementById("resultsGet");
    const errorContainer = document.getElementById("errorGet");
    const getBtn = document.getElementById("getBtn")

    fetch(`/certificate/student/${studentId}`, { method: 'GET' })
        .then(response => response.json())
        .then(data => {
            if (data.exists) {
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



document.getElementById("verifyCertificateForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const fileInput = document.getElementById("file").value;
    const file = fileInput.files[0];
    const resultsContainer = document.getElementById("resultsVerify");
    const errorContainer = document.getElementById("errorVerify");
    const submitBtn = this.querySelector("button[type='submit']");

    if (!file) {
        alert("Select an excel file");
        return;
    }

    submitBtn.disabled = true;
    submitBtn.textContent = 'Processing...';

    const formData = new FormData();
    formData.append('file', file);

    fetch(`/certificate/bulk-verify`, {
        method: 'GET',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            console.log("Response data:", data); // Debug: Log the entire response

            const tableBody = document.querySelector("#resultsTable tbody");
            tableBody.innerHTML = "";

            if (!Array.isArray(data)) {
                throw new Error("Expected an array of results, but got: " + JSON.stringify(data));
            }

            data.forEach(result => {
                console.log("Processing result:", result);
                const row = tableBody.insertRow();
                row.insertCell().textContent = result.studentId;
                row.insertCell().textContent = "Certificate Found" || "N/A";
                row.insertCell().textContent = result.name || "N/A";
                row.insertCell().textContent = result.programme.programmeName || "N/A";
                row.insertCell().textContent = result.department.departmentName || "N/A";
                row.insertCell().textContent = result.academicYear.academicYear || "N/A";
                row.insertCell().textContent = result.classHonours || "N/A";

                const actionCell = row.insertCell();
                if (result.exists && result.viewLink) {
                    const viewLink = document.createElement('a');
                    viewLink.href = result.viewLink;
                    viewLink.textContent = "View Certificate";
                    viewLink.target = "_blank";
                    actionCell.appendChild(viewLink);
                } else {
                    actionCell.textContent = "N/A";
                }
            });

            resultsContainer.style.display = "block";
            errorContainer.style.display = "none";
        })
        .catch(error => {
            console.error('Error:', error);
            errorContainer.textContent = "An error occurred while processing the file.";
        })
        .finally(() => {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Verify Certificates';
        });
});
