$(document).ready(function() {
    $('#programmeTable').DataTable();
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

$(document).ready(function() {
    $('#programmeTable').DataTable();
});

document.getElementById('addProgrammeForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    // Get form data
    const programmeId = document.getElementById('programmeId').value;
    const programmeName = document.getElementById('programmeName').value;
    const faculty = document.getElementById('faculty').value;
    const department = document.getElementById('department').value;

    // Validate form data
    if (!programmeId || !programmeName || !faculty || !department) {
        await Swal.fire({
            icon: 'warning',
            title: 'Required Fields',
            text: 'All fields are required.'
        });
        return;
    }

    if (isNaN(programmeId)) {
        await Swal.fire({
            icon: 'warning',
            title: 'Invalid Programme ID',
            text: 'Enter a valid Programme ID.'
        });
        return;
    }

    if (!/^[a-zA-Z\s]+$/.test(programmeName)) {
        await Swal.fire({
            icon: 'warning',
            title: 'Invalid Programme Name',
            text: 'Enter a valid Programme Name'
        });
        return;
    }

    // Prepare data to send
    const formData = new FormData();
    formData.append('programmeId', programmeId);
    formData.append('programmeName', programmeName);
    formData.append('faculty', faculty);
    formData.append('department', department);

    try {
        const response = await fetch('/programme/add', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            // Check response status or JSON if needed
            const result = await response.text(); // or response.json() if server returns JSON

            // Handle response and display SweetAlert
            await Swal.fire({
                icon: 'success',
                title: 'Programme Added',
                text: 'Programme has been added successfully!'
            });

            // Optionally redirect or reset form
            document.getElementById('addProgrammeForm').reset(); // Reset form
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


function importProgrammes() {
    console.log('Importing');
    const programmeFile = document.getElementById('file');
    const form = document.getElementById('programmeImportForm');

    if (programmeFile.files.length === 0) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please select a file'
        });
        return false;
    }

    programmeFile.addEventListener('change', function(event) {
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

    fetch('/programme/import-programmes', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.addedCount > 0) {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: `${data.addedCount} programmes have been added successfully.`
                });
            }

            if (data.notAddedProgrammes && Object.keys(data.notAddedProgrammes).length > 0) {
                let message = 'The following programmes were not added:\n\n';
                for (let [id, reason] of Object.entries(data.notAddedProgrammes)) {
                    message += `Programme ID ${id}: ${reason}\n`;
                }
                Swal.fire({
                    icon: 'warning',
                    title: 'Warning',
                    text: message
                });
            }

            if ((!data.addedCount || data.addedCount === 0) &&
                (!data.notAddedProgrammes || Object.keys(data.notAddedProgrammes).length === 0)) {
                Swal.fire({
                    icon: 'info',
                    title: 'Information',
                    text: 'No Programmes were added. The file might be empty.'
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'An error occurred while importing programmes.'
            });
        });

    return false; // Prevent form from submitting traditionally
}

function attachDeleteListeners() {
    document.querySelectorAll('.btn-danger').forEach(button => {
        button.addEventListener('click', async (event) => {
            const programmeId = event.target.dataset.programmeId; // Get the ID from the data attribute

            if (!programmeId || isNaN(programmeId)) {
                await Swal.fire({
                    icon: 'error',
                    title: 'Invalid ID',
                    text: 'Invalid Programme ID. Please try again.'
                });
                return;
            }

            const result = await Swal.fire({
                title: 'Are you sure?',
                text: 'Are you sure you want to delete this Programme?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes, delete it!',
                cancelButtonText: 'No, cancel!',
                reverseButtons: true
            });

            if (result.isConfirmed) {
                try {
                    const response = await fetch(`/programme/delete/${programmeId}`, {
                        method: 'DELETE',
                    });

                    if (response.ok) {
                        await Swal.fire({
                            icon: 'success',
                            title: 'Deleted',
                            text: 'Programme has been deleted successfully!'
                        });

                        // Remove the row from the table
                        event.target.closest('tr').remove();
                    } else {
                        const errorText = await response.text();
                        await Swal.fire({
                            icon: 'error',
                            title: 'Deletion Error',
                            text: errorText || 'There was an error deleting the Programme. Please try again.'
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
                    text: 'Programme deletion was cancelled.'
                });
            }
        });
    });
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




// Initial call to load select controls and table when the DOM is fully loaded
document.addEventListener("DOMContentLoaded", async function () {
    loadFaculties();
    loadDepartments()
    showEditForm();


    try {
        const response = await fetch('/programme/all');
        if (response.ok) {
            const programmes = await response.json();

            const tableBody = document.getElementById('programmeTableBody');
            tableBody.innerHTML = ''; // Clear any existing rows

            programmes.forEach(programme => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td class="programme-id">${programme.programmeId}</td>
                    <td>${programme.programmeName}</td>
                    <td>${programme.faculty.facultyName}</td>
                    <td>${programme.department.departmentName}</td>
                    <td>${programme.dateAdded}</td>
                    <td>${programme.dateEdited}</td>
                    <td>
                    <button class="btn btn-sm btn-info">Edit</button>
                    <button class="btn btn-sm btn-danger" data-programme-id="${programme.programmeId}">Delete</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });

            // Attach delete event listeners after populating the table
            attachDeleteListeners();
        } else {
            throw new Error('Failed to fetch Programme data');
        }
    } catch (error) {
        await Swal.fire({
            icon: 'error',
            title: 'Data Fetch Error',
            text: 'There was an error fetching Programme data. Please try again.'
        });
    }
});


function showEditForm() {
    const programmeTable = document.getElementById('programmeTableBody');
    const editModal = document.getElementById('editModal');
    const editProgrammeId = document.getElementById('editProgrammeId');
    const editProgrammeName = document.getElementById('editProgrammeName');
    const editDepartment = document.getElementById('editDepartment');
    const editFaculty = document.getElementById('editFaculty');

    programmeTable.addEventListener('click', function(e) {
        if (e.target.classList.contains('btn-info')) {
            const row = e.target.closest('tr');
            const programmeId = row.querySelector('.programme-id').textContent;
            const programmeName = row.querySelector('td:nth-child(2)').textContent;
            const facultyName = row.querySelector('td:nth-child(3)').textContent
            const departmentName = row.querySelector('td:nth-child(4)').textContent

            editProgrammeId.value = programmeId;
            editProgrammeName.value = programmeName;

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

            $(editModal).modal('show');
        }
    });
}

function submitEditForm() {
    const formData = {
        programmeId: document.getElementById('editProgrammeId').value,
        programmeName: document.getElementById('editProgrammeName').value,
        faculty: {facultyId: parseInt(document.getElementById('editFaculty').value, 10)},
        department: {departmentId: parseInt(document.getElementById('editDepartment').value, 10)}
    };

    fetch('/programme/update', {
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
                    title: 'Programme Update',
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