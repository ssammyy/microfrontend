import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators, FormControl} from '@angular/forms';
import {MatSnackBar} from '@angular/material/snack-bar';
import {PVOCService} from "../../../core/store/data/pvoc/pvoc.service";
import {MatDialog} from "@angular/material/dialog";
import {ProductDetailsComponent} from "../waiver-application/product-details/product-details.component";
import swal from "sweetalert2";
import {Router} from "@angular/router";

@Component({
    selector: 'app-importation-waiver',
    templateUrl: './importation-waiver.component.html',
    styleUrls: ['./importation-waiver.component.css']
})
export class ImportationWaiverComponent implements OnInit {
    public ApplicantDetailsForm: FormGroup;
    public ApplicationDetailsForm: FormGroup;
    public masterProductList: any[] = [];
    public finishedFillingFirstForm: boolean = false;
    public firstFormButtonClicked: boolean = false;
    private selectedFiles: File[] = []

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
        masterList: []
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
        {value: 'ken-0', viewValue: 'KES'},
        {value: 'ug-1', viewValue: 'UGA'},
        {value: 'tz-2', viewValue: 'Tz'}
    ]

    constructor(
        public formBuilder: FormBuilder,
        private pvoc: PVOCService,
        private dialog: MatDialog,
        private router:Router
    ) {
    }

    ngOnInit() {
        this.createForm();
        this.loadDefaults()
    }

    createForm() {
        // Step 1:
        this.ApplicantDetailsForm = this.formBuilder.group({
            applicantName: new FormControl('', Validators.required),
            telephoneNumber: new FormControl('', Validators.required),
            postalAddress: new FormControl('', Validators.required),
            email: new FormControl('', [Validators.required, Validators.email]),
            kraPin: new FormControl('', Validators.required),
        });
        // Step 2:
        this.ApplicationDetailsForm = this.formBuilder.group({
            category: new FormControl(null, Validators.required),
            justification: new FormControl('', Validators.required),
            productDescription: new FormControl(null, Validators.required)
        })

        this.ApplicantDetailsForm.valueChanges.subscribe((data) => {
            this.applicantFormErrors = this.validateForm(this.ApplicantDetailsForm, this.applicantFormErrors, true);
            let registeredValues = this.ApplicantDetailsForm.value;
            if ((registeredValues.applicantName) && (registeredValues.phoneNumber) && (registeredValues.postalAddress)
                && (registeredValues.email) && (registeredValues.kraPin) && (!this.applicantFormErrors.applicantName) &&
                (!this.applicantFormErrors.phoneNumber) && (!this.applicantFormErrors.postalAddress)
                && (!this.applicantFormErrors.email) && (!this.applicantFormErrors.kraPin)) {
                this.finishedFillingFirstForm = true;
            } else {
                this.finishedFillingFirstForm = false;
            }
        });
    }

    importFile($event) {
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
        this.selectedFiles.push(file)
        // console.log("file selected!", this.secondFormData.documentation);

    }

    submitDataFunction() {
        let data = this.ApplicantDetailsForm.value;
        data["category"] = this.ApplicationDetailsForm.value.category;
        data["justification"] = this.ApplicationDetailsForm.value.justification;
        data["products"] = this.masterProductList
        console.log(data)
        this.pvoc.applyForImportWaiver(data, this.selectedFiles)
            .subscribe(
                res => {
                    if(res.responseCode==='00') {
                        swal.fire({
                            title: res.message,
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        }).then(this.goBackHome);
                    } else {
                        swal.fire({
                            title: res.message,
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-danger form-wizard-next-btn ',
                            },
                            icon: 'error'
                        });
                    }
                    // console.log(res)
                }
            )
    }

    goBackHome() {
        this.router.navigateByUrl("/pvoc")
    }

    //Submit master list
    addMasterListDataToMasterList() {

    }

    addNewMasterItem() {
        let ref = this.dialog.open(ProductDetailsComponent, {
            data: {
                descriptions: this.masterDescriptionList,
                currencies: this.masterCurrencyList,
                countries: this.masterCountryList,
            }
        });
        // Get product data
        ref.afterClosed()
            .subscribe(
                res => {
                    console.log(res)
                    if (res) {
                        this.masterProductList.push(res)
                    }
                }
            )
    }

    deleteFile(i) {
        this.secondFormData.documentation.splice(i, 1);
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

    private loadDefaults() {
        //Load categories
        this.pvoc.getWaiverCategories().subscribe(
            res => {
                if (res.responseCode == "00") {
                    this.categoryList = res.data;
                } else {
                    console.log(res.message)
                    this.categoryList = []
                }
            }
        )
    }
}
