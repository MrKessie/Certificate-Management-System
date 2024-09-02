console.log("Verify script loaded successfully")

let verifiedCertificates = [];
let signaturePad;
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

    const studentId = document.getElementById("getStudentId").value ;
    // const userId = document.getElementById("userId").value
    const collectorName = document.getElementById("collectorName").value
    const resultsContainer = document.getElementById("resultsGet");
    const errorContainer = document.getElementById("errorGet");
    const printButton = document.getElementById("printVerifiedBtn")

    if (!studentId || !collectorName) {
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

    if (!/^[a-zA-Z\s]+$/.test(collectorName)) {
        await Swal.fire({
            icon: 'warning',
            title: 'Invalid Collector Name',
            text: 'Enter a valid Collector Name'
        });
        return;
    }

    const formData = new FormData();
    // formData.append('issuer', issuer);
    formData.append('studentId', studentId);
    // formData.append('userId', userId);
    formData.append('collectorName', collectorName);


    // Get signature data
    if (signaturePad.isEmpty()) {
        await Swal.fire({
            icon: 'warning',
            title: 'Signature Required',
            text: 'Please provide a signature.'
        });
        return;
    }

    const signatureBase64 = signaturePad.toDataURL().split(',')[1];
    formData.append('signature', signatureBase64);

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

    fetch(`/certificate-issue/student/${studentId}`, {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {

            Swal.close();
            if (data.success) {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: data.message
                });

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
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: data.message
                });
                    // Swal.close();
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
        text: 'Please wait while we get the certificates.',
        allowOutsideClick: false,
        allowEscapeKey: false,
        showConfirmButton: false,
        willOpen: () => {
            Swal.showLoading();
        }
    });

    fetch('/certificate-issue/bulk', {
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
    verifiedCertificates = [];

    results.forEach(result => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${result.studentId || 'N/A'}</td>
            <td>${result.exists ? 'Found' : 'Not Found'}</td>
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

    document.getElementById('resultsVerify').style.display = 'block';

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
            <title>Existing Certificates</title>
            <style>
                body { font-family: Arial, sans-serif; margin: 20px }
                table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
                th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                th { background-color: #f2f2f2; }
            </style>
        </head>
        <body>
            <h1>Verified Certificates</h1>
                <table>
                  <thead>
                  <tr>
                    <th>Student ID</th>
                    <th>Status</th>
                    <th>Student Name</th>
                    <th>Programme</th>
                    <th>Department</th>
                    <th>Academic Year</th>
                    <th>Action</th>
                  </tr>
                  </thead>
                  <tbody>
                  ${verifiedCertificates.map(cert => `
                  <tr>
                    <td>${cert.studentId}</td>
                    <td>Found</td>
                    <td>${cert.name}</td>
                    <td>${cert.programme}</td>
                    <td>${cert.department}</td>
                    <td>${cert.academicYear}</td>
                    <td>${cert.classHonours}</td>
                  </tr>
                  `).join('')}
                  </tbody>
                </table>
        </body>
        </html>
    `);
    printWindow.document.close();
    printWindow.print();
}


// New function to print PDF certificates
// function printCertificatesPdf() {
//     // Create an invisible iframe
//     const iframe = document.createElement('iframe');
//     iframe.style.display = 'none';
//     document.body.appendChild(iframe);
//
//     let printCount = 0;
//
//     function printNext() {
//         if (printCount < verifiedCertificates.length) {
//             const cert = verifiedCertificates[printCount];
//             iframe.src = cert.viewLink; // Assuming viewLink is the URL to the PDF
//             iframe.onload = function() {
//                 setTimeout(() => {
//                     iframe.contentWindow.print();
//                     printCount++;
//                     setTimeout(printNext, 1000); // Wait for 1 second before printing next
//                 }, 1000); // Wait for 1 second to ensure PDF is loaded
//             };
//         } else {
//             document.body.removeChild(iframe);
//         }
//     }
//     printNext();
// }

// function printCertificatesPdf() {
//     verifiedCertificates.forEach(cert => {
//         fetch(cert.viewLink)
//             .then(response => response.arrayBuffer())
//             .then(buffer => {
//                 const loadingTask = pdfjsLib.getDocument({ data: buffer });
//                 loadingTask.promise.then(pdf => {
//                     pdf.getPage(1).then(page => {
//                         const scale = 1.5;
//                         const viewport = page.getViewport({ scale: scale });
//                         const canvas = document.createElement('canvas');
//                         const context = canvas.getContext('2d');
//                         canvas.height = viewport.height;
//                         canvas.width = viewport.width;
//                         const renderContext = {
//                             canvasContext: context,
//                             viewport: viewport
//                         };
//                         page.render(renderContext).promise.then(() => {
//                             const printWindow = window.open('', '_blank');
//                             printWindow.document.write(`
//                                 <html lang="en">
//                                 <head>
//                                     <title>Print PDF</title>
//                                 </head>
//                                 <body>
//                                     ${canvas.outerHTML}
//                                 </body>
//                                 </html>
//                             `);
//                             printWindow.document.close();
//                             printWindow.print();
//                         });
//                     });
//                 });
//             });
//     });
// }

// function printCertificatesPdf() {
//     verifiedCertificates.forEach(cert => {
//         const proxyUrl = `/proxy/certificates?pdfPath=${encodeURIComponent(cert.viewLink)}`;
//         const newWindow = window.open(proxyUrl, '_blank');
//         if (newWindow) {
//             newWindow.print();
//         } else {
//             alert("Unable to open PDF for printing. Please check your browser settings.");
//         }
//     });
// }
function printCertificatesPdf() {
    const iframe = document.createElement('iframe');
    iframe.style.display = 'none';
    document.body.appendChild(iframe);

    verifiedCertificates.forEach(cert => {
        const proxyUrl = `/proxy/pdf?url=${encodeURIComponent(cert.viewLink)}`;
        iframe.src = proxyUrl;
        iframe.onload = function() {
            setTimeout(() => {
                iframe.contentWindow.print();
            }, 1000); // Wait for PDF to load
        };
    });
}

document.getElementById('printVerifiedBtn').addEventListener('click', printVerifiedCertificates);
document.getElementById('printCertificatesPdfBtn').addEventListener('click', printCertificatesPdf);


document.addEventListener('DOMContentLoaded', function() {
    const canvas = document.getElementById('signatureCanvas');
    signaturePad = new SignaturePad(canvas);
});



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
