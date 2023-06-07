var thispage = "";
var addProviderRow = true;
var sharingItem = "1";


function TrackClick(AdID, UserID) {

    //Log the click to an ad 
    var forgeryId = $("#SymptomCheckerToken").val();

    $.ajax({
        url: '/records/AdClicked',
        type: "POST",
        data: { AdID: AdID, UserID: UserID },
        async: true,
        dataType: "json",
        headers: {
            'VerificationToken': forgeryId
        },
        success: function (returnVal) {

        },
        error: function (data) {

        },
    });

}

function AddReadingAlert(ReloadUrl) {

    if ($("#upperLimit").val() == "" && $("#lowerLimit").val() == "")
    {
        showAlert("Please enter a Lower Limit or Upper Limit for the Alert");
    }
    else if ($("#Provider").val() == "" && $("#smsNumber").val() == "")
    {
        showAlert("Please select a Health Provider to alert or specify a Mobile Number to alert via SMS.");
    }
    else
    {
        if ($("#upperLimit").val() == "")
            $("#upperLimit").val(0);

        if ($("#lowerLimit").val() == "")
            $("#lowerLimit").val(0);


        var jhr = $.post("/Records/AddReadingAlert", { RecordCategory: $("#readingType").val(), MaximumThreshold: $("#upperLimit").val(), MinimumThreshold: $("#lowerLimit").val(), Provider: $("#Provider").val(), MobileNumber: $("#smsNumber").val(), UserID: $("#UserID").val() }, function (data, status) {

            if (ReloadUrl != '')
            {
                window.location = ReloadUrl;
                return true;
            }
                
            //Add a record for the provider before adding them against the user
            var providerName = $("#Provider option:selected").html();
            var readingTypeName = $("#readingType option:selected").html();

            var row = '<tr><td>' + readingTypeName + '</td><td>' + $("#upperLimit").val() + '</td><td>' + $("#lowerLimit").val() + '</td><td>' + providerName + '</td><td>' + $("#smsNumber").val() + '</td><td></td></tr>';
            $("#healthalerts").append(row);
            $("#alertsAdd").modal('hide');
        }).error(function () { showAlert("There was a problem adding the health reading alert. Please try again. If the problem persists please contact us at support@medelinked.com") });

    }
}

function AddProvider() {

    if ($("#providername").val() == "") {
        $("#error").html("Please enter the name of the health provider");
        $("#error").show();
        return false;
    }
    
    //Submit the form
    if (thispage != "ProviderPortal")
    {
        $("#provAddBtn").click();
    }
    
}

function ShowProviderDetails(name, addressLine1, addressLine2, postcode, county, country, providerType, providerID, email, website, phone, addRow) {

    $("#providerID_Upd").val(providerID);
    $("#providername_Upd").val(name);
    $("#address1_Upd").val(addressLine1);
    $("#address2_Upd").val(addressLine2);
    $("#postcode_Upd").val(postcode);
    $("#county_Upd").val(county);
    $("#country_Upd").val(country);
    $("#cemail_Upd").val(email);
    $("#website_Upd").val(website);
    $("#phone_Upd").val(phone);
    $("#providertype_Upd").val(providerType);

    $("#providerUpd").modal("show");
}

function UpdateProvider(name, addressLine1, addressLine2, postcode, country, providerType, providerID, email, website, phone) {

    if (providerID != "" && providerID != "00000000-0000-0000-0000-000000000000")
    {
        window.location = "/records/connectwithprovider/" + providerID;
        return false;
    }

    $("#providerID").val(providerID);
    $("#providername").val(name);
    $("#address1").val(addressLine1);
    $("#address2").val(addressLine2);
    $("#postcode").val(postcode);
    $("#Country").val(country);
	$("#cemail").val(email);
	$("#website").val(website);
	$("#phone").val(phone);
    $("#providertype").val(providerType);
    
    //Add the provider
    AddProvider();
}

function UpdateProviderDetails()
{

    if ($("#providername_Upd").val() == "") {
        $("#error").html("Please enter the name of the health provider");
        $("#error").show();
        return false;
    }

    //Submit the form
    $("#provUpdBtn").click();
}

