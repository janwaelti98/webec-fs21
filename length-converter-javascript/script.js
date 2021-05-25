function convert() {
    let feetField = document.getElementById('feet')
    let resultSpan = document.getElementById('result')

    let feet = feetField.value
    let cm = feet * 2.56
    let cmPart = Math.floor(cm)
    let mmPart = Math.round(cm * 10 % 10)
    
    if (cm !== 0) {
        resultSpan.textContent = cmPart + ' cm, ' + mmPart + ' mm'
    } else {
        resultSpan.textContent = ''
    }
}
