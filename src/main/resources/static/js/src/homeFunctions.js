function addLayer(x) {
    console.log(x)
    var template = $('div:first').clone(); // Clone the first div
    template.find('input').val(''); // Clear the input value
    template.find('label').attr('for', ''); // Clear the "for" attribute of the label
    template.prependTo('#layerForm'); // Append the cloned div to the form
}