$(document).ready(function() {
    $('#userActivityTable').DataTable();
});



document.addEventListener('DOMContentLoaded', async () => {
    try {
        const response = await fetch('/user-activity/all');
        if (response.ok) {
            const userActivity = await response.json();

            const tableBody = document.getElementById('userActivityTable');
            tableBody.innerHTML = ''; // Clear any existing rows

            userActivity.forEach(activity => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${activity.id}</td>
                    <td>${activity.user.userId}</td>
                    <td>${activity.action}</td>
                    <td>${activity.details}</td>
                    <td>${activity.timestamp}</td>
                `;
                tableBody.appendChild(row);
            });

            // Attach delete event listeners after populating the table
            attachDeleteListeners();
        } else {
            throw new Error('Failed to fetch User Activity data');
        }
    } catch (error) {
        await Swal.fire({
            icon: 'error',
            title: 'Data Fetch Error',
            text: 'There was an error fetching User Activity data. Please try again.'
        });
    }
});


document.getElementById('exportForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const fromDate = document.getElementById('fromDate').value;
    const toDate = document.getElementById('toDate').value;

    // Perform form validation
    if (!fromDate || !toDate) {
        alert("Please select a valid date range.");
        return;
    }

    // Open the PDF in a new tab
    const url = `/user-activity/export-user-activities?fromDate=${fromDate}&toDate=${toDate}`;
    window.open(url, '_blank');
});
