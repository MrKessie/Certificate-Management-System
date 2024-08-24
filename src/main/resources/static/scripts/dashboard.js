console.log("Dashboard script loaded successfully")

function toggleSidebar() {
    var sidebar = document.getElementById('sidebar-wrapper');
    var mainContent = document.getElementById('main-content');

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
        })
        .catch(error => {
            console.error('Error fetching user info:', error);
        });
});

// function loadPage(url) {
//     // console.log('Add btn clicked')
//     $.get(url, function(data) {
//         $('#main-content').html(data);
//         history.pushState(null,'', url);
//     });
// }