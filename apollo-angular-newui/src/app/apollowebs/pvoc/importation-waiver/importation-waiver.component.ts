import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-importation-waiver',
  templateUrl: './importation-waiver.component.html',
  styleUrls: ['./importation-waiver.component.css']
})
export class ImportationWaiverComponent implements OnInit{
  public ApplicantDetailsForm: FormGroup;
  public finishedFillingFirstForm: boolean = false;
  public firstFormButtonClicked: boolean = false;
  public finishedFillingSecondForm: boolean = false;
  public secondFormButtonClicked: boolean = false;

  public applicantFormErrors = {
    applicantName: '',
    phoneNumber: '',
    postalAddress: '',
    email: '',
    kraPin: ''
  };

  public secondFormData = {
    category: '',
    justification: '',
    productDescription: '',
    documentation: [],
    masterList:[]
  }

  public masterListData = {
    productDescriptionMaster: '',
    hsCodeMaster: '',
    unitMaster: '',
    quantityMaster: '',
    originCountryMaster: '',
    totalAmountMaster: '',
  }

  categoryList = [
    {value: 'cat-0', viewValue: 'Category 1'},
    {value: 'cat-1', viewValue: 'Category 2'},
    {value: 'cat-2', viewValue: 'Category 3'}
  ];

  descriptionList = [
    {value: 'desc-0', viewValue: 'Description 1'},
    {value: 'desc-1', viewValue: 'Description 2'},
    {value: 'desc-2', viewValue: 'Description 3'}
  ];

  masterDescriptionList = [
    {value: 'desc-0', viewValue: 'Master Description 1'},
    {value: 'desc-1', viewValue: 'Master Description 2'},
    {value: 'desc-2', viewValue: 'Master Description 3'}
  ];

  masterCountryList = [
    {value: 'ken-0', viewValue: 'Kenya'},
    {value: 'ug-1', viewValue: 'Uganda'},
    {value: 'tz-2', viewValue: 'Tanzania'}
  ];

  masterCurrencyList = [
    {value: 'ken-0', viewValue: 'Kes'},
    {value: 'ug-1', viewValue: 'UGA'},
    {value: 'tz-2', viewValue: 'Tz'}
  ]

  constructor(
    public formBuilder: FormBuilder,
    public snackbar: MatSnackBar
  ) { }

  ngOnInit() {
    this.createForm();
  }

  createForm(){
    this.ApplicantDetailsForm = this.formBuilder.group({
      applicantName: new FormControl('', Validators.required),
      phoneNumber: new FormControl('', Validators.required),
      postalAddress: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      kraPin: new FormControl('', Validators.required),
    });

    this.ApplicantDetailsForm.valueChanges.subscribe((data) => {
      this.applicantFormErrors = this.validateForm(this.ApplicantDetailsForm, this.applicantFormErrors, true);
      let registeredValues = this.ApplicantDetailsForm.value;
      if((registeredValues.applicantName) && (registeredValues.phoneNumber) && (registeredValues.postalAddress)
      && (registeredValues.email) && (registeredValues.kraPin) && (!this.applicantFormErrors.applicantName) &&
      (!this.applicantFormErrors.phoneNumber) && (!this.applicantFormErrors.postalAddress)
      && (!this.applicantFormErrors.email) && (!this.applicantFormErrors.kraPin)){
        this.finishedFillingFirstForm = true;
      } else {
        this.finishedFillingFirstForm = false;
      }
    });
  }

  selectOption(id: number) {
    //getted from binding
    console.log(this.secondFormData.category, this.secondFormData.productDescription)
  }

  importFile($event){
    if ($event.target.files.length == 0) {
      console.log("No file selected!");
      return
    }
    let file: File = $event.target.files[0];
    // console.log("file selected!",$event.target.files,  file.name);
    this.secondFormData.documentation.push({
      name: file.name,
      file
    });
    // console.log("file selected!", this.secondFormData.documentation);
    
  }

  submitDataFunction(){
    if(!this.firstFormButtonClicked){
      this.finishedFillingFirstForm = true;
      this.firstFormButtonClicked = true;
    } else {
      //it is the second part
    }
  }

  //Transfer current marsterlistdata to masterlist array pool
  addNewMasterSheet(){

  }

  //Submit master list
  addMasterListDataToMasterList(){

  }

  deleteFile(i){
    this.secondFormData.documentation.splice(i, 1);
    // delete this.secondFormData.documentation[i];
  }

  // return list of error messages
  public validationMessages() {
    const messages = {
      required: 'This field is required',
      email: 'This email address is invalid',
      invalid_characters: (matches: any[]) => {

        let matchedCharacters = matches;

        matchedCharacters = matchedCharacters.reduce((characterString, character, index) => {
          let string = characterString;
          string += character;

          if (matchedCharacters.length !== index + 1) {
            string += ', ';
          }

          return string;
        }, '');

        return `These characters are not allowed: ${matchedCharacters}`;
      },
    };

    return messages;
  }

  // Validate form instance
  // check_dirty true will only emit errors if the field is touched
  // check_dirty false will check all fields independent of
  // being touched or not. Use this as the last check before submitting
  public validateForm(formToValidate: FormGroup, formErrors: any, checkDirty?: boolean) {
    const form = formToValidate;

    for (const field in formErrors) {
      if (field) {
        formErrors[field] = '';
        const control = form.get(field);

        const messages = this.validationMessages();
        if (control && !control.valid) {
          if (!checkDirty || (control.dirty || control.touched)) {
            for (const key in control.errors) {
              if (key && key !== 'invalid_characters') {
                formErrors[field] = formErrors[field] || messages[key];
              } else {
                formErrors[field] = formErrors[field] || messages[key](control.errors[key]);
              }
            }
          }
        }
      }
    }

    return formErrors;
  }

}
