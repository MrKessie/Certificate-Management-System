console.log("Script loaded successfully")

$(document).ready(function() {
    $('#certificateTable').DataTable();
    $('#addCertificateFormContainer').show();
    $('#uploadCertificateFormContainer').hide();
    $('#importCertificateFormContainer').hide();
});

function showAddForm() {
    document.getElementById('addCertificateFormContainer').style.display = 'block';
    document.getElementById('uploadCertificateFormContainer').style.display = 'none';
    document.getElementById('importCertificateFormContainer').style.display = 'none';
}

function showUploadForm() {
    document.getElementById('addCertificateFormContainer').style.display = 'none';
    document.getElementById('uploadCertificateFormContainer').style.display = 'block';
    document.getElementById('importCertificateFormContainer').style.display = 'none';
}

function showImportForm() {
    document.getElementById('addCertificateFormContainer').style.display = 'none';
    document.getElementById('uploadCertificateFormContainer').style.display = 'none';
    document.getElementById('importCertificateFormContainer').style.display = 'block';
}

document.getElementById('addCertificateForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    // Get form data
    const certificateId = document.getElementById('certificateId').value;
    const studentId = document.getElementById('studentId').value;
    const studentName = document.getElementById('studentName').value;
    const programme = document.getElementById('programme').value;
    const academicYear = document.getElementById('academic-year').value;
    const department = document.getElementById('department').value;
    const graduateClass = document.getElementById('graduateClass').value;
    const file = document.getElementById('certificateFile').files[0];

    // Validate form data
    if(!certificateId || !studentId || !studentName || !programme || !academicYear || !department || !graduateClass || !file) {
        await Swal.fire({
            icon: 'warning',
            title: 'Required Fields',
            text: 'All fields are required.'
        });
        return;
    }

    if (isNaN(certificateId) || isNaN(studentId)) {
        await Swal.fire({
            icon: 'warning',
            title: 'Invalid ID',
            text: 'Enter valid Certificate ID and Student ID.'
        });
        return;
    }

    if (!/^[a-zA-Z\s]+$/.test(studentName)) {
        await Swal.fire({
            icon: 'warning',
            title: 'Invalid Student Name',
            text: 'Enter a valid Student Name'
        });
        return;
    }

    // Prepare data to send
    const formData = new FormData();
    formData.append('certificateId', certificateId);
    formData.append('studentId', studentId);
    formData.append('studentName', studentName);
    formData.append('programme', programme);
    formData.append('academicYear', academicYear);
    formData.append('department', department);
    formData.append('graduateClass', graduateClass);
    formData.append('certificateFile', file);

    try {
        // Show loading alert
        Swal.fire({
            title: 'Processing...',
            text: 'Please wait while we add the certificate.',
            allowOutsideClick: false,
            allowEscapeKey: false,
            showConfirmButton: false,
            willOpen: () => {
                Swal.showLoading();
            }
        });

        const response = await fetch('/certificate/add', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        Swal.close(); // Close the loading alert

        if (response.ok && result.status === 'success') {
            await Swal.fire({
                icon: 'success',
                title: 'Success!',
                text: result.message
            });
            document.getElementById('addCertificateForm').reset();
            window.location.reload();
        } else {
            await Swal.fire({
                icon: 'error',
                title: 'Submission Error',
                text: result.message || 'There was an error with the submission. Please try again.'
            });
        }
    } catch (error) {
        console.error('Error:', error);
        await Swal.fire({
            icon: 'error',
            title: 'Submission Error',
            text: 'There was an error with the submission. Please try again.'
        });
    }
});


function attachDeleteListeners() {
    document.querySelectorAll('.btn-danger').forEach(button => {
        button.addEventListener('click', async (event) => {
            const certificateId = event.target.dataset.certificateId; // Get the ID from the data attribute

            if (!certificateId || isNaN(certificateId)) {
                await Swal.fire({
                    icon: 'error',
                    title: 'Invalid ID',
                    text: 'Invalid Certificate ID. Please try again.'
                });
                return;
            }

            const result = await Swal.fire({
                title: 'Are you sure?',
                text: 'Are you sure you want to delete this Certificate?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes, delete it!',
                cancelButtonText: 'No, cancel!',
                reverseButtons: true
            });

            if (result.isConfirmed) {
                try {
                    const response = await fetch(`/certificate/delete/${certificateId}`, {
                        method: 'DELETE',
                    });

                    if (response.ok) {
                        await Swal.fire({
                            icon: 'success',
                            title: 'Deleted',
                            text: 'Certificate has been deleted successfully!'
                        });

                        // Remove the row from the table
                        event.target.closest('tr').remove();
                    } else {
                        const errorText = await response.text();
                        await Swal.fire({
                            icon: 'error',
                            title: 'Deletion Error',
                            text: errorText || 'There was an error deleting the Certificate. Please try again.'
                        });
                    }
                } catch (error) {
                    await Swal.fire({
                        icon: 'error',
                        title: 'Submission Error',
                        text: 'There was an error with the submission. Please try again.'
                    });
                }
            } else {
                // User canceled deletion
                await Swal.fire({
                    icon: 'info',
                    title: 'Cancelled',
                    text: 'Certificate deletion was cancelled.'
                });
            }
        });
    });
}



