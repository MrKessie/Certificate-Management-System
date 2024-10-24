console.log("Academic year Script is loaded successfully");

// Show Import Form
// document.getElementById('showImportForm').addEventListener('click', function() {
//     document.getElementById('addForm').style.display = 'none';
//     document.getElementById('importForm').style.display = 'block';
// });

// Cancel Import
// document.getElementById('cancelImport').addEventListener('click', function() {
//     document.getElementById('addForm').style.display = 'block';
//     document.getElementById('importForm').style.display = 'none';
// });

// Populate Edit Form in Modal
// function populateEditForm(id, year) {
//     document.getElementById('editAcademicYearId').value = id;
//     document.getElementById('editAcademicYear').value = year;
// }
$(document).ready(function() {
    console.log('Document is ready');

    // Toggle between forms
    $('#showImportForm').on('click', function() {
        $('#addForm').hide();
        $('#importForm').show();
    });

    $('#cancelImport').on('click', function() {
        $('#importForm').hide();
        $('#addForm').show();
    });

    // Initialize DataTable
    $('#academicYearTable').DataTable();

});


//FUNCTION VALIDATE FORM CONTROLS AMD SUBMIT
// document.getElementById('academicYearForm').addEventListener('submit', async function(event) {
//     event.preventDefault();
//
//     // Get form data
//     const academicYear = document.getElementById('academicYearInput').value;
//     // const facultyName = document.getElementById('facultyName').value;
//
//     // Validate form data
//     if (!academicYear) {
//         await Swal.fire({
//             icon: 'warning',
//             title: 'Required Fields',
//             text: 'All fields are required.'
//         });
//         return;
//     }
//
//     // Prepare data to send
//     const formData = new FormData();
//     formData.append('academicYear', academicYear);
//     // formData.append('facultyName', facultyName);
//
//     try {
//         const response = await fetch('/academic-year/add', {
//             method: 'POST',
//             body: formData
//         });
//
//         if (response.ok) {
//             // Check response status or JSON if needed
//             const result = await response.text(); // or response.json() if server returns JSON
//
//             // Handle response and display SweetAlert
//             await Swal.fire({
//                 icon: 'success',
//                 title: 'Academic Year Added',
//                 text: 'Academic Year has been added successfully!'
//             });
//
//             // Optionally redirect or reset form
//             document.getElementById('academicYearForm').reset(); // Reset form
//             window.location.reload(); // Redirect if needed
//         } else {
//             const errorText = await response.text(); // or response.json() if server returns JSON
//
//             await Swal.fire({
//                 icon: 'error',
//                 title: 'Submission Error',
//                 text: errorText || 'There was an error with the submission. Please try again.'
//             });
//         }
//     } catch (error) {
//         await Swal.fire({
//             icon: 'error',
//             title: 'Submission Error',
//             text: 'There was an error with the submission. Please try again.'
//         });
//     }
// });

// Function to add academic year
async function addAcademicYear() {
// document.getElementById('acadeicYearForm').addEventListener('submit', async function(event) {
    const academicYearInput = document.getElementById('academicYearInput');
    const academicYear = academicYearInput.value.trim();

    if (!academicYear) {
        await Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please enter an academic year.'
        });
        return;
    }

    try {
        // Show loading state
        Swal.fire({
            title: 'Adding Academic Year',
            text: 'Please wait...',
            allowOutsideClick: false,
            showConfirmButton: false,
            willOpen: () => {
                Swal.showLoading();
            }
        });

        // Make API call to add academic year
        const response = await fetch('/academic-year/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `academicYear=${encodeURIComponent(academicYear)}`
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.text();

        await Swal.fire({
            icon: 'success',
            title: 'Academic Year saved successfully.',
            text: data
        });

        // Clear the input field
        academicYearInput.value = '';
        // Optionally, refresh the list of academic years or update the UI
    } catch (error) {
        console.error('Error:', error);
        await Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'An error occurred while adding the academic year. Please try again.'
        });
    }
}


// Event listener for the Save button
document.getElementById('saveAcademicYear').addEventListener('click', addAcademicYear);

// Event listener for the modal close button
document.getElementById('closeModal').addEventListener('click', () => {
    // Close the modal (assuming you're using Bootstrap)
    $('#addAcademicYearModal').modal('hide');
});

// Function to handle Excel import (placeholder)
async function importExcel() {
    await Swal.fire({
        icon: 'info',
        title: 'Import Excel',
        text: 'Excel import functionality is not implemented in this example.'
    });
}

// Event listener for the Import (Excel) button
document.getElementById('importExcel').addEventListener('click', importExcel);


document.addEventListener('DOMContentLoaded', async () => {
    showEditForm()
    console.log('DOM content');
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

function showEditForm() {
    const academicYearTable = document.getElementById('academicYearTableBody');
    const editModal = document.getElementById('editModal');
    const editForm = document.getElementById('editForm');
    const editAcademicYearId = document.getElementById('editAcademicYearId');
    const editAcademicYear = document.getElementById('editAcademicYear');

    academicYearTable.addEventListener('click', function(e) {
        if (e.target.classList.contains('btn-info')) {
            const row = e.target.closest('tr');
            const academicYearId = row.querySelector('.academic-year-id').textContent;
            const academicYear = row.querySelector('td:nth-child(2)').textContent;

            editAcademicYearId.value = academicYearId;
            editAcademicYear.value = academicYear;

            $(editModal).modal('show');
        }
    });
}

function submitEditForm() {
    const formData = {
        facultyId: document.getElementById('editAcademicYearId').value,
        facultyName: document.getElementById('editAcademicYear').value
    };

    fetch('/academic-year/update', {
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
                    title: 'Academic Year Update',
                    text: data.message
                });
                location.reload(); // Reload the page to show updated data
            } else {
                await Swal.fire({
                    icon: 'error',
                    title: 'Updating Error',
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