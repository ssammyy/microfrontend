$(document).ready(function () {

    //Steps controls
    let current_fs, next_fs, previous_fs; //fieldsets
    let opacity;
    let current = 1;
    const steps = $("fieldset").length;

    setProgressBar(current);

    $(".next").click(function () {

        current_fs = $(this).parent();
        next_fs = $(this).parent().next();

        //Add Class Active
        $("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");

        //show the next fieldset
        next_fs.show();
        //hide the current fieldset with style
        current_fs.animate({opacity: 0}, {
            step: function (now) {
                // for making fielset appear animation
                opacity = 1 - now;

                current_fs.css({
                    'display': 'none',
                    'position': 'relative'
                });
                next_fs.css({'opacity': opacity});
            },
            duration: 500
        });
        setProgressBar(++current);
    });

    $(".previous").click(function () {

        current_fs = $(this).parent();
        previous_fs = $(this).parent().prev();

        //Remove class active
        $("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");

        //show the previous fieldset
        previous_fs.show();

        //hide the current fieldset with style
        current_fs.animate({opacity: 0}, {
            step: function (now) {
                // for making fielset appear animation
                opacity = 1 - now;

                current_fs.css({
                    'display': 'none',
                    'position': 'relative'
                });
                previous_fs.css({'opacity': opacity});
            },
            duration: 500
        });
        setProgressBar(--current);
    });

    function setProgressBar(curStep) {
        var percent = parseFloat(100 / steps) * curStep;
        percent = percent.toFixed();
        $(".progress-bar")
            .css("width", percent + "%")
    }

    $(".submit").click(function () {
        return false;
    });//controls end

    //categoryException children controls
    $("#categoryException").change(function () {
        const category = parseInt($(this).children("option:selected").val());
        console.log(category);
        switch (category) {
            case 1:
                $("#sparePartsCat").css("display", "block");
                $("#mainMachinery").css("display", "none");
                $("#spareParts").css("display", "none");
                break;
            case 2:
                $("#sparePartsCat").css("display", "none");
                $("#mainMachinery").css("display", "block");
                $("#spareParts").css("display", "none");
                break;

            case 3:
                $("#sparePartsCat").css("display", "none");
                $("#mainMachinery").css("display", "none");
                $("#spareParts").css("display", "block");
                break
        }

    });
});