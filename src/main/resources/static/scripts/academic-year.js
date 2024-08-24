console.log("Academic year Script is loaded successfully");

// Show Import Form
document.getElementById('showImportForm').addEventListener('click', function() {
    document.getElementById('addForm').style.display = 'none';
    document.getElementById('importForm').style.display = 'block';
});

// Cancel Import
document.getElementById('cancelImport').addEventListener('click', function() {
    document.getElementById('addForm').style.display = 'block';
    document.getElementById('importForm').style.display = 'none';
});

// Populate Edit Form in Modal
// function populateEditForm(id, year) {
//     document.getElementById('editAcademicYearId').value = id;
//     document.getElementById('editAcademicYear').value = year;
// }
$(document).ready(function() {
    console.log('Document is ready');
    // Initialize DataTable
    $('#academicYearTable').DataTable();

    // Toggle between forms
    $('#showImportForm').on('click', function() {
        $('#addForm').hide();
        $('#importForm').show();
    });

    $('#cancelImport').on('click', function() {
        $('#importForm').hide();
        $('#addForm').show();
    });

});


//FUNCTION VALIDATE FORM CONTROLS AMD SUBMIT
document.getElementById('academicYearForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    // Get form data
    const academicYear = document.getElementById('academicYearInput').value;
    // const facultyName = document.getElementById('facultyName').value;

    // Validate form data
    if (!academicYear) {
        await Swal.fire({
            icon: 'warning',
            title: 'Required Fields',
            text: 'All fields are required.'
        });
        return;
    }

    // Prepare data to send
    const formData = new FormData();
    formData.append('academicYear', academicYear);
    // formData.append('facultyName', facultyName);

    try {
        const response = await fetch('/academic-year/add', {
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
                text: 'Academic Year has been added successfully!'
            });

            // Optionally redirect or reset form
            document.getElementById('academicYearForm').reset(); // Reset form
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
    console.log('DOM content');
    try {
        const response = await fetch('/academic-year/all');
        if (response.ok) {
            const faculties = await response.json();

            const tableBody = document.getElementById('academicYearTableBody');
            tableBody.innerHTML = ''; // Clear any existing rows

            faculties.forEach(academicYear => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${academicYear.id}</td>
                    <td>${academicYear.academicYear}</td>
                    <td>${academicYear.dateAdded}</td>
                    <td>${academicYear.dateEdited}</td>
                    <td>
                    <button class="btn btn-sm btn-info" data-toggle="modal" data-target="#editModal" onclick="editAcademicYear(${academicYear.id}, '${academicYear.academicYear}')">Edit</button>
                    <button class="btn btn-sm btn-danger" data-academic-year-id="${academicYear.id}">Delete</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });

            // Attach delete event listeners after populating the table
            attachDeleteListeners();
        } else {
            throw new Error('Failed to fetch academic year data');
        }
    } catch (error) {
        await Swal.fire({
            icon: 'error',
            title: 'Data Fetch Error',
            text: 'There was an error fetching academic year data. Please try again.'
        });
    }
});


function attachDeleteListeners() {
    document.querySelectorAll('.btn-danger').forEach(button => {
        button.addEventListener('click', async (event) => {
            const academicYearId = event.target.dataset.id; // Get the ID from the data attribute

            if (!academicYearId) {
                await Swal.fire({
                    icon: 'error',
                    title: 'Invalid ID',
                    text: 'Invalid Academic year ID. Please try again.'
                });
                return;
            }

            const result = await Swal.fire({
                title: 'Are you sure?',
                text: 'Are you sure you want to delete this academic year?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes, delete it!',
                cancelButtonText: 'No, cancel!',
                reverseButtons: true
            });

            if (result.isConfirmed) {
                try {
                    const response = await fetch(`/academic-year/delete/${academicYearId}`, {
                        method: 'DELETE',
                    });

                    if (response.ok) {
                        await Swal.fire({
                            icon: 'success',
                            title: 'Deleted',
                            text: 'Academic Year has been deleted successfully!'
                        });

                        // Remove the row from the table
                        event.target.closest('tr').remove();
                    } else {
                        const errorText = await response.text();
                        await Swal.fire({
                            icon: 'error',
                            title: 'Deletion Error',
                            text: errorText || 'There was an error deleting the Academic Year. Please try again.'
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
                    text: 'Academic Year deletion was cancelled.'
                });
            }
        });
    });
}


//FUNCTION TO IMPORT ACADEMIC YEARS
function importAcademicYears() {
    console.log('Importing');
    const academicYearFile = document.getElementById('file');
    const form = document.getElementById('academicYearImportForm');

    if (academicYearFile.files.length === 0) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please select a file'
        });
        return false;
    }

    academicYearFile.addEventListener('change', function(event) {
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

    fetch('/academic-year/import-academic-years', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.addedCount > 0) {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: `${data.addedCount} academic years have been added successfully.`
                });
            }

            if (data.notAddedAcademicYears && Object.keys(data.notAddedAcademicYears).length > 0) {
                let message = 'The following academic years were not added:\n\n';
                for (let [academicYear, reason] of Object.entries(data.notAddedAcademicYears)) {
                    message += `Academic Year ${academicYear}: ${reason}\n`;
                }
                Swal.fire({
                    icon: 'warning',
                    title: 'Warning',
                    text: message
                });
            }

            if ((!data.addedCount || data.addedCount === 0) &&
                (!data.notAddedAcademicYears || Object.keys(data.notAddedAcademicYears).length === 0)) {
                Swal.fire({
                    icon: 'info',
                    title: 'Information',
                    text: 'No academic years were added. The file might be empty.'
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'An error occurred while importing academic years.'
            });
        });

    return false; // Prevent form from submitting traditionally
}



function editAcademicYear(id, year) {
    // Set the values in the modal
    const academicYearId = document.getElementById('editAcademicYearId').value = id;
    const academicYear = document.getElementById('editAcademicYear').value = year;

    console.log("Academic Year ID:", academicYearId);
    console.log("Academic Year:", academicYear);

    // Show the modal
    $('#editModal').modal('show');
}


async function submitEditForm() {
    const academicYearId = document.getElementById('editAcademicYearId').value;
    const academicYear = document.getElementById('editAcademicYear').value;

    const data = {
        // academicYearId: academicYearId,
        academicYear: academicYear
    };

    try {
        const response = await fetch('/academic-year/update/${academicYearId}', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            $('#editModal').modal('hide');
            await Swal.fire({
                icon: 'success',
                title: 'Updated!',
                text: 'Academic Year has been updated.'
            });

            // Optionally, refresh the table to reflect the changes
            document.location.reload();
        } else {
            throw new Error('Failed to update academic year');
        }
    } catch (error) {
        await Swal.fire({
            icon: 'error',
            title: 'Update Error',
            text: 'There was an error updating the academic year. Please try again.'
        });
    }
}