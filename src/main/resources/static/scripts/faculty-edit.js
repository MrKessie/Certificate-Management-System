$(document).ready(function() {
    $('#facultyTable').DataTable();
});


document.addEventListener('DOMContentLoaded', async () => {
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