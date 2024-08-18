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

// document.getElementById('addCertificateForm').addEventListener('submit', function (event) {
//     event.preventDefault();
//
//     const formData = new FormData();
//     const studentId = document.getElementById('studentId').value;
//     const studentName = document.getElementById('studentName').value;
//     const programme = document.getElementById('programme').value;
//     const academicYear = document.getElementById('academicYear').value;
//     const department = document.getElementById('department').value;
//     const graduateClass = document.getElementById('graduate-class').value;
//     const file = document.getElementById('certificateFile').files[0];
//
//     formData.append('studentId', studentId);
//     formData.append('studentName', studentName);
//     formData.append('programme', programme);
//     formData.append('academicYear', academicYear);
//     formData.append('department', department);
//     formData.append('graduate-class', graduateClass);
//     formData.append('certificateFile', file);
//
//
//     fetch('/certificate/upload', {
//         method: 'POST',
//         body: formData
//     })
//         .then(response => {
//             if (response.ok) {
//                 return response.text(); // or response.json() if expecting JSON
//             }
//             throw new Error('Network response was not ok.');
//         })
//         .then(data => {
//             alert('Certificate uploaded successfully');
//             location.reload();
//         })
//         .catch(error => {
//             console.error('Error:', error);
//             alert('Failed to upload certificate');
//         });
// });

// document.getElementById('addCertificateForm').addEventListener('submit', function (event) {
//     event.preventDefault();
//     const formData = new FormData();
//     const studentId = document.getElementById('studentId').value;
//     const studentName = document.getElementById('studentName').value;
//     const programme = document.getElementById('programme').value;
//     const academicYear = document.getElementById('academicYear').value;
//     const department = document.getElementById('department').value;
//     const graduateClass = document.getElementById('graduate-class').value;
//     const file = document.getElementById('certificateFile').files[0];
//
//     formData.append('studentId', studentId);
//     formData.append('studentName', studentName);
//     formData.append('programme', programme);
//     formData.append('academicYear', academicYear);
//     formData.append('department', department);
//     formData.append('graduate-class', graduateClass);
//     formData.append('certificateFile', file);
//
//     fetch('/certificate/add', {
//         method: 'POST',
//         body: formData
//     })
//         .then(response => {
//             if (response.ok) {
//                 return response.text(); // or response.json() if expecting JSON
//             }
//             throw new Error('Network response was not ok.');
//         })
//         .then(data => {
//             alert('Certificate uploaded successfully');
//             location.reload();
//         })
//         .catch(error => {
//             console.error('Error:', error);
//             alert('Failed to upload certificate');
//         });
// });

function addCertificate() {
    const studentId = document.getElementById('studentId').value;
    const studentName = document.getElementById('studentName').value;
    const programme = document.getElementById('programme').value;
    const academicYear = document.getElementById('academicYear').value;
    const department = document.getElementById('department').value;
    const graduateClass = document.getElementById('graduate-class').value;
    const file = document.getElementById('certificateFile').files[0];
    const form = document.getElementById('addCertificateForm')

    if(!studentId || !studentName || !programme || !academicYear || !department || !graduateClass || !file) {
        alert('All fields are required');
        return false;
    }

    if (isNaN(studentId)){
        alert('Enter a valid Student ID');
        return false;
    }

    if (!/^[a-zA-Z\s]+$/.test(studentName)) {
        alert("Enter a valid Student name");
        return false;
    }

    if (!/^[a-zA-Z\s]+$/.test(graduateClass)) {
        alert("Enter a valid class");
        return false;
    }

    if (file.files.length === 0) {
        alert("Select a certificate file");
        return false;
    }

    alert("Certificate Uploaded successfully");
    return true;
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

document.addEventListener("DOMContentLoaded", function() {
    loadProgrammes()
    loadDepartments()
    loadAcademicYears();
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
                    const nameMatch = text.match(/certify that\s+([A-Z\s]+)\s+having pursued/);
                    const degreeMatch = text.match(/to the degree of\s+(Bachelor of [A-Za-z]+)\s+with/);
                    const classMatch = text.match(/with\s+(\w+\s+CLASS\s+HONOURS\s+\([^)]+\))/);
                    const departmentMatch = text.match(/in\s+([A-Za-z\s]+Education)/);

                    if (nameMatch) {
                        document.getElementById('studentName').value = nameMatch[1].trim();
                        console.log("Extracted name:", nameMatch[1].trim());
                    }
                    if (degreeMatch) {
                        const programmeSelect = document.getElementById('programme');
                        const programmeOption = Array.from(programmeSelect.options).find(option => option.text === degreeMatch[1].trim());
                        if (programmeOption) {
                            programmeOption.selected = true;
                            console.log("Extracted programme:", degreeMatch[1].trim());
                        }
                    }
                    if (classMatch) {
                        document.getElementById('graduateClass').value = classMatch[1].trim();
                        console.log("Extracted class:", classMatch[1].trim());
                    }

                    // if (departmentMatch) {
                    //     document.getElementById('department').value = departmentMatch[1].trim();
                    //     console.log("Extracted department:", departmentMatch[1].trim());
                    // }
                    if (departmentMatch) {
                        const departmentSelect = document.getElementById('department');
                        const departmentOption = Array.from(departmentSelect.options).find(option => option.text === departmentMatch[1].trim());
                        if (departmentOption) {
                            departmentOption.selected = true;
                            console.log("Extracted department:", departmentMatch[1].trim());
                        }
                    }

                    console.log("Name match:", nameMatch);
                    console.log("Degree match:", degreeMatch);
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

document.getElementById('addCertificateForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const formData = new FormData(this);

    fetch('/certificate/add', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('Certificate saved successfully');
                this.reset();
            } else {
                alert('Error saving certificate: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while saving the certificate');
        });
});


