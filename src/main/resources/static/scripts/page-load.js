// function loadPage(page) {
//     // Construct the new URL
//     const newUrl = `${page}`;
//
//     // Use the History API to change the URL
//     // window.history.pushState({ page }, '', newUrl);
//
//     //Load content dynamically
//     fetch(page)
//         .then(response => response.text())
//         .then(html => {
//             document.getElementById("mainContent").innerHTML = html;
//             initializeFacultyPage();
//             //window.history.pushState({}, '', page);
//             window.history.pushState({ page }, '', newUrl);
//         });
// }
//
// // Handle back/forward navigation
// window.addEventListener('popstate', (event) => {
//     if (event.state && event.state.page) {
//         loadPage(event.state.page);
//     }
// });

// function loadPage(page) {
//     // Construct the new URL
//     const newUrl = window.location.origin + page;
//
//     // Use the History API to change the URL
//     window.history.pushState({page}, '', newUrl);
//
//     // Load content dynamically
//     fetch(newUrl)
//         .then(response => response.text())
//         .then(html => {
//             document.getElementById("mainContent").innerHTML = html;
//             // Initialize any JavaScript code specific to the loaded page
//             //initializeFacultyPage();
//         })
// }

// function loadPage(pageName) {
//     const mainContent = document.getElementById('main-content');
//
//     fetch(`/${pageName}`)
//         .then(response => response.text())
//         .then(data => {
//             mainContent.innerHTML = data;
//             // Call the API to fetch the data for the page
//             // if (page === 'faculty') {
//             //     loadFacultyData();
//             // }
//         })
//         .catch(error => console.error('Error loading page:', error));
// }


// document.getElementById('facultyButton').addEventListener('click', function() {
//     loadPage('/faculty');
// });
//
// document.getElementById('departmentButton').addEventListener('click', function() {
//     loadPage('/department');
// });
//
// document.getElementById('studentButton').addEventListener('click', function() {
//     loadPage('/student');
// });
//
// function loadPage(url) {
//     fetch(url)
//         .then(response => response.text())
//         .then(data => {
//             document.getElementById('mainContent').innerHTML = data;
//         })
//         .catch(error => console.error('Error loading page:', error));
// }


// // Add event listeners to the side nav buttons
// document.getElementById('facultyButton').addEventListener('click', function() {
//     loadPage('faculty');
// });
//
// document.getElementById('departmentButton').addEventListener('click', function() {
//     loadPage('department');
// });
//
// document.getElementById('studentButton').addEventListener('click', function() {
//     loadPage('student');
// });
//
// // Function to load the page into the main content area
// function loadPage(pageName) {
//     var xhr = new XMLHttpRequest();
//     xhr.open('GET', '/' + pageName, true);
//     xhr.onload = function() {
//         if (xhr.status === 200) {
//             document.getElementById('mainContent').innerHTML = xhr.responseText;
//         } else {
//             console.error('Error loading page: ' + xhr.status);
//         }
//     };
//     xhr.onerror = function() {
//         console.error('Request failed');
//     };
//     xhr.send();
// }


const facultyBtn = document.getElementById('facultyButton');
const departmentBtn = document.getElementById('departmentButton');
const studentBtn = document.getElementById('studentButton');
const academicYearBtn = document.getElementById('yearButton');
const mainContent = document.getElementById('mainContent');

facultyBtn.addEventListener('click', () => {
    fetch('/faculty')
        .then(response => response.text())
        .then(html => {
            mainContent.innerHTML = html;
        })
        .catch(error => console.error(error));
});

departmentBtn.addEventListener('click', () => {
    fetch('/department')
        .then(response => response.text())
        .then(html => {
            mainContent.innerHTML = html;
        })
        .catch(error => console.error(error));
});

academicYearBtn.addEventListener('click', () => {
    fetch('/academic-year')
        .then(response => response.text())
        .then(html => {
            mainContent.innerHTML = html;
        })
        .catch(error => console.error(error));
});

studentBtn.addEventListener('click', () => {
    fetch('/student')
        .then(response => response.text())
        .then(html => {
            mainContent.innerHTML = html;
        })
        .catch(error => console.error(error));
});


