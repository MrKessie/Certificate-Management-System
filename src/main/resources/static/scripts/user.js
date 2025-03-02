console.log("Register script is loaded successfully");
$(document).ready(function() {
    $('#userTable').DataTable();
});

document.getElementById('registerForm').addEventListener('submit', async function(event) {
    event.preventDefault();
    console.log("Register form reached");

    // Get form data
    const userId = document.getElementById('userId').value;
    const userName = document.getElementById('fullName').value;
    const gender = document.getElementById('gender').value;
    const role = document.getElementById('role').value;
    const password = document.getElementById('password').value;
    const username = document.getElementById('usernames').value;

    // Validate form data
    if (!userId || !userName || !gender || !role || !password || !username) {
        await Swal.fire({
            icon: 'warning',
            title: 'Required Fields',
            text: 'All fields are required.'
        });
        return;
    }

    if (isNaN(userId)) {
        await Swal.fire({
            icon: 'warning',
            title: 'Invalid User ID',
            text: 'Enter a valid User ID.'
        });
        return;
    }

    if (!/^[a-zA-Z\s]+$/.test(userName)) {
        await Swal.fire({
            icon: 'warning',
            title: 'Invalid User Name',
            text: 'Enter a valid User Name'
        });
        return;
    }

    // Prepare data to send
    const formData = new FormData();
    formData.append('userId', userId);
    formData.append('fullName', userName);
    formData.append('gender', gender);
    formData.append('role', role);
    formData.append('password', password);
    formData.append('username', username);

    try {
        Swal.fire({
            title: 'Processing...',
            text: 'Please wait while we add the User.',
            allowOutsideClick: false,
            allowEscapeKey: false,
            showConfirmButton: false,
            willOpen: () => {
                Swal.showLoading();
            }
        });

        const response = await fetch('/certificate-management-system/register/user', {
            method: 'POST',
            body: formData
        });
        console.log("After fetching");

        if (response.ok) {
            // Check response status or JSON if needed
            const result = await response.text(); // or response.json() if server returns JSON

            Swal.close();

            // Handle response and display SweetAlert
            await Swal.fire({
                icon: 'success',
                title: 'User Added',
                text: 'User has been added successfully!'
            });

            // Optionally redirect or reset form
            document.getElementById('registerForm').reset(); // Reset form
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
                    const response = await fetch(`/certificate-management-system/user/delete/${userId}`, {
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


document.addEventListener('DOMContentLoaded', async () => {
    console.log("DOM is loaded")
    try {
        const response = await fetch('/certificate-management-system/user/all');
        if (response.ok) {
            const users = await response.json();

            const tableBody = document.getElementById('userTableBody');
            tableBody.innerHTML = ''; // Clear any existing rows

            users.forEach(user => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${user.userId}</td>
                    <td>${user.fullName}</td>
                    <td>${user.username}</td>
                    <td>${user.gender}</td>
                    <td>${user.role}</td>
                    <td>${user.dateAdded}</td>
                    <td>${user.dateEdited}</td>
                    <td>
                    <button class="btn btn-sm btn-info"">Edit</button>
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


