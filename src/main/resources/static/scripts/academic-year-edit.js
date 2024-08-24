console.log("Edit script loaded");

$(document).ready(function() {
    // Initialize DataTable
    $('#academicYearTable').DataTable();
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

