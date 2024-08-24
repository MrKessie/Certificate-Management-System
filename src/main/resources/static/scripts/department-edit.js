
$(document).ready(function() {
    $('#departmentTable').DataTable();
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