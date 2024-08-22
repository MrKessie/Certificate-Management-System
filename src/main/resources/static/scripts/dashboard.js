console.log("Dashboard script loaded successfully")

function toggleSidebar() {
    var sidebar = document.getElementById('sidebar-wrapper');
    var mainContent = document.getElementById('main-content');

    // Toggle the 'collapsed' class on both sidebar and main content
    sidebar.classList.toggle('collapsed');
    mainContent.classList.toggle('collapsed');
}

document.getElementById('menu-toggle').addEventListener('click', toggleSidebar);


// function loadPage(url) {
//     // console.log('Add btn clicked')
//     $.get(url, function(data) {
//         $('#main-content').html(data);
//         history.pushState(null,'', url);
//     });
// }