console.log("Student script loaded");

$(document).ready(function() {
    $('#studentTable').DataTable();
});

// Toggle between forms
document.getElementById('showImportForm').addEventListener('click', function() {
    document.getElementById('addStudentForm').style.display = 'none';
    document.getElementById('importForm').style.display = 'block';
});

document.getElementById('cancelImport').addEventListener('click', function() {
    document.getElementById('importForm').style.display = 'none';
    document.getElementById('addStudentForm').style.display = 'block';
});


document.getElementById('addStudentForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    // Get form data
    const studentId = document.getElementById('studentId').value;
    const studentName = document.getElementById('studentName').value;
    const programme = document.getElementById('programme').value;
    const academicYear = document.getElementById('academicYear').value;
    const department = document.getElementById('department').value;
    const faculty = document.getElementById('faculty').value;

    // Validate form data
    if (!studentId || !studentName || !programme || !faculty || !department || !academicYear) {
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
    formData.append('studentId', studentId);
    formData.append('studentName', studentName);
    formData.append('academicYear',academicYear);
    formData.append('faculty', faculty);
    formData.append('department', department);
    formData.append('programme', programme);

    try {
        const response = await fetch('/student/add', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            // Check response status or JSON if needed
            const result = await response.text(); // or response.json() if server returns JSON

            // Handle response and display SweetAlert
            await Swal.fire({
                icon: 'success',
                title: 'Student Added',
                text: 'Student has been added successfully!'
            });

            // Optionally redirect or reset form
            document.getElementById('addStudentForm').reset(); // Reset form
            window.location.reload(); // Redirect if needed
        } else {
            const errorText = await response.text(); // or response.json() if server returns JSON

            await Swal.fire({
                icon: 'error',
                title: 'Submission Error',
                text: errorText || 'There was an error with the submission. Please try again.'
            });
        }
    } catch (error) {
        await Swal.fire({
            icon: 'error',
            title: 'Submission Error',
            text: 'There was an error with the submission. Please try again.'
        });
    }
});


function importStudents() {
    console.log('Importing');
    const studentFile = document.getElementById('file');
    const form = document.getElementById('studentImportForm');

    if (studentFile.files.length === 0) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please select a file'
        });
        return false;
    }

    studentFile.addEventListener('change', function(event) {
        const file = event.target.files[0];
        if (file) {
            const validMimeTypes = [
                'application/vnd.ms-excel',
                'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
            ];
            const fileName = file.name;
            const fileExtension = fileName.split('.').pop().toLowerCase();
            if (!validMimeTypes.includes(file.type) && !['xls', 'xlsx'].includes(fileExtension)) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Please upload a valid Excel file.'
                });
                event.target.value = '';
            }
        }
    });

    const formData = new FormData(form);

    fetch('/student/import-students', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.addedCount > 0) {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: `${data.addedCount} student(s) have been added successfully.`
                });
            }

            if (data.notAddedStudents && Object.keys(data.notAddedStudents).length > 0) {
                let message = 'The following students were not added:\n\n';
                for (let [id, reason] of Object.entries(data.notAddedStudents)) {
                    message += `Student ID ${id}: ${reason}\n`;
                }
                Swal.fire({
                    icon: 'warning',
                    title: 'Warning',
                    text: message
                });
            }

            if ((!data.addedCount || data.addedCount === 0) &&
                (!data.notAddedStudents || Object.keys(data.notAddedStudents).length === 0)) {
                Swal.fire({
                    icon: 'info',
                    title: 'Information',
                    text: 'No students were added. The file might be empty.'
                });
            }

            window.location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'An error occurred while importing students.'
            });
        });

    return false; // Prevent form from submitting traditionally
}


