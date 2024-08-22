console.log("Verify script loaded successfully")

$('#showVerifyForm').on('click', function() {
    $('#getCertificateFormContainer').hide();
    $('#verifyCertificateFormContainer').show();
});

$('#showGetForm').on('click', function() {
    $('#verifyCertificateFormContainer').hide();
    $('#getCertificateFormContainer').show();
    $('#resultsVerify').hide();
});


document.getElementById("getCertificateForm").addEventListener("submit", async function (event) {
    event.preventDefault();
    console.log("Verifying certificate")

    // const currentUser = {userId: document.getElementById("issuer").value};
    const studentId = document.getElementById("getStudentId").value ;
    const resultsContainer = document.getElementById("resultsGet");
    const errorContainer = document.getElementById("errorGet");
    const getBtn = document.getElementById("getBtn")

    if (!studentId) {
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

    const formData = new FormData();
    // formData.append('issuer', issuer);
    formData.append('studentId', studentId);

    Swal.fire({
        title: 'Processing...',
        text: 'Please wait while we get the certificate.',
        allowOutsideClick: false,
        allowEscapeKey: false,
        showConfirmButton: false,
        willOpen: () => {
            Swal.showLoading();
        }
    });

    fetch(`/certificate/issue/student/${studentId}`, {method: 'GET'})
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



document.getElementById('bulkIssueForm').addEventListener('submit', function(e) {
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

    fetch('/certificate/issue/bulk', {
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
    const tableBody = document.querySelector('#resultsTable tbody');
    tableBody.innerHTML = '';

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
    });

    document.getElementById('resultsVerify').style.display = 'block';
}

function showError(message) {
    const errorDiv = document.getElementById('errorVerify');
    errorDiv.textContent = message;
    errorDiv.style.display = 'block';
}


// document.addEventListener('DOMContentLoaded', function() {
//     const bulkVerifyForm = document.getElementById('bulkVerifyForm');
//     const fileInput = document.getElementById('file');
//     const errorVerify = document.getElementById('errorVerify');
//     const resultsCard = document.getElementById('resultsCard');
//     const resultsTable = document.getElementById('resultsTable');
//
//     bulkVerifyForm.addEventListener('submit', function(e) {
//         e.preventDefault();
//
//         const file = fileInput.files[0];
//
//         if (!file) {
//             showError('Please select a file to upload.');
//             return;
//         }
//
//         const formData = new FormData();
//         formData.append('file', file);
//
//         Swal.fire({
//             title: 'Processing...',
//             text: 'Please wait while we verify the certificates.',
//             allowOutsideClick: false,
//             allowEscapeKey: false,
//             showConfirmButton: false,
//             willOpen: () => {
//                 Swal.showLoading();
//             }
//         });
//
//         fetch('/certificate/verify-bulk', {
//             method: 'POST',
//             body: formData
//         })
//             .then(response => {
//                 if (!response.ok) {
//                     throw new Error('Network response was not ok');
//                 }
//                 return response.json();
//             })
//             .then(data => {
//                 Swal.close();
//                 displayResults(data);
//             })
//             .catch(error => {
//                 console.error('Error:', error);
//                 Swal.close();
//                 showError('An error occurred while processing the file.');
//             });
//     });
//
//     function displayResults(results) {
//         const tableBody = resultsTable.querySelector('tbody');
//         tableBody.innerHTML = '';
//
//         results.forEach(result => {
//             const row = document.createElement('tr');
//             row.innerHTML = `
//                 <td>${result.studentId || 'N/A'}</td>
//                 <td>${result.exists ? 'Verified' : 'Not Found'}</td>
//                 <td>${result.name || 'N/A'}</td>
//                 <td>${result.programme || 'N/A'}</td>
//                 <td>${result.department || 'N/A'}</td>
//                 <td>${result.academicYear || 'N/A'}</td>
//                 <td>${result.classHonours || 'N/A'}</td>
//                 <td>
//                     ${result.exists
//                 ? `<a href="${result.viewLink}" class="btn btn-sm btn-primary" target="_blank">View</a>`
//                 : result.message || 'N/A'}
//                 </td>
//             `;
//             tableBody.appendChild(row);
//         });
//
//         resultsCard.style.display = 'block';
//     }
//
//     function showError(message) {
//         errorVerify.textContent = message;
//         errorVerify.style.display = 'block';
//     }
// });
