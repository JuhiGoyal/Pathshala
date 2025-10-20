// notebook-editor.js
// prevents copy/paste and gathers editor text into hidden textarea before submit
document.addEventListener('DOMContentLoaded', function() {
    var editor = document.getElementById('editor');
    if (!editor) return;
    // disable copy, cut (optional - still allow user to type)
    editor.addEventListener('copy', function(e){ e.preventDefault(); });
    editor.addEventListener('cut', function(e){ e.preventDefault(); });
    editor.addEventListener('paste', function(e){ e.preventDefault(); });
    // disable right click menu inside editor (optional)
    editor.addEventListener('contextmenu', function(e){ e.preventDefault(); });
    // simple keyboard block: prevent Ctrl+V
    document.addEventListener('keydown', function(e){
        if (e.ctrlKey && (e.key === 'v' || e.key === 'V')) {
            e.preventDefault();
        }
    });
});

function submitEditor() {
    var editor = document.getElementById('editor');
    var answerText = document.getElementById('answerText');
    var form = document.getElementById('submissionForm');
    if (!editor || !answerText || !form) return;
    // Collect plain text from editor (no HTML)
    var text = editor.innerText || editor.textContent || "";
    answerText.value = text.trim();
    if (answerText.value.length === 0) {
        alert("Please write your answer before submitting.");
        return;
    }
    // submit the form (standard submit)
    form.submit();
}
