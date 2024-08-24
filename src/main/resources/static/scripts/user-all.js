$(document).ready(function() {
    $('#userTable').DataTable();
});


document.addEventListener('DOMContentLoaded', async () => {
    console.log("DOM is loaded")
    try {
        const response = await fetch('/user/all');
        if (response.ok) {
            const users = await response.json();

            const tableBody = document.getElementById('userTableBody');
            tableBody.innerHTML = ''; // Clear any existing rows

            users.forEach(user => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${user.userId}</td>
                    <td>${user.fullName}</td>
                    <td>${user.gender}</td>
                    <td>${user.role}</td>
                    <td>${user.dateAdded}</td>
                    <td>${user.dateEdited}</td>
                    <td>
                    <button class="btn btn-sm btn-info" onclick="editFaculty(1)">Edit</button>
                    <button class="btn btn-sm btn-danger" data-user-id="${user.userId}">Delete</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });

            // Attach delete event listeners after populating the table
            attachDeleteListeners();
        } else {
            throw new Error('Failed to fetch User data');
        }
    } catch (error) {
        await Swal.fire({
            icon: 'error',
            title: 'Data Fetch Error',
            text: 'There was an error fetching User data. Please try again.'
        });
    }
});



function attachDeleteListeners() {
    document.querySelectorAll('.btn-danger').forEach(button => {
        button.addEventListener('click', async (event) => {
            const userId = event.target.dataset.userId; // Get the ID from the data attribute

            if (!userId || isNaN(userId)) {
                await Swal.fire({
                    icon: 'error',
                    title: 'Invalid ID',
                    text: 'Invalid User ID. Please try again.'
                });
                return;
            }

            const result = await Swal.fire({
                title: 'Are you sure?',
                text: 'Are you sure you want to delete this User?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes, delete it!',
                cancelButtonText: 'No, cancel!',
                reverseButtons: true
            });

            if (result.isConfirmed) {
                try {
                    const response = await fetch(`/user/delete/${userId}`, {
                        method: 'DELETE',
                    });

                    if (response.ok) {
                        await Swal.fire({
                            icon: 'success',
                            title: 'Deleted',
                            text: 'User has been deleted successfully!'
                        });

                        // Remove the row from the table
                        event.target.closest('tr').remove();
                    } else {
                        const errorText = await response.text();
                        await Swal.fire({
                            icon: 'error',
                            title: 'Deletion Error',
                            text: errorText || 'There was an error deleting the User. Please try again.'
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
                    text: 'User deletion was cancelled.'
                });
            }
        });
    });
}