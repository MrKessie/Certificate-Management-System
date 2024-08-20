console.log("Department script loaded")

// Initialize DataTable
$(document).ready(function() {
    $('#departmentTable').DataTable();
});

// Toggle between forms
document.getElementById('showImportForm').addEventListener('click', function() {
    document.getElementById('addForm').style.display = 'none';
    document.getElementById('importForm').style.display = 'block';
});

document.getElementById('cancelImport').addEventListener('click', function() {
    document.getElementById('importForm').style.display = 'none';
    document.getElementById('addForm').style.display = 'block';
});


document.getElementById('departmentForm').addEventListener('submit', async function(event) {
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
            document.getElementById('departmentForm').reset(); // Reset form
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
    try {
        const response = await fetch('/department/all');
        if (response.ok) {
            const faculties = await response.json();

            const tableBody = document.getElementById('departmentTableBody');
            tableBody.innerHTML = ''; // Clear any existing rows

            faculties.forEach(department => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${department.departmentId}</td>
                    <td>${department.departmentName}</td>
                    <td>${department.faculty.facultyName}</td>
                    <td>${department.dateAdded}</td>
                    <td>${department.dateEdited}</td>
                    <td>
                    <button class="btn btn-sm btn-info" onclick="editFaculty(1)">Edit</button>
                    <button class="btn btn-sm btn-danger" data-department-id="${department.departmentId}">Delete</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });

            // Attach delete event listeners after populating the table
            attachDeleteListeners();
        } else {
            throw new Error('Failed to fetch department data');
        }
    } catch (error) {
        await Swal.fire({
            icon: 'error',
            title: 'Data Fetch Error',
            text: 'There was an error fetching department data. Please try again.'
        });
    }
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

// function loadTableData() {
//     fetch('/department/all')
//         .then(response => response.json())
//         .then(data => {
//             if (!Array.isArray(data)) {
//                 console.error("Expected an array but got:", data);
//                 return;
//             }
//             const tableBody = document.querySelector('#departmentTable tbody');
//             tableBody.innerHTML = '';
//             data.forEach(department => {
//                 const row = document.createElement('tr');
//                 row.innerHTML = `<td>${department.departmentId}</td>
//                              <td>${department.departmentName}</td>
//                              <td>${department.faculty}</td>
//                              <td>${department.dateAdded}</td>
//                              <td>${department.dateEdited}</td>
//                              <td>
//                                 <button class="btn btn-sm btn-info" onclick="editFaculty(1)">Edit</button>
//                                 <button class="btn btn-sm btn-danger" th:onclick="'deleteDepartment(' + ${department.departmentId} + ')'">Delete</button>
//                              </td>`;
//                 tableBody.appendChild(row);
//             });
//         })
//         .catch(error => console.error('Error loading table data:', error));
//
// }

// function loadTableData() {
//     console.log("Loading table data");
//     fetch('/department/all') // Adjust the endpoint to match your actual data source
//         .then(response => response.json())
//         .then(data => {
//
//             console.log("fetch data")
//             // Check if data is an array
//             if (!Array.isArray(data)) {
//                 console.error("Expected an array but got:", data);
//                 return;
//             }
//             const tableBody = document.querySelector('#departmentTable tbody');
//             tableBody.innerHTML = ''; // Clear existing table rows
//
//             console.log("for each data")
//             data.forEach(department => {
//                 const row = document.createElement('tr');
//                 // row.setAttribute('data-academic-year-id', department.id);
//                 row.innerHTML = `<td>${department.id}</td>
//                                  <td>${department.departmentId}</td>
//                                  <td>${department.departmentName}</td>
//                                  <td>${department.facultyId}</td>
//                                  <td>${department.dateAdded}</td>
//                                  <td>${department.dateEdited}</td>
//                                  <td>
//                                    <button class="btn btn-sm btn-info" th:onclick="'editDepartment(' + ${department.id} + ')'">Edit</button>
//                                    <button class="btn btn-sm btn-danger" th:onclick="'deleteDepartment(' + ${department.id} + ')'">Delete</button>
//                                  </td>`;
//                 tableBody.appendChild(row);
//             });
//         })
//         .catch(error => console.error('Error loading table data:', error));
// }

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


// Initial call to load faculties when the DOM is fully loaded
document.addEventListener("DOMContentLoaded", function() {
    loadFaculties();
});
