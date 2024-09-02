console.log("Dashboard script loaded successfully")

// Hide preloader when the page is fully loaded


function toggleSidebar() {
    const sidebar = document.getElementById('sidebar-wrapper');
    const mainContent = document.getElementById('main-content');

    // Toggle the 'collapsed' class on both sidebar and main content
    sidebar.classList.toggle('collapsed');
    mainContent.classList.toggle('collapsed');
}

document.getElementById('menu-toggle').addEventListener('click', toggleSidebar);


document.addEventListener('DOMContentLoaded', function() {
    console.log("Loading user profile...")

    fetch('/info/user-info')
        .then(response => response.json())
        .then(data => {
            document.getElementById('userName').textContent = data.fullName;
            document.getElementById('role').textContent = data.role;
            document.getElementById('currentUserId').textContent = data.userId;
        })
        .catch(error => {
            console.error('Error fetching user info:', error);
        });
});

document.getElementById('userActivityReportButton').addEventListener('click', function() {
    fetch('/reports/user-activity/pdf', {
        method: 'GET',
        credentials: 'include'
    })
        .then(response => {
            if (response.ok) {
                return response.blob();
            } else if (response.status === 403) {
                throw new Error('You do not have permission to view this report.');
            } else {
                throw new Error('An error occurred while generating the report.');
            }
        })
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.style.display = 'none';
            a.href = url;
            a.download = 'user_activity_report.pdf';
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
        })
        .catch(error => {
            alert(error.message);
        });
});



const userId = document.getElementById('currentUserId').value;
console.log("Current user ID: " + userId);
document.getElementById('tableStatsReportButton').addEventListener('click', function() {
    window.open('/reports/table-stats/pdf', '_blank');
});

function getCurrentUserId() {
    return document.getElementById('currentUserId').value;
}

