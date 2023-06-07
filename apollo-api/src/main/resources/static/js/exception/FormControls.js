let currentTab = 0; // Current tab is set to be the first tab (0)
//showTab(currentTab); // Display the current tab
var current_fs, next_fs, previous_fs; //fieldsets
var left, opacity, scale; //fieldset properties which we will animate
var animating; //flag to prevent quick multi-click glitches

$(".next").click(function(){
    if(animating) return false;
    animating = true;

    current_fs = $(this).parents("fieldset");
    next_fs = $(this).parents("fieldset").next();

    //activate next step on progressbar using the index of next_fs
    $("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");

    //show the next fieldset
    next_fs.show();
    //hide the current fieldset with style
    current_fs.animate({opacity: 0}, {
        step: function(now, mx) {
            //as the opacity of current_fs reduces to 0 - stored in "now"
            //1. scale current_fs down to 80%
            scale = 1 - (1 - now) * 0.2;
            //2. bring next_fs from the right(50%)
            left = (now * 50)+"%";
            //3. increase opacity of next_fs to 1 as it moves in
            opacity = 1 - now;
            current_fs.css({
                'transform': 'scale('+scale+')',
                'position': 'absolute'
            });
            next_fs.css({'left': left, 'opacity': opacity});
        },
        duration: 800,
        complete: function(){
            current_fs.hide();
            animating = false;
        },
        //this comes from the custom easing plugin
        easing: 'easeInOutBack'
    });
});

$(".previous").click(function(){
    if(animating) return false;
    animating = true;

    current_fs = $(this).parents("fieldset");
    previous_fs = $(this).parents("fieldset").prev();

    //de-activate current step on progressbar
    $("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");

    //show the previous fieldset
    previous_fs.show();
    //hide the current fieldset with style
    current_fs.animate({opacity: 0}, {
        step: function(now, mx) {
            //as the opacity of current_fs reduces to 0 - stored in "now"
            //1. scale previous_fs from 80% to 100%
            scale = 0.8 + (1 - now) * 0.2;
            //2. take current_fs to the right(50%) - from 0%
            left = ((1-now) * 50)+"%";
            //3. increase opacity of previous_fs to 1 as it moves in
            opacity = 1 - now;
            current_fs.css({'left': left});
            previous_fs.css({'transform': 'scale('+scale+')', 'opacity': opacity});
        },
        duration: 800,
        complete: function(){
            current_fs.hide();
            animating = false;
        },
        //this comes from the custom easing plugin
        easing: 'easeInOutBack'

    });
});
// function showTab(n) {
//     // This function will display the specified tab of the form...
//     const x = document.getElementsByClassName("tab");
//     x[n].style.display = "block";
//     //... and fix the Previous/Next buttons:
//     if (n === 0) {
//         document.getElementById("prevBtn").style.display = "none";
//     } else {
//         document.getElementById("prevBtn").style.display = "inline";
//     }
//     if (n === (x.length - 1)) {
//         document.getElementById("nextBtn").innerHTML = "SUBMIT";
//         document.getElementById("saveBtn").style.display = "none";
//     } else {
//         document.getElementById("saveBtn").style.display = "inline";
//         document.getElementById("nextBtn").innerHTML = "SAVE AND PROCEED";
//     }
//     //... and run a function that will display the correct step indicator:
//     fixStepIndicator(n)
// }

// function nextPrev(n) {
//     // This function will figure out which tab to display
//     const x = document.getElementsByClassName("tab");
//     // Exit the function if any field in the current tab is invalid:
//     if (n === 1 && !validateForm()) return true;
//     // Hide the current tab:
//     x[currentTab].style.display = "none";
//     // Increase or decrease the current tab by 1:
//     currentTab = currentTab + n;
//     // if you have reached the end of the form...
//     if (currentTab >= x.length) {
//         // ... the form gets submitted:
//         document.getElementById("regForm").submit();
//         return false;
//     }
//     // Otherwise, display the correct tab:
//
// }

//showTab(currentTab);

function validateForm() {
    // This function deals with validation of the form fields
    var x, y, i, valid = true;
    x = document.getElementsByClassName("tab");
    y = x[currentTab].getElementsByTagName("input");
    // A loop that checks every input field in the current tab:
    for (i = 0; i < y.length; i++) {
        // If a field is empty...
        if (y[i].value === "") {
            // add an "invalid" class to the field:
            y[i].className += " invalid";
            // and set the current valid status to false
            valid = true;
        }
    }
    // If the valid status is true, mark the step as finished and valid:
    if (valid) {
        document.getElementsByClassName("step")[currentTab].className += " finish";
    }
    return valid; // return the valid status
}

function fixStepIndicator(n) {
    // This function removes the "active" class of all steps...
    var i, x = document.getElementsByClassName("step");
    for (i = 0; i < x.length; i++) {
        x[i].className = x[i].className.replace(" active", "");
    }
    //... and adds the "active" class on the current step:
    x[n].className += " active";
}

$(document).ready(function () {
    $(".checkboxes").change(function () {
        if ($(this).prop("checked")) {
            $(this).parents(".container-select").find(".box").show();
        } else {
            $(this).parents(".container-select").find(".box").hide();
        }
    });

    $('#modalBtn').on('click',function (event) {
        $('#myModal').hide();
    });

    $("#success-alert").hide();
    $("#rawM").click(function showAlert() {
        $("#success-alert").fadeTo(2000, 500).slideUp(500, function() {
            $("#success-alert").slideUp(500);
        });
    });

    $("#success-machine").hide();
    $("#AddMachine").click(function showAlert() {
        $("#success-machine").fadeTo(2000, 500).slideUp(500, function() {
            $("#success-machine").slideUp(500);
        });
    });

    $("#success-spare").hide();
    $("#sparesBtn").click(function showAlert() {
        $("#success-spare").fadeTo(2000, 500).slideUp(500, function() {
            $("#success-spare").slideUp(500);
        });
    });

    $("#success-save").hide();
    $("#save_exemption").click(function showAlert() {
        $("#success-save").fadeTo(2000, 500).slideUp(500, function() {
            $("#success-save").slideUp(500);
        });
    });

    $("#modalBtn").click(function (){
        $("#myModal").modal("hide");
    })
});
