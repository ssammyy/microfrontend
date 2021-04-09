// NOTICE!! DO NOT USE ANY OF THIS JAVASCRIPT
// IT'S ALL JUST JUNK FOR OUR DOCS!
// ++++++++++++++++++++++++++++++++++++++++++

var confirmed = false;

!function ($) {

    $(function () {

        // Popover 
        $('#Password').hover(function () {
            $(this).popover('show')
        });

        // Popover 
        $('#Password').focus(function () {
            $(this).popover('show')
        });

        // Popover 
        $('#Password').blur(function () {
            $(this).popover('hide')
        });

        // Popover 
        $('#ZDID').hover(function () {
            $(this).popover('show')
        });

        // Popover 
        $('#ZDID').focus(function () {
            $(this).popover('show')
        });

        // Popover 
        $('#ZaptagID').blur(function () {
            $(this).popover('hide')
        });

        // Popover 
        $('#ZaptagID').hover(function () {
            $(this).popover('show')
        });

        // Popover 
        $('#ZaptagID').focus(function () {
            $(this).popover('show')
        });

        // Popover 
        $('#ZaptagID').blur(function () {
            $(this).popover('hide')
        });

        // Add the datepicker
        $(".input-append.date").datepicker();

        if ($.browser.msie) {
            $('.modal').removeClass('fade');    
        }
    })

} (window.jQuery)

function showAlert(message) {
    
    //Add the message
    $("#alertBody").html(message);

    //Show the alert
    $('#alertModal').modal('show');
}

function showConfirm(message, action) {

    bootbox.confirm(message, function (confirmed) {

        if (confirmed) {
            window.location = action;
        }
    });

}

function showHealthCategories() {
    $("#healthCats").toggle()
}

function showWellnessCategories() {
    $("#wellnessCats").toggle()
}

function ShowTab(tabname) {

    var categoryName = tabname;
    var isWellness = false;

    if (categoryName == "BG")
    {
        categoryName = "Blood Glucose Readings";
        isWellness = true;
    }
    else if (categoryName == "BP")
    {
        categoryName = "Blood Pressure Readings";
        isWellness = true;
    }
    else if (categoryName == "Waist")
    {
        categoryName = "Waist Measurements";
        isWellness = true;
    }
    else if (categoryName == "Heart")
    {
        categoryName = "Heart Rate Readings";
        isWellness = true;
    }
    else if (categoryName == "Sleep") {
        categoryName = "Sleep Tracking";
        isWellness = true;
    }
    else if (categoryName == "Intake") {
        categoryName = "Calorie Intake";
        isWellness = true;
    }
    else if (categoryName == "Travel")
        categoryName = "Travel Insurance Records";
    else if (categoryName == "Insurance")
        categoryName = "Insurance Documents";
    else if (categoryName == "InsuranceClaims")
        categoryName = "Insurance Claims";
    else if (categoryName == "Weight")
    {
        categoryName = "Weight Measurements";
        isWellness = true;
    }
    else if (categoryName == "Cholesterol")
    {
        categoryName = "Cholesterol Readings";
        isWellness = true;
    }
    else if (categoryName == "Exercise")
    {
        categoryName = "Exercise Sessions";
        isWellness = true;
    }
    else if (categoryName == "Insurance")
        categoryName = "Insurance Documents";
    else if (categoryName == "ERecords")
        categoryName = "Emergency Records";
    else if (categoryName == "EContacts")
        categoryName = "Emergency Contacts";
    else if (categoryName == "CarePlan")
        categoryName = "Care Plan";
    else if (categoryName == "FamilyHistory")
        categoryName = "Family History";
    else if (categoryName == "Inpatient")
        categoryName = "Inpatient Admission";
    else if (categoryName == "Outpatient")
        categoryName = "Outpatient Attendance";
    else if (categoryName == "Oxygen")
    {
        categoryName = "Oxygen Saturation";
        isWellness = true;
    }
    else
        categoryName = categoryName + " Records";

    if (isWellness)
    {
        $("#AllHWRecords").hide();
        $("#AllWRecords").show();
        $("#providerList").show();
        $("#EContacts").hide();
        $("#DateRecords").hide();
        $("#PersonalRecords").hide();
        $("#ERecords").hide();
        $("#catHeaderW").html(categoryName);
        $("#myTimeline").hide();
        $("#mycharts").hide();
        $(".ListView").hide();
        $(".ListView." + tabname).show();

        //Call the function
        eval("drawWellness" + tabname + "();");
    }
    else
    {
        switch (tabname) {

            case "EContacts":
                $("#providerList").hide();
                $("#AllHWRecords").hide();
                $("#AllWRecords").hide();
                $("#EContacts").show();
                $("#DateRecords").hide();
                $("#PersonalRecords").hide();
                $("#myTimeline").hide();
                $("#mycharts").hide();
                break;

            case "PersonalRecords":
                $("#providerList").hide();
                $("#AllHWRecords").hide();
                $("#AllWRecords").hide();
                $("#EContacts").hide();
                $("#DateRecords").hide();
                $("#PersonalRecords").show();
                $("#myTimeline").hide();
                $("#mycharts").hide();
                break;

            case "DateRecords":
                $("#providerList").show();
                $("#AllHWRecords").hide();
                $("#AllWRecords").hide();
                $("#EContacts").hide();
                $("#DateRecords").show();
                $("#PersonalRecords").hide();
                $("#myTimeline").hide();
                $("#mycharts").hide();
                break;

            case "ERecords":
                $("#AllHWRecords").show();
                $("#AllWRecords").hide();
                $("#providerList").hide();
                $("#EContacts").hide();
                $("#DateRecords").hide();
                $("#PersonalRecords").hide();
                $("#catHeader").html(categoryName);
                $(".ListView").hide();
                $("#myTimeline").hide();
                $(".ERecord").show();
                $("#mycharts").hide();
                break;

            default:
                $("#AllHWRecords").show();
                $("#providerList").show();
                $("#AllWRecords").hide();
                $("#EContacts").hide();
                $("#DateRecords").hide();
                $("#PersonalRecords").hide();
                $("#ERecords").hide();
                $("#catHeader").html(categoryName);
                $("#myTimeline").hide();
                $("#mycharts").hide();
                $(".ListView").hide();
                $(".ListView." + tabname).show();
                break;
        }
    }
    
}