$("#menu-toggle").click(function(e) {
    e.preventDefault();
    $("#wrapper").toggleClass("toggled");
});

// function toggleSidebar() {
//     var sidebar = document.getElementById("sidebar-wrapper");
//     var mainContent = document.getElementById("sidebar-content");
//     sidebar.classList.toggle("collapsed");
//     mainContent.classList.toggle("collapsed");
// }

function loadPage(url) {
    // console.log('Add btn clicked')
    $.get(url, function(data) {
        $('#main-content').html(data);
        history.pushState(null,'', url);
    });
}