function attachDeleteListeners() {
    document.querySelectorAll('.btn-danger').forEach(button => {
        button.addEventListener('click', async (event) => {
            const studentId = event.target.dataset.studentId; // Get the ID from the data attribute

            if (!studentId || isNaN(studentId)) {
                await Swal.fire({
                    icon: 'error',
                    title: 'Invalid ID',
                    text: 'Invalid Student ID. Please try again.'
                });
                return;
            }

            const result = await Swal.fire({
                title: 'Are you sure?',
                text: 'Are you sure you want to delete this Student?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes, delete it!',
                cancelButtonText: 'No, cancel!',
                reverseButtons: true
            });

            if (result.isConfirmed) {
                try {
                    const response = await fetch(`/student/delete/${studentId}`, {
                        method: 'DELETE',
                    });

                    if (response.ok) {
                        await Swal.fire({
                            icon: 'success',
                            title: 'Deleted',
                            text: 'Student has been deleted successfully!'
                        });

                        // Remove the row from the table
                        event.target.closest('tr').remove();
                    } else {
                        const errorText = await response.text();
                        await Swal.fire({
                            icon: 'error',
                            title: 'Deletion Error',
                            text: errorText || 'There was an error deleting the Student. Please try again.'
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
                    text: 'Student deletion was cancelled.'
                });
            }
        });
    });
}


function loadFaculties() {
    console.log("Loading faculties...")
    fetch('/faculty/all/faculties')
        .then(response => response.json())
        .then(data => {
            const facultySelect = document.getElementById('faculty');
            //facultySelect.innerHTML = ''; // Clear existing options
            data.forEach(faculty => {
                let option = document.createElement('option');
                option.value = faculty.facultyId;
                option.textContent = faculty.facultyName;
                facultySelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading faculties:', error));
}

function loadEditFaculties() {
    return fetch('/faculty/all/faculties')
        .then(response => response.json())
        .then(faculties => {
            const editFaculty = document.getElementById('editFaculty');
            editFaculty.innerHTML = '<option value="">Select Faculty</option>';
            faculties.forEach(faculty => {
                const option = document.createElement('option');
                option.value = faculty.facultyId;
                option.textContent = faculty.facultyName;
                editFaculty.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading faculties:', error));
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

function loadEditDepartments() {
    return fetch('/department/all/departments')
        .then(response => response.json())
        .then(departments => {
            const editDepartment = document.getElementById('editDepartment');
            editDepartment.innerHTML = '<option value="">Select Department</option>';
            departments.forEach(department => {
                const option = document.createElement('option');
                option.value = department.departmentId;
                option.textContent = department.departmentName;
                editDepartment.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading Departments:', error));
}

function loadProgrammes() {
    console.log("Loading prog...")
    fetch('/programme/all/programmes')
        .then(response => response.json())
        .then(data => {
            const departmentSelect = document.getElementById('programme');
            //facultySelect.innerHTML = ''; // Clear existing options
            data.forEach(programme => {
                let option = document.createElement('option');
                option.value = programme.programmeId;
                option.textContent = programme.programmeName;
                departmentSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading programmes:', error));
}

function loadEditProgrammes() {
    return fetch('/programme/all/programmes')
        .then(response => response.json())
        .then(programmes => {
            const editProgramme = document.getElementById('editProgramme');
            editProgramme.innerHTML = '<option value="">Select Programme</option>';
            programmes.forEach(programme => {
                const option = document.createElement('option');
                option.value = programme.programmeId;
                option.textContent = programme.programmeName;
                editProgramme.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading Programmes:', error));
}

function loadAcademicYears() {
    console.log("Loading academic...")
    fetch('/academic-year/all/academic-years')
        .then(response => response.json())
        .then(data => {
            const departmentSelect = document.getElementById('academicYear');
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

function loadEditAcademicYears() {
    return fetch('/academic-year/all/academic-years')
        .then(response => response.json())
        .then(academicYears => {
            const editAcademicYear = document.getElementById('editAcademicYear');
            editAcademicYear.innerHTML = '<option value="">Select Academic Year</option>';
            academicYears.forEach(academicYear => {
                const option = document.createElement('option');
                option.value = academicYear.academicYearId;
                option.textContent = academicYear.academicYear;
                editAcademicYear.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading Academic Years:', error));
}

function showEditForm() {
    const studentTable = document.getElementById('studentTableBody');
    const editModal = document.getElementById('editModal');
    const editStudentId = document.getElementById('editStudentId');
    const editStudentName = document.getElementById('editStudentName');
    const editProgramme = document.getElementById('editProgramme');
    const editDepartment = document.getElementById('editDepartment');
    const editFaculty = document.getElementById('editFaculty');
    const editAcademicYear = document.getElementById('editAcademicYear');

    studentTable.addEventListener('click', function(e) {
        if (e.target.classList.contains('btn-info')) {
            const row = e.target.closest('tr');
            const studentId = row.querySelector('.student-id').textContent;
            const studentName = row.querySelector('.student-name').textContent;
            const academicYear = row.querySelector('.academic-year').textContent;
            const facultyName = row.querySelector('.faculty-name').textContent
            const departmentName = row.querySelector('.department-name').textContent
            const programmeName = row.querySelector('.programme-name').textContent;


            editStudentId.value = studentId;
            editStudentName.value = studentName;

            loadEditAcademicYears().then(() => {
                const academicYearOption = Array.from(editAcademicYear.options).find(option => option.text === academicYear);
                if (academicYearOption) {
                    editAcademicYear.value = academicYearOption.value;
                }
            });

            loadEditFaculties().then(() => {
                const facultyOption = Array.from(editFaculty.options).find(option => option.text === facultyName);
                if (facultyOption) {
                    editFaculty.value = facultyOption.value;
                }
            });

            loadEditDepartments().then(() => {
                const departmentOption = Array.from(editDepartment.options).find(option => option.text === departmentName);
                if (departmentOption) {
                    editDepartment.value = departmentOption.value;
                }
            });

            loadEditProgrammes().then(() => {
                const programmeOption = Array.from(editProgramme.options).find(option => option.text === programmeName);
                if (programmeOption) {
                    editProgramme.value = programmeOption.value;
                }
            });

            $(editModal).modal('show');
        }
    });
}




// Initial call to load faculties when the DOM is fully loaded
document.addEventListener("DOMContentLoaded", async function () {
    loadFaculties();
    loadDepartments();
    loadProgrammes();
    loadAcademicYears();
    showEditForm();
});