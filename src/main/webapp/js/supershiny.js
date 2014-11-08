//wysiwyg summernote editor funcs
function turnOnEditor() {
    $('#summernote').summernote();
    $('.note-editable').html($('#editor-input').val());
    if ($('#editor-input').val() === null) {
        $('#preview').hide();
    } else {
        $('#preview').show();
        $('#preview').html($('.note-editable').html());
    }
    
}

function showContent() {
    $('#editor-input').val($('.note-editable').html());
    if ($('#editor-input').val() === null) {
        $('#preview').hide();
    } else {
        $('#preview').show();
        $('#preview').html($('.note-editable').html());
    }
}

//modal controls
function closeModal() {
    $('#bsModal').modal('hide');
}

function showModal() {
    $('#bsModal').modal('show');
}

//left/right keys triggers for images listview
$("body").keydown(function (e) {
    if (e.keyCode === 37) { // left
        $("#prev").click();
    }
    else if (e.keyCode === 39) { // right
        $("#next").click();
    }
});

// Activates Tooltips for Social Links
$('.tooltip-social').tooltip({
  selector: "a[data-toggle=tooltip]"
})