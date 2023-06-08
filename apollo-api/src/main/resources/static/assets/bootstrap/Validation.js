/**
 * DHTML phone number validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */

// Declaring required variables
var digits = "0123456789";
// non-digit characters which are allowed in phone numbers
var phoneNumberDelimiters = "()- ";
// characters which are allowed in international phone numbers
// (a leading + is OK)
var validWorldPhoneChars = phoneNumberDelimiters + "+";
// Minimum no of digits in an international phone no.
var minDigitsInIPhoneNumber = 10;
var maxDigitsInIPhoneNumber = 15;

function isInteger(s)
{   var i;
    for (i = 0; i < s.length; i++)
    {   
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag)
{   var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++)
    {   
        // Check that current character isn't whitespace.
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}

function checkInternationalPhone(strPhone){
s=stripCharsInBag(strPhone,validWorldPhoneChars);
return (isInteger(s) && s.length >= minDigitsInIPhoneNumber && s.length <= maxDigitsInIPhoneNumber);
}

function validateBirthday(dtControl)
{
    var input = dtControl
    var validformat=/^\d{1,2}\/\d{1,2}\/\d{4}$/ //Basic check for format validity
    var returnval=false
    if (!validformat.test(input.value))
    showAlert('You have entered your Date of Birth in an invalid format. Please enter the date in DD/MM/YYYY format (for example 16/03/1982).')
    else{ //Detailed check for valid date ranges
    var dayfield=input.value.split('/')[0]
    var monthfield=input.value.split('/')[1]
    var yearfield=input.value.split('/')[2]
   
    var dayobj = new Date(yearfield, monthfield-1, dayfield)
    
    // check birthday
    var mm = dayfield;
    var bday = monthfield;
    var byear = yearfield;
    thedate = new Date();
    mm2 = thedate.getMonth() + 1;
    dd2 = thedate.getDate();
    yy2 = thedate.getYear();
    if (yy2 < 200) yy2 += 1900
    yourage = yy2 - byear;
    if (mm2 < mm) yourage--;
    if ((mm2 == mm) && (dd2 < bday)) yourage--;
    
    // check date
    if ((dayobj.getMonth()+1!=monthfield)||(dayobj.getDate()!=dayfield)||(dayobj.getFullYear()!=yearfield)) {
        showAlert('Invalid Day, Month, or Year range detected. Please enter a valid date in the format DD/MM/YYYY.')
    } else if (yourage < 0) {
        showAlert('This date must be in the past. Please correct.');
    } else {
        returnval=true
    }
    }
    if (returnval==false) input.focus()
    return returnval
}


function validatePastDate(dtControl)
{
    var input = dtControl
    var validformat=/^\d{1,2}\/\d{1,2}\/\d{4}$/ //Basic check for format validity
    var returnval=false
    if (!validformat.test(input.value))
    showAlert('Invalid Date Format. Please enter a valid date in the format DD/MM/YYYY.')
    else{ //Detailed check for valid date ranges
    var dayfield=input.value.split('/')[0]
    var monthfield=input.value.split('/')[1]
    var yearfield=input.value.split('/')[2]
   
    var dayobj = new Date(yearfield, monthfield-1, dayfield)

    thedate = new Date();
    
    // check date
    if ((dayobj.getMonth()+1!=monthfield)||(dayobj.getDate()!=dayfield)||(dayobj.getFullYear()!=yearfield)) {
        showAlert('Invalid Day, Month, or Year range detected. Please enter a valid date in the format DD/MM/YYYY.')
    }else if (thedate <= dayobj) {
        showAlert('This date must be in the past. Please correct.');
    } else {
        returnval=true
    }
    }
    if (returnval==false) input.focus()
    return returnval
}




/**
* Validation code for various things
*/

function ValidateType(typename, field, fieldname) {

    if (typename == "telephone") {
        var Phone=field;

        if (!Phone.value == "") {
            if (checkInternationalPhone(Phone.value) == false) {
                showAlert("Please Enter a valid " + fieldname)
                Phone.value = ""
                Phone.focus()
                return false
            }
        }

        return true
    } 
    
    if (typename == "birthday") {
        if (validateBirthday(field) == false) {
	        return false        
        }
        return true;
    }

    if (typename == "date") {
        if (validateDate(field.id) == false) {
	        return false        
        }
        return true;
    }

    if (typename == "time") {
        if (validateTime(field) == false) {
	        return false        
        }
        return true;
    }
    
    if (typename == "pastdate") {
        if (validateDate(field.id) == false) {
	        return false        
        }
        return true;
    }

    if (typename == "numeric") {
        if (validateNumeric(field) == false) 
        {
	        showAlert("Please enter a valid numeric value for " + fieldname + ".");
	        return false        
        }
        return true;
    }


    return false;
}

function validateNumeric(field)
{
    var ret = true;
    
    if (isNaN(field.value))
        ret = false;        
    
    return ret;
}

//This function is used to validate date in the format dd/mm/yyyy (U.K. style)
//dtControl is the client id of the control which contains the date

function validateDate(dtControl)
{
    var input = document.getElementById(dtControl)

    if (input.value === "")
        return true;

    var validformat=/^\d{1,2}\/\d{1,2}\/\d{4}$/ //Basic check for format validity
    var returnval=false
    if (!validformat.test(input.value))
    showAlert('The date format you have entered is invalid. Please enter a valid date in the format DD/MM/YYYY.')
    else{ //Detailed check for valid date ranges
    var dayfield=input.value.split('/')[0]
    var monthfield=input.value.split('/')[1]
    var yearfield=input.value.split('/')[2]
   
    var dayobj = new Date(yearfield, monthfield-1, dayfield)
    if ((dayobj.getMonth()+1!=monthfield)||(dayobj.getDate()!=dayfield)||(dayobj.getFullYear()!=yearfield))
    showAlert('Invalid Day, Month, or Year range detected. Please enter a valid date in the format DD/MM/YYYY.')
    else
    {
        returnval=true
    }
    }
    if (returnval==false) input.focus()
    return returnval
}

function ExtensionsOkay() {

    if (document.RecordForm.AttFile == null)
        return true;

    var extension = new Array();
    var fieldvalue = document.RecordForm.AttFile.value;

    if (fieldvalue == "")
        return true;

    // Step 2 of 2:
    // Add the file name extensions that are okay (with 
    //    the period), for the variables with their numbers 
    //    in sequential order, as many or as few as needed, 
    //    starting with 0. (These are case sensitive.)

    extension[0] = ".png";
    extension[1] = ".gif";
    extension[2] = ".jpg";
    extension[3] = ".jpeg";
    extension[4] = ".bmp";
    extension[5] = ".tiff";
    extension[6] = ".tif";
    extension[7] = ".dcm";
    extension[8] = ".pdf";
    extension[9] = ".doc";
    extension[10] = ".xls";
    extension[11] = ".xlsx";
    extension[12] = ".docx";

    // No other customization needed.
    var thisext = fieldvalue.substr(fieldvalue.lastIndexOf('.'));

    for (var i = 0; i < extension.length; i++) {
        if (thisext == extension[i]) { return true; }
    }

    showAlert("The file you are trying to upload is not supported by Zaptag. Only image files (jpg, gif, bmp, png, tiff), Word Documents, Excel Spreadsheets, PDF documents and X-Ray Images (dcm) are supported.");
    return false;
}

function validateTime(control)
{
    var minhour = 6;        //This and higher is considered 'am'

    var timevalue = new String(control.value);
    var errvalue = new String("");

    var minutes = new String("");
    var hours = new String("");
    var ampm = new String("");

    if (timevalue == "")
        {
        errvalue = "Blank";
        }

    if (timevalue.indexOf(":")!=-1)
    /*  This is a 'normal' time.  Split it out as necessary.  */
        {
        var tempArray = timevalue.split(":");
        hours = tempArray[0];
        if (isNaN(hours))
        /*  First split is not a valid number, crash out  */
            {
            errvalue = "Yes";
            }
        else
            {
            /*  Make sure the number is between 0 and 23  */
            if ((hours> 23) || (hours <0))
                {
                errvalue = "Yes";
                }
            else
                {
                if (hours> 12)
                    {
                    hours = hours - 12;
                    ampm = "pm";
                    }
                else if (hours == 12)
                    {
                    ampm = "pm";
                    }
                else if (hours == 0)
                    {
                    ampm = "am";
                    hours = 12;
                    }
                else
                    {
                    if (hours <minhour)
                        {
                        ampm = "pm";        
                        }
                    else
                        {
                        ampm = "am";
                        }
                    hours = hours;
                    }
                }
            }
        /*  Gen the minutes   */
        minutes = tempArray[1];
        if (minutes.indexOf("a")> -1)
            {
            ampm = "am";
            minutes = parseInt(minutes);
            }
        else if (minutes.indexOf("p")> -1)
            {
            ampm = "pm";
            minutes = parseInt(minutes);
            }
        else
            {
            minutes = parseInt(minutes);
            }
        /*  Make sure the minutes are in a valid range (00-59)    */
        if (isNaN(minutes))
            {
            errvalue = "Yes";
            minutes = 0;    //Just to make sure for later
            }
        else
            {
            if ((minutes < 0) || (minutes> 59))
                {
                minutes = 0;
                errvalue = "Yes";
                }
            else
                {
                //Minutes is valid
                if (minutes <10)
                    {
                    minutes = "0" + String(minutes);
                    }
                else
                    {
                    minutes = minutes;
                    }
                }
            }
        }
    else
    /*  Have to work a little harder  */
        {
        if (isNaN(timevalue))
            {
            errvalue = "Yes";
            }
        else
            {
            if ((timevalue.length == 3) || (timevalue.length ==4))
                {
                //Get the minutes
                minutes = timevalue.substr((timevalue.length -2), 2);
                if ((minutes>= 0) && (minutes <60))
                    {
                    //Ignore this
                    }
                else
                    {
                    errvalue = "Yes";
                    }
                
                //Get the hours
                hours = timevalue.substr(0, (timevalue.length - 2));
                if ((hours <0) || (hours> 23))
                    {
                    errvalue = "Yes";
                    }
                if (hours <10)
                    {
                    if (timevalue.substr(0,1)=="0")
                        {
                        if (hours == 0)
                            {
                            ampm = "am";
                            hours = 12;
                            }
                        else
                            {
                            ampm = "am";
                            }
                        }
                    else
                        {
                        if (hours <minhour)
                            {
                            ampm = "pm";
                            }
                        else
                            {
                            ampm = "am";
                            }
                        }
                    }
                else
                    {
                    if ((hours == 10) || (hours == 11))
                        {
                        ampm = "am";
                        }
                    else if (hours == 12)
                        {
                        ampm = "pm";
                        }
                    else
                        {
                        ampm = "pm";
                        hours = hours - 12;
                        }
                    }
                }
            else
                {
                errvalue = "Yes";
                }
            }
        }
    if (errvalue!="")
    /*  There was an error    */    
        {
        if (control.value != "")
            {
            showAlert("You appear to have entered an invalid time.  Please correct it.");
            control.select();
            control.focus();
            return false;
            }
        }
    else
        {
        control.value = (hours * 1) + ":" + minutes + " " + ampm;
        }

} 