function AssignGP(name, addressLine1, addressLine2, postcode, country, providerType, providerID, email, website, phone) {

    $("#GPProviderID").val(providerID);
    $("#GPName").val(name);
    $("#GPAddr1").val(addressLine1);
    $("#GPAddr2").val(addressLine2 + ", " + postcode);
    $("#GPCountry").val(country);
    $("#GPEmail").val(email);
    $("#GPPhone").val(phone);
   
    $("#GPSearch").modal('hide');
}

function LoadProviders(providerType, name, country) {

    if (providerType == "" && name == "" && country == "")
        return;

    //Show the loading image
    $("#providers").html("<table style='border:none; width: 100%'><tr><td align='center'><img style='height:100px;width:100px' src='/content/images/circle-loading-animation.gif' alt='loading' /></td></tr></table>")

    //alert("/Wizard/GetProviders?providerType=" + providerType + "&name=" + name + "&country=" + country);
    $.get("/Wizard/GetProviders?providerType=" + providerType + "&name=" + name + "&country=" + country, function (data, status) {
        $("#providers").html(data);
    });
   
}

function LoadGPProviders(name, country) {

    if (name == "" && country == "")
        return;

    //Show the loading image
    $("#GPResultsList").html("<table style='border:none; width: 100%'><tr><td align='center'><img style='height:100px;width:100px' src='/content/images/circle-loading-animation.gif' alt='loading' /></td></tr></table>")

    //alert("/Wizard/GetProviders?providerType=" + providerType + "&name=" + name + "&country=" + country);
    $.get("/Wizard/GetProviders?providerType=GP&name=" + name + "&country=" + country + "&callback=AssignGP", function (data, status) {
        $("#GPResultsList").html(data);
    });

}

function LoadNHSServices() {

    var serviceType = $("#serviceType").val();
    var criteria = $("#searchTerm").val();

    if (serviceType == "")
        return;

    //Show the loading image
    $("#nhsservices").html("<table style='border:0; width: 100%'><tr><td align='center'><img style='height:100px;width:100px' src='/content/images/circle-loading-animation.gif' alt='loading' /></td></tr></table>")

    $.get("/Wizard/GetNHSServices?serviceType=" + serviceType + "&criteria=" + criteria, function (data, status) {
        $("#nhsservices").html(data);
    });

}

function LoadNHSGPServices() {

    var serviceType = 'GPPractices';
    var criteria = $("#GPSearchTerm").val();

    if (serviceType == "")
        return;

    //Show the loading image
    $("#NHSGPResultsList").html("<table style='border:0; width: 100%'><tr><td align='center'><img style='height:100px;width:100px' src='/content/images/circle-loading-animation.gif' alt='loading' /></td></tr></table>")

    $.get("/Wizard/GetNHSServices?serviceType=" + serviceType + "&criteria=" + criteria + "&callback=AssignGP", function (data, status) {
        $("#NHSGPResultsList").html(data);
    });

}

function LoadConsultants() {

    var serviceType = $("#csSpeciality").val();
    var criteria = $("#csPostcode").val();

    //Show the loading image
    $("#consultants").html("<table style='border:0; width: 100%'><tr><td align='center'><img style='height:100px;width:100px' src='/content/images/circle-loading-animation.gif' alt='loading' /></td></tr></table>")

    $.get("/Wizard/GetConsultant?serviceType=" + serviceType + "&criteria=" + criteria, function (data, status) {
        $("#consultants").html(data);
    });

}

function SelectProviderToShare(name, email)
{
	//Update the parent form
	$("#SharerEmail" + sharingItem).val(email);
	$("#SharerForename" + sharingItem).val(name);
	
	//Close the window
	$("#providerAdd").modal('hide');
}

function ChooseProviderToShare(itemNumber)
{
	//Update the parent form
	sharingItem = itemNumber;
	
	//Close the window
	$("#providerAdd").modal('show');
}

function ConnectWithingsDevice() {

    if ($("#WithingsUserID").val() == "" || $("#WithingsToken").val() == "") {
        $("#withingsError").html("Please enter your User ID and Token required to connect your account. Use the instructions below to get these from your Withings account.");
        $("#withingsError").show();
        return false;
    }

    $('#withingsForm').submit();

    return true;
}