function loadProgrammes() {
    console.log("Loading prog...")
    fetch('/programme/all/programmes')
        .then(response => response.json())
        .then(data => {
            const programmeSelect = document.getElementById('programme');
            //facultySelect.innerHTML = ''; // Clear existing options
            data.forEach(programme => {
                let option = document.createElement('option');
                option.value = programme.programmeId;
                option.textContent = programme.programmeName;
                programmeSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading programmes:', error));
}

function loadDepartments() {
    console.log("Loading departments...")
    fetch('/department/all/departments')
        .then(response => response.json())
        .then(data => {
            const departmentSelect = document.getElementById('department');
            //facultySelect.innerHTML = ''; // Clear existing options
            data.forEach(department => {
                let option = document.createElement('option');
                option.value = department.departmentId;
                option.textContent = department.departmentName;
                departmentSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading faculties:', error));
}

function loadAcademicYears() {
    console.log("Loading academic...")
    fetch('/academic-year/all/academic-years')
        .then(response => response.json())
        .then(data => {
            const departmentSelect = document.getElementById('academic-year');
            //facultySelect.innerHTML = ''; // Clear existing options
            data.forEach(academicYear => {
                let option = document.createElement('option');
                option.value = academicYear.id;
                option.textContent = academicYear.academicYear;
                departmentSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading faculties:', error));
}

document.addEventListener("DOMContentLoaded", async function () {
    loadProgrammes()
    loadDepartments()
    loadAcademicYears();


    try {
        const response = await fetch('/certificate/all');
        if (response.ok) {
            const certificates = await response.json();

            const tableBody = document.getElementById('certificateTableBody');
            tableBody.innerHTML = ''; // Clear any existing rows

            certificates.forEach(certificate => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${certificate.certificateId}</td>
                    <td>${certificate.studentId.studentId}</td>
                    <td>${certificate.studentName}</td>
                    <td>${certificate.academicYear.academicYear}</td>
                    <td>${certificate.programme.programmeName}</td>
                    <td>${certificate.department.departmentName}</td>
                    <td>${certificate.graduateClass}</td>
                    <td>${certificate.certificatePath}</td>
                    <td>${certificate.dateAdded}</td>
                    <td>${certificate.dateEdited}</td>
                    <td>
                    <button class="btn btn-sm btn-info" onclick="editFaculty(1)">Edit</button>
                    <button class="btn btn-sm btn-danger" data-certificate-id="${certificate.certificateId}">Delete</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });

            // Attach delete event listeners after populating the table
            attachDeleteListeners();
        } else {
            throw new Error('Failed to fetch Certificate data');
        }
    } catch (error) {
        await Swal.fire({
            icon: 'error',
            title: 'Data Fetch Error',
            text: 'There was an error fetching Certificate data. Please try again.'
        });
    }
});


document.getElementById('certificateFile').addEventListener('change', function(e) {
    const file = e.target.files[0];
    if (file.type !== 'application/pdf') {
        alert('Please upload a PDF file');
        return;
    }

    const reader = new FileReader();
    reader.onload = function(event) {
        const arrayBuffer = event.target.result;

        pdfjsLib.getDocument(arrayBuffer).promise.then(function(pdf) {
            pdf.getPage(1).then(function(page) {
                page.getTextContent().then(function(textContent) {
                    const text = textContent.items.map(function(item) {
                        return item.str || item.text || '';
                    }).join(' ');

                    console.log("Extracted text:", text); // For debugging

                    // Updated regular expressions
                    const nameMatch = text.match(/certify\s+that\s+([\w\s]+)\s+having\s+pursued/i);
                    const programmeMatch = text.match(/to\s+the\s+degree\s+of\s+(Bachelor\s+of\s+(?:Arts|Science))/i);
                    const classMatch = text.match(/with\s+((?:FIRST|SECOND)\s+CLASS\s+HONOURS(?:\s*\([^)]+\))?)/i);
                    const departmentMatch = text.match(/in\s+([A-Za-z\s]+Education)/i);

                    function cleanText(text) {
                        return text.replace(/\s+/g, ' ')
                            .replace(/\(\s+/g, '(')
                            .replace(/\s+\)/g, ')')
                            .replace(/\bTec\s*hnology\b/g, 'Technology')
                            .trim();
                    }

                    if (nameMatch) {
                        document.getElementById('studentName').value = cleanText(nameMatch[1]);
                        console.log("Extracted name:", cleanText(nameMatch[1]));
                    }

                    let programmeValue = null;
                    if (programmeMatch) {
                        programmeValue = cleanText(programmeMatch[1]);
                    } else {
                        // If programmeMatch is null, try to extract it differently
                        const altProgrammeMatch = text.match(/Bachelor\s+of\s+(?:Arts|Science)/i);
                        if (altProgrammeMatch) {
                            programmeValue = cleanText(altProgrammeMatch[0]);
                        }
                    }

                    if (programmeValue) {
                        const programmeSelect = document.getElementById('programme');
                        const programmeOption = Array.from(programmeSelect.options).find(option =>
                            cleanText(option.text).toLowerCase() === programmeValue.toLowerCase()
                        );
                        if (programmeOption) {
                            programmeOption.selected = true;
                            console.log("Extracted programme:", programmeValue);
                        } else {
                            console.log("Programme not found in select options:", programmeValue);
                        }
                    } else {
                        console.log("Programme not matched in text");
                    }

                    if (classMatch) {
                        document.getElementById('graduateClass').value = cleanText(classMatch[1]);
                        console.log("Extracted class:", cleanText(classMatch[1]));
                    }

                    if (departmentMatch) {
                        const departmentSelect = document.getElementById('department');
                        const departmentValue = cleanText(departmentMatch[1]);
                        const departmentOption = Array.from(departmentSelect.options).find(option =>
                            cleanText(option.text).toLowerCase() === departmentValue.toLowerCase()
                        );
                        if (departmentOption) {
                            departmentOption.selected = true;
                            console.log("Extracted department:", departmentValue);
                        } else {
                            console.log("Department not found in select options:", departmentValue);
                        }
                    }

                    console.log("Name match:", nameMatch);
                    console.log("Programme match:", programmeMatch);
                    console.log("Class match:", classMatch);
                    console.log("Department match:", departmentMatch);
                });
            });
        }).catch(function(error) {
            console.error('Error loading PDF:', error);
        });
    };
    reader.readAsArrayBuffer(file);
});


document.getElementById('importCertificateForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const file = document.getElementById('importFile').files[0];

    if (!file) {
        await Swal.fire({
            icon: 'warning',
            title: 'No File Selected',
            text: 'Please select an Excel file to import.'
        });
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    try {
        // Show loading alert
        Swal.fire({
            title: 'Processing...',
            text: 'Please wait while we import the certificates.',
            allowOutsideClick: false,
            allowEscapeKey: false,
            showConfirmButton: false,
            willOpen: () => {
                Swal.showLoading();
            }
        });

        const response = await fetch('/certificate/import', {
            method: 'POST',
            body: formData
        });

        const results = await response.json();

        Swal.close(); // Close the loading alert

        if (response.ok) {
            let successCount = 0;
            let errorCount = 0;
            let errorMessages = [];

            results.forEach(result => {
                if (result.status === 'success') {
                    successCount++;
                } else {
                    errorCount++;
                    errorMessages.push(result.message);
                }
            });

            let message = `Successfully imported ${successCount} certificate(s).`;
            if (errorCount > 0) {
                message += `\n${errorCount} certificate(s) could not be imported.`;
            }

            await Swal.fire({
                icon: errorCount > 0 ? 'warning' : 'success',
                title: 'Import Results',
                text: message,
                showConfirmButton: true,
                confirmButtonText: 'OK'
            });

            if (errorCount > 0) {
                await Swal.fire({
                    icon: 'info',
                    title: 'Import Errors',
                    html: errorMessages.join('<br>'),
                    showConfirmButton: true,
                    confirmButtonText: 'OK'
                });
            }

            // Reset form and reload page
            document.getElementById('importCertificateForm').reset();
            window.location.reload();
        } else {
            throw new Error('Import failed');
        }
    } catch (error) {
        console.error('Error:', error);
        await Swal.fire({
            icon: 'error',
            title: 'Import Error',
            text: 'There was an error importing the certificates. Please try again.'
        });
    }
});
