function showSpinnerOnLoad(document){
        let state = document.readyState
        if (state == 'interactive') {
            document.getElementById('contents').style.visibility="hidden";
        } else if (state == 'complete') {
            setTimeout(function(){
                document.getElementById('interactive');
                document.getElementById('spinnerContainer').style.visibility="hidden";
                document.getElementById('contents').style.visibility="visible";
            },1000);
        }
}

function showSpinner(document){
        document.getElementById('spinnerContainer').style.visibility="visible";
        document.getElementById('contents').style.visibility="hidden";
}
