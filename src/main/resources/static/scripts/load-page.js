$(document).ready(function() {
    $('#student-btn').click(function() {
        loadPage('/student');
    });

    $('#facultyAll-btn').click(function() {
        loadPage('/faculty/faculty-all');
    });
    $('#facultyAdd-btn').click(function() {
        loadPage('/faculty/faculty-add');
    });
    $('#facultyEdit-btn').click(function() {
        loadPage('/faculty/faculty-edit');
    });

    // Initial load for the dashboard
    loadPage('/admin-dashboard');
});

function loadPage(url) {
    console.log("Button clicked");
    $.get(url, function(data) {
        $('#main-content').html(data);
        history.pushState(null,'', url);
    });
}

