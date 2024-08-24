$(document).ready(function() {
    $('#programmeTable').DataTable();
});

document.addEventListener("DOMContentLoaded", async function () {
    try {
        const response = await fetch('/programme/all');
        if (response.ok) {
            const programmes = await response.json();

            const tableBody = document.getElementById('programmeTableBody');
            tableBody.innerHTML = ''; // Clear any existing rows

            programmes.forEach(programme => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${programme.programmeId}</td>
                    <td>${programme.programmeName}</td>
                    <td>${programme.faculty.facultyName}</td>
                    <td>${programme.department.departmentName}</td>
                    <td>${programme.dateAdded}</td>
                    <td>${programme.dateEdited}</td>
                    <td>
                    <button class="btn btn-sm btn-info" onclick="editFaculty(1)">Edit</button>
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