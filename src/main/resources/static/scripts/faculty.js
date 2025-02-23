console.log("Faculty JS loaded");


// Initialize DataTable
$(document).ready(function() {
    $('#facultyTable').DataTable();
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

document.getElementById('addFacultyForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    // Get form data
    const facultyId = document.getElementById('facultyId').value;
    const facultyName = document.getElementById('facultyName').value;

    // Validate form data
    if (!facultyId || !facultyName) {
        await Swal.fire({
            icon: 'warning',
            title: 'Required Fields',
            text: 'All fields are required.'
        });
        return;
    }

    if (isNaN(facultyId)) {
        await Swal.fire({
            icon: 'warning',
            title: 'Invalid Faculty ID',
            text: 'Enter a valid Faculty ID.'
        });
        return;
    }

    if (!/^[a-zA-Z\s]+$/.test(facultyName)) {
        await Swal.fire({
            icon: 'warning',
            title: 'Invalid Faculty Name',
            text: 'Enter a valid Faculty Name'
        });
        return;
    }

    // Prepare data to send
    const formData = new FormData();
    formData.append('facultyId', facultyId);
    formData.append('facultyName', facultyName);

    try {
        const response = await fetch('/faculty/add', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            // Check response status or JSON if needed
            const result = await response.text(); // or response.json() if server returns JSON

            // Handle response and display SweetAlert
            await Swal.fire({
                icon: 'success',
                title: 'Faculty Added',
                text: 'Faculty has been added successfully!'
            });

            // Optionally redirect or reset form
            document.getElementById('addFacultyForm').reset(); // Reset form
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

function importFaculties() {
    console.log('Importing');
    const facultyFile = document.getElementById('file');
    const form = document.getElementById('facultyImportForm');

    if (facultyFile.files.length === 0) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please select a file'
        });
        return false;
    }

    facultyFile.addEventListener('change', function(event) {
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

    fetch('/faculty/import-faculties', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.addedCount > 0) {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: `${data.addedCount} faculties have been added successfully.`
                });
            }

            if (data.notAddedFaculties && Object.keys(data.notAddedFaculties).length > 0) {
                let message = 'The following faculties were not added:\n\n';
                for (let [id, reason] of Object.entries(data.notAddedFaculties)) {
                    message += `Faculty ID ${id}: ${reason}\n`;
                }
                Swal.fire({
                    icon: 'warning',
                    title: 'Warning',
                    text: message
                });
            }

            if ((!data.addedCount || data.addedCount === 0) &&
                (!data.notAddedFaculties || Object.keys(data.notAddedFaculties).length === 0)) {
                Swal.fire({
                    icon: 'info',
                    title: 'Information',
                    text: 'No faculties were added. The file might be empty.'
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'An error occurred while importing faculties.'
            });
        });

    return false; // Prevent form from submitting traditionally
}


function editFaculty(button) {
    // Read data attributes from the button
    const facultyId = button.getAttribute('data-faculty-id');
    const facultyName = button.getAttribute('data-faculty-name');

    // Populate the form fields with the values
    document.getElementById('editFacultyId').value = facultyId;
    document.getElementById('editFacultyName').value = facultyName;

    // Show the modal
    const editModal = new bootstrap.Modal(document.getElementById('editFacultyModal'));
    editModal.show();
}


document.addEventListener('DOMContentLoaded', async () => {
    showEditForm();
});

function attachDeleteListeners() {
    document.querySelectorAll('.btn-danger').forEach(button => {
        button.addEventListener('click', async (event) => {
            const facultyId = event.target.dataset.facultyId; // Get the ID from the data attribute

            if (!facultyId || isNaN(facultyId)) {
                await Swal.fire({
                    icon: 'error',
                    title: 'Invalid ID',
                    text: 'Invalid Faculty ID. Please try again.'
                });
                return;
            }

            const result = await Swal.fire({
                title: 'Are you sure?',
                text: 'Are you sure you want to delete this faculty?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes, delete it!',
                cancelButtonText: 'No, cancel!',
                reverseButtons: true
            });

            if (result.isConfirmed) {
                try {
                    const response = await fetch(`/faculty/delete/${facultyId}`, {
                        method: 'DELETE',
                    });

                    if (response.ok) {
                        await Swal.fire({
                            icon: 'success',
                            title: 'Deleted',
                            text: 'Faculty has been deleted successfully!'
                        });

                        // Remove the row from the table
                        event.target.closest('tr').remove();
                    } else {
                        const errorText = await response.text();
                        await Swal.fire({
                            icon: 'error',
                            title: 'Deletion Error',
                            text: errorText || 'There was an error deleting the faculty. Please try again.'
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
                    text: 'Faculty deletion was cancelled.'
                });
            }
        });
    });
}

function attachEditListeners() {
    document.querySelectorAll('.btn-info').forEach(button => {
        button.addEventListener('click', async (event) => {
            const facultyId = event.target.dataset.facultyId;

            try {
                const response = await fetch(`/faculty/edit/${facultyId}`);
                if (response.ok) {
                    const faculty = await response.json();

                    // Pre-fill the form with the existing data
                    document.getElementById('editFacultyId').value = faculty.facultyId;
                    document.getElementById('editFacultyName').value = faculty.facultyName;

                    // Show the edit modal
                    const myModal = new bootstrap.Modal(document.getElementById('editFacultyModal'));
                    myModal.show();
                } else {
                    await Swal.fire({
                        icon: 'error',
                        title: 'Fetch Error',
                        text: 'There was an error fetching the faculty details. Please try again.'
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
    });
}
//FUNCTION IMPORT FACULTIES


function showEditForm() {
    const facultyTable = document.getElementById('facultyTableBody');
    const editModal = document.getElementById('editModal');
    const editForm = document.getElementById('editForm');
    const editFacultyId = document.getElementById('editFacultyId');
    const editFacultyName = document.getElementById('editFacultyName');

    facultyTable.addEventListener('click', function(e) {
        if (e.target.classList.contains('btn-info')) {
            const row = e.target.closest('tr');
            const facultyId = row.querySelector('.faculty-id').textContent;
            const facultyName = row.querySelector('td:nth-child(2)').textContent;

            editFacultyId.value = facultyId;
            editFacultyName.value = facultyName;

            $(editModal).modal('show');
        }
    });
}

function submitEditForm() {
    const formData = {
        facultyId: document.getElementById('editFacultyId').value,
        facultyName: document.getElementById('editFacultyName').value
    };

    fetch('/faculty/update', {
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
                    title: 'Faculty Update',
                    text: data.message
                });
                location.reload(); // Reload the page to show updated data
            } else {
                await Swal.fire({
                    icon: 'error',
                    title: 'Update Error',
                    text: 'Error updating Faculty:' + data.message
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

