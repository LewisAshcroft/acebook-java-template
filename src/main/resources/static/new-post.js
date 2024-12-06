document.addEventListener("DOMContentLoaded", () => {
    const inputFile = document.getElementById("input-file");
    const preview = document.getElementById("label-file")

    inputFile.onchange = event => {
        const [file] = inputFile.files;
        if (file) {
            console.log(URL.createObjectURL(file));
            preview.style.backgroundImage = 'url(' + URL.createObjectURL(file) + ')'
        }
    };
});