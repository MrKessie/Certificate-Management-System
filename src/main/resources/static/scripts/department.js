console.log("Department script loaded")

// Initialize DataTable
$(document).ready(function() {
    $('#departmentTable').DataTable();
});

document.getElementById('addDepartmentForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    // Get form data
    const departmentId = document.getElementById('departmentId').value;
    const departmentName = document.getElementById('departmentName').value;
    const faculty = document.getElementById('faculty').value;

    // Validate form data
    if (!departmentId || !departmentName || !faculty) {
        await Swal.fire({
            icon: 'warning',
            title: 'Required Fields',
            text: 'All fields are required.'
        });
        return;
    }

    if (isNaN(departmentId)) {
        await Swal.fire({
            icon: 'warning',
            title: 'Invalid Department ID',
            text: 'Enter a valid Department ID.'
        });
        return;
    }

    if (!/^[a-zA-Z\s]+$/.test(departmentName)) {
        await Swal.fire({
            icon: 'warning',
            title: 'Invalid Department Name',
            text: 'Enter a valid Department Name'
        });
        return;
    }

    // Prepare data to send
    const formData = new FormData();
    formData.append('departmentId', departmentId);
    formData.append('departmentName', departmentName);
    formData.append('faculty', faculty)

    try {
        const response = await fetch('/department/add', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            // Check response status or JSON if needed
            const result = await response.text(); // or response.json() if server returns JSON

            // Handle response and display SweetAlert
            await Swal.fire({
                icon: 'success',
                title: 'Department Added',
                text: 'Department has been added successfully!'
            });

            // Optionally redirect or reset form
            document.getElementById('addDepartmentForm').reset(); // Reset form
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


document.addEventListener('DOMContentLoaded', async () => {
    loadFaculties();
});


function attachDeleteListeners() {
    document.querySelectorAll('.btn-danger').forEach(button => {
        button.addEventListener('click', async (event) => {
            const departmentId = event.target.dataset.departmentId; // Get the ID from the data attribute

            if (!departmentId || isNaN(departmentId)) {
                await Swal.fire({
                    icon: 'error',
                    title: 'Invalid ID',
                    text: 'Invalid Department ID. Please try again.'
                });
                return;
            }

            const result = await Swal.fire({
                title: 'Are you sure?',
                text: 'Are you sure you want to delete this Department?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes, delete it!',
                cancelButtonText: 'No, cancel!',
                reverseButtons: true
            });

            if (result.isConfirmed) {
                try {
                    const response = await fetch(`/department/delete/${departmentId}`, {
                        method: 'DELETE',
                    });

                    if (response.ok) {
                        await Swal.fire({
                            icon: 'success',
                            title: 'Deleted',
                            text: 'Department has been deleted successfully!'
                        });

                        // Remove the row from the table
                        event.target.closest('tr').remove();
                    } else {
                        const errorText = await response.text();
                        await Swal.fire({
                            icon: 'error',
                            title: 'Deletion Error',
                            text: errorText || 'There was an error deleting the Department. Please try again.'
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
                    text: 'Department deletion was cancelled.'
                });
            }
        });
    });
}

//FUNCTION IMPORT FACULTIES
function importDepartments() {
    console.log('Importing');
    const departmentFile = document.getElementById('file');
    const form = document.getElementById('departmentImportForm');

    if (departmentFile.files.length === 0) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please select a file'
        });
        return false;
    }

    departmentFile.addEventListener('change', function(event) {
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

    fetch('/department/import-departments', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.addedCount > 0) {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: `${data.addedCount} departments have been added successfully.`
                });
            }

            if (data.notAddedDepartments && Object.keys(data.notAddedDepartments).length > 0) {
                let message = 'The following departments were not added:\n\n';
                for (let [id, reason] of Object.entries(data.notAddedDepartments)) {
                    message += `Department ID ${id}: ${reason}\n`;
                }
                Swal.fire({
                    icon: 'warning',
                    title: 'Warning',
                    text: message
                });
            }

            if ((!data.addedCount || data.addedCount === 0) &&
                (!data.notAddedDepartments || Object.keys(data.notAddedDepartments).length === 0)) {
                Swal.fire({
                    icon: 'info',
                    title: 'Information',
                    text: 'No departments were added. The file might be empty.'
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'An error occurred while importing departments.'
            });
        });

    return false; // Prevent form from submitting traditionally
}


function deleteDepartment(id) {
    if (confirm('Are you sure you want to delete this Department?')) {
        fetch(`/department/delete/${id}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    alert("Department deleted")
                    loadTableData(); // Reload table data
                } else {
                    alert('Failed to delete Department');
                }
            });
    }
}

function loadFaculties() {
    console.log("Loading")
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

// function loadEditFaculties() {
//     return fetch('/faculty/all/faculties')
//         .then(response => response.json())
//         .then(faculties => {
//             const editFaculty = document.getElementById('editFaculty');
//             editFaculty.innerHTML = '<option value="">Select Faculty</option>';
//             faculties.forEach(faculty => {
//                 const option = document.createElement('option');
//                 option.value = faculty.facultyId;
//                 option.textContent = faculty.facultyName;
//                 editFaculty.appendChild(option);
//             });
//         })
//         .catch(error => console.error('Error loading faculties:', error));
// }

function editDepartment(button) {
    // Read data attributes from the button
    const departmentId = button.getAttribute('data-department-id');
    const departmentName = button.getAttribute('data-department-name');
    const faculty = button.getAttribute('data-faculty');

    // Populate the form fields with the values
    document.getElementById('editDepartmentId').value = departmentId;
    document.getElementById('editDepartmentName').value = departmentName;
    document.getElementById('editFaculty').value = faculty;

    // Show the modal
    const editModal = new bootstrap.Modal(document.getElementById('editDepartmentModal'));
    editModal.show();
}




// function showEditForm() {
//     const departmentTable = document.getElementById('departmentTableBody');
//     const editModal = document.getElementById('editModal');
//     const editDepartmentId = document.getElementById('editDepartmentId');
//     const editDepartmentName = document.getElementById('editDepartmentName');
//     const editFaculty = document.getElementById('editFaculty');
//
//     departmentTable.addEventListener('click', function(e) {
//         if (e.target.classList.contains('btn-info')) {
//             const row = e.target.closest('tr');
//             const departmentId = row.querySelector('.department-id').textContent;
//             const departmentName = row.querySelector('td:nth-child(2)').textContent;
//             const facultyName = row.querySelector('td:nth-child(3)').textContent
//
//             editDepartmentId.value = departmentId;
//             editDepartmentName.value = departmentName;
//             // editFaculty.value = faculty;
//
//             loadEditFaculties().then(() => {
//                 const facultyOption = Array.from(editFaculty.options).find(option => option.text === facultyName);
//                 if (facultyOption) {
//                     editFaculty.value = facultyOption.value;
//                 }
//             });
//
//             $(editModal).modal('show');
//         }
//     });
// }

function submitEditForm() {
    const formData = {
        departmentId: document.getElementById('editDepartmentId').value,
        departmentName: document.getElementById('editDepartmentName').value,
        faculty: {facultyId: parseInt(document.getElementById('editFaculty').value, 10)}
    };
    console.log('Sending update data:', formData);

    fetch('/department/update', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(async data => {
            if (data.success) {
                await Swal.fire({
                    icon: 'success',
                    title: 'Department Update',
                    text: data.message
                });
                location.reload(); // Reload the page to show updated data
            } else {
                await Swal.fire({
                    icon: 'error',
                    title: 'Update Error',
                    text: data.message
                });
            }
        })
        .catch(async (error) => {
            console.error('Error:', error);
            await Swal.fire({
                icon: 'error',
                title: 'Submission Error',
                text: 'There was an error with the submission. Please try again.'
            });
        });

    $(editModal).modal('hide');
}


