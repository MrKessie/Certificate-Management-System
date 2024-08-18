//FUNCTION VALIDATE FORM CONTROLS AND SUBMIT
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


document.addEventListener('DOMContentLoaded', async () => {
    attachEditListeners();
    $('#editFacultyFormModal').on('show.bs.modal', async function (event) {
        const button = $(event.relatedTarget);

        try {
            const response = await fetch('/faculty/all');
            if (response.ok) {
                const faculties = await response.json();

                const tableBody = document.getElementById('facultyTableBody');
                tableBody.innerHTML = ''; // Clear any existing rows

                faculties.forEach(faculty => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                    <td>${faculty.facultyId}</td>
                    <td>${faculty.facultyName}</td>
                    <td>${faculty.dateAdded}</td>
                    <td>${faculty.dateEdited}</td>
                    <td>
                    <button class="btn btn-sm btn-info" onclick="editFaculty(1)">Edit</button>
                    <button class="btn btn-sm btn-danger" data-faculty-id="${faculty.facultyId}">Delete</button>
                    </td>
                `;
                    tableBody.appendChild(row);
                });

                // Attach delete event listeners after populating the table
                attachDeleteListeners();
            } else {
                throw new Error('Failed to fetch faculty data');
            }
        } catch (error) {
            await Swal.fire({
                icon: 'error',
                title: 'Data Fetch Error',
                text: 'There was an error fetching faculty data. Please try again.'
            });
        }
    });



document.getElementById('editFacultyForm').addEventListener('submit', async (event) => {
    event.preventDefault(); // Prevent default form submission

    const facultyId = document.getElementById('editFacultyId').value;
    const facultyName = document.getElementById('editFacultyName').value;

    try {
        const response = await fetch(`/faculties/update/${facultyId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ facultyName })
        });

        if (response.ok) {
            await Swal.fire({
                icon: 'success',
                title: 'Updated',
                text: 'Faculty details updated successfully!'
            });

            // Hide the modal
            const myModalEl = document.getElementById('editFacultyModal');
            const modal = bootstrap.Modal.getInstance(myModalEl);
            modal.hide();

            // Optionally, refresh the table or update the specific row
            location.reload(); // Reload the page or update the table row dynamically
        } else {
            const errorText = await response.text();
            await Swal.fire({
                icon: 'error',
                title: 'Update Error',
                text: errorText || 'There was an error updating the faculty. Please try again.'
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

//FUNCTION IMPORT FACULTIES
function importFaculties() {
    console.log('Importing');
    const facultyFile = document.getElementById('file');

    if (facultyFile.files.length === 0) {
        alert("Select a file");
        return false;
    }
    alert('Faculty imported successfully');
    return true;
}

//FUNCTION LOAD FACULTIES
function loadTableData() {
    fetch('/faculty/all')
        .then(response => response.json())
        .then(data => {
            if (!Array.isArray(data)) {
                console.error("Expected an array but got:", data);
                return;
            }
            const tableBody = document.querySelector('#facultyTable tbody');
            tableBody.innerHTML = '';
            data.forEach(faculty => {
                const row = document.createElement('tr');
                row.innerHTML = `<td>${faculty.facultyId}</td> 
                             <td>${faculty.facultyName}</td>
                             <td>${faculty.dateAdded}</td>
                             <td>${faculty.dateEdited}</td>
                             <td>
                                <button class="btn btn-sm btn-info" onclick="editFaculty(1)">Edit</button>
                                <button class="btn btn-sm btn-danger" th:onclick="'deleteFaculty(' + ${faculty.facultyId} + ')'">Delete</button>
                             </td>`;
                tableBody.appendChild(row);
            });
        })
        .catch(error => console.error('Error loading table data:', error));
}

}



//FUNCTION DELETE FACULTY BASED ON ID
// function deleteFaculty(id) {
//     if (confirm('Are you sure you want to delete this faculty?')) {
//         fetch(`/faculty/delete/${id}`, {
//             method: 'DELETE'
//         })
//             .then(response => {
//                 if (response.ok) {
//                     alert("Faculty deleted successfully");
//                     loadTableData(); // Reload table data
//                 } else {
//                     alert('Failed to delete Faculty');
//                 }
//             });
//     }
// }





