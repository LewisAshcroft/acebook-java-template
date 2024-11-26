function myFunction() {
  document.getElementById("myDropdown").classList.toggle("show");
}

window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {
    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        openDropdown.classList.remove('show');
      }
    }
  }
}

function searchFunction() {
    const elements = document.getElementsByClassName("Post");
    let search = document.getElementById("searchbar").value;
    console.log("This is what you searched: " + search);
    for (let i = 0; i < elements.length; i++) {
        elements[i].classList.toggle("hide");
    }
    for (let i = 0; i < elements.length; i++) {
        if (i == search && window.getComputedStyle().display === "none") {
            elements[i].classList.toggle("hide");
        }
    }
    if (search == "") {
        for (let i = 0; i < elements.length; i++) {
            if (window.getComputedStyle().display === "none"){
                elements[i].classList.toggle("hide");
            }
        }
    }
}